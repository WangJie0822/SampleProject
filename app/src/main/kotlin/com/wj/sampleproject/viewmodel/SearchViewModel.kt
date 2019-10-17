package com.wj.sampleproject.viewmodel

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.ext.tagableScope
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.isNotNullAndBlank
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.toNewList
import cn.wj.android.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.HotSearchEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.CollectRepository
import com.wj.sampleproject.repository.SearchRepository
import kotlinx.coroutines.launch

/**
 * 搜索 ViewModel
 */
class SearchViewModel
/**
 * @param searchRepository 搜索相关数据仓库
 * @param collectRepository 收藏相关数据仓库
 */
constructor(
        private val searchRepository: SearchRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel(),
        ArticleListViewModel {

    /** 页码 */
    private var pageNum = NET_PAGE_START

    /** 热搜数据 */
    val hotSearchData = MutableLiveData<ArrayList<HotSearchEntity>>()
    /** 文章列表数据 */
    val articleListData = MutableLiveData<ArrayList<ArticleEntity>>()
    /** 跳转 WebView 数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 搜索关键字 */
    val keywords = BindingField("") { _, value ->
        if (value.isNullOrBlank()) {
            showHotSearch.set(true)
        }
    }

    /** 标记 - 是否显示搜索热词 */
    val showHotSearch = BindingField(true)

    /** 软键盘搜索 */
    val onSearchAction: (TextView, Int, KeyEvent?) -> Boolean = { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 搜索
            if (keywords.get().isNullOrBlank()) {
                snackbarData.postValue(SnackbarModel(R.string.app_please_enter_keywords))
            } else {
                onRefresh.invoke()
            }
        }
        false
    }

    /** 标记 - 是否正在刷新 */
    val refreshing: BindingField<Boolean> = BindingField(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNum = NET_PAGE_START
        getSearchList()
    }

    /** 标记 - 是否正在加载更多 */
    val loadMore: BindingField<Boolean> = BindingField(false)

    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getSearchList()
    }

    /** 标记 - 是否没有更多 */
    val noMore: BindingField<Boolean> = BindingField(true)

    /** 返回点击 */
    val onBackClick = {
        uiCloseData.postValue(UiCloseModel())
    }

    /** 文章 item 点击 */
    override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        jumpWebViewData.postValue(WebViewActivity.ActionModel(item.title.orEmpty(), item.link.orEmpty()))
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

    /** 热门搜索条目点击 */
    val onHotSearchItemClick: (HotSearchEntity) -> Unit = { item ->
        keywords.set(item.name)
        if (keywords.get().isNotNullAndBlank()) {
            onRefresh.invoke()
        }
    }

    /**
     * 获取热搜数据
     */
    fun getHotSearch() {
        tagableScope.launch {
            try {
                // 获取热搜数据
                val result = searchRepository.getHotSearch()
                if (result.success()) {
                    // 获取成功，刷新列表
                    hotSearchData.postValue(result.data.orEmpty())
                } else {
                    // 获取失败，提示
                    snackbarData.postValue(result.toError())
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getHotSearch")
                // 获取失败，提示、回滚收藏状态
                snackbarData.postValue(SnackbarModel(throwable.showMsg))
            }
        }
    }

    /**
     * 获取搜索列表
     */
    private fun getSearchList() {
        tagableScope.launch {
            try {
                // 获取文章列表数据
                val result = searchRepository.search(pageNum, keywords.get().orEmpty())
                if (result.success()) {
                    // 请求成功
                    val newList = articleListData.value.toNewList(result.data?.datas, refreshing.get())
                    if (newList.isNotEmpty()) {
                        showHotSearch.set(false)
                    }
                    articleListData.postValue(newList)
                    noMore.set(result.data?.over?.toBoolean().condition)
                } else {
                    snackbarData.postValue(SnackbarModel(result.errorMsg))
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getSearchList")
                snackbarData.postValue(SnackbarModel(throwable.showMsg))
            } finally {
                refreshing.set(false)
                loadMore.set(false)
            }
        }
    }

    /**
     * 收藏
     *
     * @param item 文章对象
     */
    private fun collect(item: ArticleEntity) {
        tagableScope.launch {
            try {
                // 收藏
                val result = collectRepository.collectArticleInside(item.id.orEmpty())
                if (!result.success()) {
                    // 收藏失败，提示、回滚收藏状态
                    snackbarData.postValue(SnackbarModel(result.errorMsg))
                    item.collected.set(false)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "collect")
                // 收藏失败，提示、回滚收藏状态
                snackbarData.postValue(SnackbarModel(throwable.showMsg))
                item.collected.set(false)
            }
        }
    }

    /**
     * 取消收藏
     *
     * @param item 文章对象
     */
    private fun unCollect(item: ArticleEntity) {
        tagableScope.launch {
            try {
                // 取消收藏
                val result = collectRepository.unCollectArticleList(item.id.orEmpty())
                if (!result.success()) {
                    // 取消收藏失败，提示、回滚收藏状态
                    snackbarData.postValue(SnackbarModel(result.errorMsg))
                    item.collected.set(true)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "unCollect")
                // 取消收藏失败，提示、回滚收藏状态
                snackbarData.postValue(SnackbarModel(throwable.showMsg))
                item.collected.set(true)
            }
        }
    }
}