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
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.net.NetResult
import com.wj.sampleproject.repository.ArticleRepository
import kotlinx.coroutines.launch

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
        ArticleCollectionInterface by ArticleCollectionInterfaceImpl(repository),
        ArticleListPagingInterface by ArticleListPagingInterfaceImpl() {

    init {
        getArticleList = { pageNum ->
            val result = MutableLiveData<NetResult<ArticleListEntity>>()
            viewModelScope.launch {
                try {
                    result.value = repository.getCollectionList(pageNum)
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

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }
}