package com.wj.sampleproject.interfaces

import com.wj.sampleproject.entity.ArticleEntity

/**
 * 文章列表点击事件接口
 *
 * - 创建时间：2021/1/22
 *
 * @author 王杰
 */
interface ArticleListItemInterface {

    /** 文章列表条目点击 */
    val onArticleItemClick: (ArticleEntity) -> Unit

    /** 文章收藏点击 */
    val onArticleCollectClick: (ArticleEntity) -> Unit
}