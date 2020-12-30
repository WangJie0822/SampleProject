package com.wj.sampleproject.viewmodel

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.repository.ArticleRepository

/**
 * 公众号文章 ViewModel，注入 [repository] 获取数据
 */
class BjnewsArticlesViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    /** 公众号 id */
    var bjnewsId = ""

    init {
        getArticleList = { pageNum ->
            repository.getBjnewsArticles(bjnewsId, pageNum)
        }
    }
}