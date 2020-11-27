package com.wj.sampleproject.viewmodel

import androidx.lifecycle.*
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.copy
import cn.wj.android.common.ext.orElse
import cn.wj.android.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleListEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.net.NetResult
import com.wj.sampleproject.repository.BjnewsRepository
import com.wj.sampleproject.repository.CollectRepository
import kotlinx.coroutines.launch

/**
 * 公众号文章 ViewModel
 *
 * @param bjnewsRepository 公众号相关数据仓库
 * @param collectRepository 收藏相关数据仓库
 */
class BjnewsArticlesViewModel(
        private val bjnewsRepository: BjnewsRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel(),
        ArticleListViewModel {

    /** 公众号 id */
    var bjnewsId = ""

    /** 页码 */
    private var pageNumber: MutableLiveData<Int> = MutableLiveData()

    /** 文章列表返回数据 */
    private val articleListResultData: LiveData<NetResult<ArticleListEntity>> = pageNumber.switchMap { pageNum ->
        getBjnewsArticles(pageNum)
    }

    /** 文章列表数据 */
    val articleListData: LiveData<ArrayList<ArticleEntity>> = articleListResultData.map { result ->
        disposeArticleListResult(result)
    }

    /** 跳转 WebView 数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

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

    /** 文章 item 点击 */
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

    /** 获取公众号文章列表 */
    private fun getBjnewsArticles(pageNum: Int): LiveData<NetResult<ArticleListEntity>> {
        val result = MutableLiveData<NetResult<ArticleListEntity>>()
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                result.value = bjnewsRepository.getBjnewsArticles(bjnewsId, pageNum)
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getBjnewsArticles")
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