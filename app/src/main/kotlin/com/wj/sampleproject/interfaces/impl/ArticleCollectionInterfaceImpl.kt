package com.wj.sampleproject.interfaces.impl

import androidx.lifecycle.MutableLiveData
import cn.wj.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.interfaces.ArticleCollectionInterface
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.repository.ArticleRepository
import com.wj.sampleproject.tools.toSnackbarModel

/**
 * 文章收藏接口实现类
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
class ArticleCollectionInterfaceImpl(private val repository: ArticleRepository)
    : ArticleCollectionInterface {

    /** 收藏文章[item]，使用 [snackbarData] 弹出提示 */
    override suspend fun collect(item: ArticleEntity, snackbarData: MutableLiveData<SnackbarModel>) {
        try {
            // 收藏
            repository.collectArticleInside(item.id.orEmpty())
                    .judge(onFailed = {
                        // 收藏失败，提示、回滚收藏状态
                        snackbarData.value = this.toSnackbarModel()
                        item.collected.set(false)
                    })
        } catch (throwable: Throwable) {
            Logger.t("NET").e(throwable, "collect")
            // 收藏失败，提示、回滚收藏状态
            snackbarData.value = throwable.toSnackbarModel()
            item.collected.set(false)
        }
    }

    /** 取消收藏文章[item]，使用 [snackbarData] 弹出提示 */
    override suspend fun unCollect(item: ArticleEntity, snackbarData: MutableLiveData<SnackbarModel>) {
        try {
            // 取消收藏
            repository.unCollectArticleList(item.id.orEmpty()).judge(onFailed = {
                // 取消收藏失败，提示、回滚收藏状态
                snackbarData.value = toSnackbarModel()
                item.collected.set(true)
            })
        } catch (throwable: Throwable) {
            Logger.t("NET").e(throwable, "unCollect")
            // 取消收藏失败，提示、回滚收藏状态
            snackbarData.value = throwable.toSnackbarModel()
            item.collected.set(true)
        }
    }
}