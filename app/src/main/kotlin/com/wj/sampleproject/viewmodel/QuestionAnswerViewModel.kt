package com.wj.sampleproject.viewmodel

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.ArticleRepository

/**
 * 问答界面 ViewModel
 *
 * - 创建时间：2021/1/19
 *
 * @author 王杰
 */
class QuestionAnswerViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    init {
        viewModel = this
        getArticleList = { pageNum ->
            repository.getQaList(pageNum)
        }
    }

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

}