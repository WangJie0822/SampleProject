package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.ArticleListEntity
import com.wj.sampleproject.interfaces.ArticleCollectionInterface
import com.wj.sampleproject.interfaces.ArticleListItemInterface
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleCollectionInterfaceImpl
import com.wj.sampleproject.interfaces.impl.ArticleListItemInterfaceImpl
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.net.NetResult
import com.wj.sampleproject.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * 公众号文章 ViewModel，注入 [repository] 获取数据
 */
class ProjectArticlesViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleCollectionInterface by ArticleCollectionInterfaceImpl(repository),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl() {

    /** 项目分类 id */
    var categoryId = ""

    init {
        getArticleList = { pageNum ->
            val result = MutableLiveData<NetResult<ArticleListEntity>>()
            viewModelScope.launch {
                try {
                    result.value = repository.getProjectList(categoryId, pageNum)
                } catch (throwable: Throwable) {
                    Logger.t("NET").e(throwable, "getArticleList")
                }
            }
            result
        }
    }

    /** 跳转网页数据 */
    val jumpWebViewData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 列表事件 */
    val articleListItemInterface: ArticleListItemInterface by lazy {
        ArticleListItemInterfaceImpl(this, jumpWebViewData)
    }
}