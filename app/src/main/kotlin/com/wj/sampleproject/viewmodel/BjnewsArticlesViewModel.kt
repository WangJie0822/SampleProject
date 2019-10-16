package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.toNewList
import cn.wj.android.logger.Logger
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.repository.BjnewsRepository
import com.wj.sampleproject.repository.CollectRepository
import kotlinx.coroutines.launch

/**
 * 公众号文章 ViewModel
 */
class BjnewsArticlesViewModel
/**
 * @param bjnewsRepository 公众号相关数据仓库
 * @param collectRepository 收藏相关数据仓库
 */
constructor(
        private val bjnewsRepository: BjnewsRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel(),
        ArticleListViewModel {

    /** 公众号 id */
    var bjnewsId = ""
    /** 页码 */
    private var pageNum = NET_PAGE_START

    /** 文章列表数据 */
    val articleListData = MutableLiveData<ArrayList<ArticleEntity>>()

    /** 标记 - 是否正在刷新 */
    val refreshing: BindingField<Boolean> = BindingField(false)

    /** 刷新回调 */
    val onRefresh: () -> Unit = {
        pageNum = NET_PAGE_START
        noMore.set(false)
        getBjnewsArticles()
    }

    /** 标记 - 是否正在加载更多 */
    val loadMore: BindingField<Boolean> = BindingField(false)

    /** 加载更多回调 */
    val onLoadMore: () -> Unit = {
        pageNum++
        getBjnewsArticles()
    }

    /** 标记 - 是否没有更多 */
    val noMore: BindingField<Boolean> = BindingField(false)

    /** 文章 item 点击 */
    override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        WebViewActivity.actionStart(title = item.title.orEmpty(), url = item.link.orEmpty())
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
     * 获取公众号文章列表
     */
    private fun getBjnewsArticles() {
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                val result = bjnewsRepository.getBjnewsArticles(bjnewsId, pageNum)
                if (result.success()) {
                    // 请求成功
                    articleListData.postValue(articleListData.value.toNewList(result.data?.datas, refreshing.get()))
                    noMore.set(result.data?.over?.toBoolean().condition)
                } else {
                    snackbarData.postValue(SnackbarModel(result.errorMsg))
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
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
        viewModelScope.launch {
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
        viewModelScope.launch {
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