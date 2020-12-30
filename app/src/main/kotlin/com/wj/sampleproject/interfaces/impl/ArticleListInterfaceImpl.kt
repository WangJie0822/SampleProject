package com.wj.sampleproject.interfaces.impl

import androidx.lifecycle.MutableLiveData
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.orEmpty
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListEventInterface
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.interfaces.ArticleCollectionInterface
import com.wj.sampleproject.interfaces.ArticleListInterface
import com.wj.sampleproject.repository.ArticleRepository

/**
 * 文章列表接口实现类
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
open class ArticleListInterfaceImpl(private val repository: ArticleRepository)
    : BaseViewModel(),
        ArticleCollectionInterface by ArticleCollectionInterfaceImpl(repository),
        ArticleListInterface {

    /** 跳转 WebView 数据 */
    override val jumpWebViewData: MutableLiveData<WebViewActivity.ActionModel> = MutableLiveData()

    /** 列表事件处理 */
    override val articleListEventInterface: ArticleListEventInterface = object : ArticleListEventInterface {

        /** 文章列表条目点击 */
        override val onArticleItemClick: (ArticleEntity) -> Unit = { item ->
            // 跳转 WebView 打开
            jumpWebViewData.value = WebViewActivity.ActionModel(item.id.orEmpty(), item.title.orEmpty(), item.link.orEmpty())
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
    }
}