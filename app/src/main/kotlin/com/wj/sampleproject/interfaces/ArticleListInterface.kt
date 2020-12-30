package com.wj.sampleproject.interfaces

import androidx.lifecycle.MutableLiveData
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListEventInterface

/**
 * 文章列表逻辑接口
 * > 包含 item 点击跳转逻辑、收藏、取消收藏逻辑
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
interface ArticleListInterface : ArticleCollectionInterface {

    /** 跳转 WebView 数据 */
    val jumpWebViewData: MutableLiveData<WebViewActivity.ActionModel>

    /** 文章列表的 `viewModel` 对象 */
    val articleListEventInterface: ArticleListEventInterface
}