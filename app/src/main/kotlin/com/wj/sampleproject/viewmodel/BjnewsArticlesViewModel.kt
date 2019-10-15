package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.utils.AppManager
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.toNewList
import cn.wj.android.logger.Logger
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListViewModel
import com.wj.sampleproject.base.SnackbarEntity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.repository.BjnewsRepository
import kotlinx.coroutines.launch

/**
 * 公众号文章 ViewModel
 */
class BjnewsArticlesViewModel
/**
 * @param repository 公众号相关数据仓库
 */
constructor(private val repository: BjnewsRepository)
    : BaseViewModel(),
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

    override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        // 跳转 WebView 打开
        WebViewActivity.actionStart(AppManager.getContext(), item.title.orEmpty(), item.link.orEmpty())
    }

    /**
     * 获取公众号文章列表
     */
    private fun getBjnewsArticles() {
        viewModelScope.launch {
            try {
                // 获取文章列表数据
                val result = repository.getBjnewsArticles(bjnewsId, pageNum)
                if (result.success()) {
                    // 请求成功
                    articleListData.postValue(articleListData.value.toNewList(result.data?.datas, refreshing.get()))
                    noMore.set(result.data?.over?.toBoolean().condition)
                } else {
                    snackbarData.postValue(SnackbarEntity(result.errorMsg))
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.postValue(SnackbarEntity(throwable.showMsg))
            } finally {
                refreshing.set(false)
                loadMore.set(false)
            }
        }
    }
}