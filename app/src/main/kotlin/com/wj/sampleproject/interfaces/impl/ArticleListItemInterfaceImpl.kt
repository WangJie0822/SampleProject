package com.wj.sampleproject.interfaces.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.common.ext.condition
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.interfaces.ArticleCollectionInterface
import com.wj.sampleproject.interfaces.ArticleListItemInterface
import kotlinx.coroutines.launch

/**
 * 文章列表点击事件接口实现类
 *
 * - 创建时间：2021/1/22
 *
 * @author 王杰
 */
class ArticleListItemInterfaceImpl(
        private val viewModel: BaseViewModel,
        private val jumpToWebViewData: MutableLiveData<WebViewActivity.ActionModel>
) : ArticleListItemInterface {

    /** 文章列表条目点击 */
    override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
        jumpToWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.link.orEmpty())
    }

    /** 文章收藏点击 */
    override val onArticleCollectClick: (ArticleEntity) -> Unit = fun(item) {
        val impl = viewModel as? ArticleCollectionInterface ?: return
        viewModel.viewModelScope.launch {
            if (item.collected.get().condition) {
                // 已收藏，取消收藏
                item.collected.set(false)
                impl.unCollect(item, viewModel.snackbarData)
            } else {
                // 未收藏，收藏
                item.collected.set(true)
                impl.collect(item, viewModel.snackbarData)
            }
        }
    }
}