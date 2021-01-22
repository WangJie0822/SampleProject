package com.wj.sampleproject.interfaces

import androidx.lifecycle.MutableLiveData
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.model.SnackbarModel

/**
 * 文章收藏接口
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
interface ArticleCollectionInterface {

    /** 收藏文章[item]，使用 [snackbarData] 弹出提示 */
    suspend fun collect(item: ArticleEntity, snackbarData: MutableLiveData<SnackbarModel>)

    /** 取消收藏文章[item]，使用 [snackbarData] 弹出提示 */
    suspend fun unCollect(item: ArticleEntity, snackbarData: MutableLiveData<SnackbarModel>)
}