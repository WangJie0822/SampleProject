package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.toNewList
import cn.wj.android.logger.Logger
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.CollectRepository
import com.wj.sampleproject.repository.SystemRepository
import kotlinx.coroutines.launch

/**
 * 体系文章列表 ViewModel
 *
 * @param systemRepository 体系相关数据仓库
 * @param collectRepository 收藏相关数据仓库
 *
 * - 创建时间：2019/10/17
 *
 * @author 王杰
 */
class SystemArticlesViewModel(
        private val systemRepository: SystemRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel(),
        ArticleListViewModel {
    
    /** 页码 */
    private var pageNum = NET_PAGE_START
    
    /** 体系目录 id */
    var cid = ""
    
    /** 文章列表数据 */
    val articleListData = MutableLiveData<ArrayList<ArticleEntity>>()
    
    /** 跳转 WebView 数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()
    
    /** 标题文本 */
    val titleStr: ObservableField<String> = ObservableField("")
    
    /** 标记 - 是否正在刷新 */
    val refreshing: ObservableBoolean = ObservableBoolean(false)
    
    /** 标记 - 是否正在加载更多 */
    val loadMore: ObservableBoolean = ObservableBoolean(false)
    
    /** 标记 - 是否没有更多 */
    val noMore: ObservableBoolean = ObservableBoolean(false)
    
    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNum = NET_PAGE_START
        noMore.set(false)
        getArticleList()
    }
    
    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getArticleList()
    }
    
    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }
    
    /** 文章 item 点击 */
    override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        jumpWebViewData.value = WebViewActivity.ActionModel(item.title.orEmpty(), item.link.orEmpty())
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
    
    /**
     * 获取文章列表
     */
    private fun getArticleList() {
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                val result = systemRepository.getSystemArticleList(pageNum, cid)
                if (result.success()) {
                    // 请求成功
                    articleListData.value = articleListData.value.toNewList(result.data?.datas, refreshing.get())
                    noMore.set(result.data?.over?.toBoolean().condition)
                } else {
                    snackbarData.value = SnackbarModel(result.errorMsg)
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "getArticleList")
                snackbarData.value = SnackbarModel(throwable.showMsg)
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
    
    /**
     * 取消收藏
     *
     * @param item 文章对象
     */
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