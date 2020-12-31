package com.wj.sampleproject.viewmodel

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.repository.ArticleRepository

/**
 * 公众号文章 ViewModel，注入 [repository] 获取数据
 */
class ProjectArticlesViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    /** 项目分类 id */
    var categoryId = ""

    init {
        viewModel = this
        getArticleList = { pageNum ->
            repository.getProjectList(categoryId, pageNum)
        }
    }
}