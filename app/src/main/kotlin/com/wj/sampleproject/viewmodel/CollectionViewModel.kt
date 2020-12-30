package com.wj.sampleproject.viewmodel

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.ArticleRepository

/**
 * 收藏 ViewModel，注入 [repository] 获取数据
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
class CollectionViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    init {
        getArticleList = { pageNum ->
            repository.getCollectionList(pageNum)
        }
    }

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }
}