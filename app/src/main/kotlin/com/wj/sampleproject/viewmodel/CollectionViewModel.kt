package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.copy
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.logger.Logger
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.EVENT_COLLECTION_CANCELLED
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.CollectRepository
import kotlinx.coroutines.launch

/**
 * 收藏 ViewModel
 *
 * @param collectRepository 收藏相关数据仓库
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
class CollectionViewModel(
        private val collectRepository: CollectRepository
) : BaseViewModel(), ArticleListViewModel {

    /** 页码 */
    private var pageNum = NET_PAGE_START

    /** 文章列表数据 */
    val articleListData = MutableLiveData<ArrayList<ArticleEntity>>()

    /** 跳转 WebView 数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

    /** 标记 - 是否正在刷新 */
    val refreshing: ObservableBoolean = ObservableBoolean(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNum = NET_PAGE_START
        noMore.set(false)
        getCollectionList()
    }

    /** 标记 - 是否正在加载更多 */
    val loadMore: ObservableBoolean = ObservableBoolean(false)

    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getCollectionList()
    }

    /** 标记 - 是否没有更多 */
    val noMore: ObservableBoolean = ObservableBoolean(false)

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
        }
    }

    /** 获取收藏列表 */
    private fun getCollectionList() {
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                val result = collectRepository.getCollectionList(pageNum)
                if (result.success()) {
                    // 请求成功
                    articleListData.value = articleListData.value.copy(result.data?.datas, refreshing.get())
                    noMore.set(result.data?.over?.toBoolean().condition)
                } else {
                    snackbarData.value = SnackbarModel(result.errorMsg)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getCollectionList")
                snackbarData.value = SnackbarModel(throwable.showMsg)
            } finally {
                refreshing.set(false)
                loadMore.set(false)
            }
        }
    }

    /** 取消收藏文章[item] */
    private fun unCollect(item: ArticleEntity) {
        viewModelScope.launch {
            try {
                // 取消收藏
                val result = collectRepository.unCollectArticleCollected(item.id.orEmpty(), item.originId.orEmpty())
                if (result.success()) {
                    // 取消收藏成功，从列表移除文章
                    val ls = arrayListOf<ArticleEntity>()
                    ls.addAll(articleListData.value.orEmpty())
                    ls.remove(item)
                    articleListData.value = (ls)
                    // 发送取消收藏通知
                    LiveEventBus.get(EVENT_COLLECTION_CANCELLED, String::class.java)
                            .post(item.originId.orEmpty())
                } else {
                    // 取消收藏失败，提示、回滚收藏状态
                    snackbarData.value = (SnackbarModel(result.errorMsg))
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