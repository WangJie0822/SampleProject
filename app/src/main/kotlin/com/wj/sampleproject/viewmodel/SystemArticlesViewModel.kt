package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableField
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.interfaces.ArticleListPagingInterface
import com.wj.sampleproject.interfaces.impl.ArticleListPagingInterfaceImpl
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.ArticleRepository

/**
 * 体系文章列表 ViewModel，注入 [repository] 获取数据
 *
 * - 创建时间：2019/10/17
 *
 * @author 王杰
 */
class SystemArticlesViewModel(
        private val repository: ArticleRepository
) : BaseViewModel(),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl(repository) {

    /** 体系目录 id */
    var cid = ""

    init {
        viewModel = this
        getArticleList = { pageNum ->
            repository.getSystemArticleList(pageNum, cid)
        }
    }

    /** 标题文本 */
    val titleStr: ObservableField<String> = ObservableField("")

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }
}