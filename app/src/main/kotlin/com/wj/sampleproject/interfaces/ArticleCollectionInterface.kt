package com.wj.sampleproject.interfaces

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.ArticleEntity

/**
 * 文章收藏接口
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
interface ArticleCollectionInterface {

    var viewModel: BaseViewModel

    /** 收藏文章[item] */
    fun collect(item: ArticleEntity)

    /** 取消收藏文章[item] */
    fun unCollect(item: ArticleEntity)
}