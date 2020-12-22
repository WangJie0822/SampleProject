package com.wj.sampleproject.viewmodel

import android.view.inputmethod.EditorInfo
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import cn.wj.android.common.ext.*
import com.orhanobut.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleListEntity
import com.wj.sampleproject.entity.HotSearchEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.net.NetResult
import com.wj.sampleproject.repository.CollectRepository
import com.wj.sampleproject.repository.SearchRepository
import kotlinx.coroutines.launch

/**
 * 搜索 ViewModel
 *
 * @param searchRepository 搜索相关数据仓库
 * @param collectRepository 收藏相关数据仓库
 */
class SearchViewModel(
        private val searchRepository: SearchRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel() {

    /** 页码 */
    private val pageNumber: MutableLiveData<Int> = MutableLiveData()

    /** 搜索返回数据 */
    private val searchResultData: LiveData<NetResult<ArticleListEntity>> = pageNumber.switchMap { pageNum ->
        getSearchList(pageNum)
    }

    /** 文章列表数据 */
    val articleListData: LiveData<ArrayList<ArticleEntity>> = searchResultData.map { result ->
        disposeArticleListResult(result)
    }

    /** 热搜数据 */
    val hotSearchData = MutableLiveData<ArrayList<HotSearchEntity>>()

    /** 跳转 WebView 数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 搜索关键字 */
    val keywords: ObservableField<String> = ObservableField("")

    /** 标记 - 是否显示搜索热词 */
    val showHotSearch: ObservableBoolean = object : ObservableBoolean(keywords) {
        override fun get(): Boolean {
            return keywords.get().isNullOrBlank()
        }
    }

    /** 软键盘搜索 */
    val onSearchAction: (Int) -> Boolean = { actionId ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 搜索
            if (keywords.get().isNullOrBlank()) {
                snackbarData.value = SnackbarModel(R.string.app_please_enter_keywords)
                true
            } else {
                refreshing.value = SmartRefreshState(true)
                false
            }
        } else {
            false
        }
    }

    /** 刷新状态 */
    val refreshing: MutableLiveData<SmartRefreshState> = MutableLiveData()

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNumber.value = NET_PAGE_START
    }

    /** 加载更多状态 */
    val loadMore: MutableLiveData<SmartRefreshState> = MutableLiveData()

    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNumber.value = pageNumber.value.orElse(NET_PAGE_START) + 1
    }

    /** 返回点击 */
    val onBackClick = {
        uiCloseData.value = UiCloseModel()
    }

    /** 文章列表的 `viewModel` 对象 */
    val articleListViewModel: ArticleListViewModel = object : ArticleListViewModel {

        /** 文章列表条目点击 */
        override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
            // 跳转 WebView 打开
            jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.link.orEmpty())
        }

        /** 文章收藏点击 */
        override val onArticleCollectClick: (ArticleEntity) -> Unit = { item ->
            if (item.collected.get().condition) {
                // 已收藏，取消收藏
                item.collected.set(false)
                unCollect(item)
            } else {
                // 未收藏，收藏
                item.collected.set(true)
                collect(item)
            }
        }
    }

    /** 热门搜索条目点击 */
    val onHotSearchItemClick: (HotSearchEntity) -> Unit = { item ->
        keywords.set(item.name)
        if (keywords.get().isNotNullAndBlank()) {
            onRefresh.invoke()
        }
    }

    /** 获取热搜数据 */
    fun getHotSearch() {
        viewModelScope.launch {
            try {
                // 获取热搜数据
                val result = searchRepository.getHotSearch()
                if (result.success()) {
                    // 获取成功，刷新列表
                    hotSearchData.value = result.data.orEmpty()
                } else {
                    // 获取失败，提示
                    snackbarData.value = result.toError()
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getHotSearch")
                // 获取失败，提示、回滚收藏状态
                snackbarData.value = SnackbarModel(throwable.showMsg)
            }
        }
    }

    /** 根据页码 [pageNum] 获取对应关键字 [keywords] 的搜索结果 */
    private fun getSearchList(pageNum: Int): LiveData<NetResult<ArticleListEntity>> {
        val result = MutableLiveData<NetResult<ArticleListEntity>>()
        viewModelScope.launch {
            try {
                result.value = searchRepository.search(pageNum, keywords.get().orEmpty())
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getSearchList")
                result.value = NetResult.fromThrowable(throwable)
            }
        }
        return result
    }

    /** 处理文章列表返回数据 [result]，并返回文章列表 */
    private fun disposeArticleListResult(result: NetResult<ArticleListEntity>): ArrayList<ArticleEntity> {
        val refresh = pageNumber.value == NET_PAGE_START
        val smartControl = if (refresh) refreshing else loadMore
        return if (result.success()) {
            smartControl.value = SmartRefreshState(loading = false, success = true, noMore = result.data?.over.toBoolean())
            articleListData.value.copy(result.data?.datas, refresh)
        } else {
            smartControl.value = SmartRefreshState(loading = false, success = false)
            articleListData.value.orEmpty()
        }
    }

    /** 收藏文章[item] */
    private fun collect(item: ArticleEntity) {
        viewModelScope.launch {
            try {
                // 收藏
                val result = collectRepository.collectArticleInside(item.id.orEmpty())
                if (!result.success()) {
                    // 收藏失败，提示、回滚收藏状态
                    snackbarData.value = SnackbarModel(result.errorMsg)
                    item.collected.set(false)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "collect")
                // 收藏失败，提示、回滚收藏状态
                snackbarData.value = SnackbarModel(throwable.showMsg)
                item.collected.set(false)
            }
        }
    }

    /** 取消收藏文章[item] */
    private fun unCollect(item: ArticleEntity) {
        viewModelScope.launch {
            try {
                // 取消收藏
                val result = collectRepository.unCollectArticleList(item.id.orEmpty())
                if (!result.success()) {
                    // 取消收藏失败，提示、回滚收藏状态
                    snackbarData.value = SnackbarModel(result.errorMsg)
                    item.collected.set(true)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "unCollect")
                // 取消收藏失败，提示、回滚收藏状态
                snackbarData.value = SnackbarModel(throwable.showMsg)
                item.collected.set(true)
            }
        }
    }
}