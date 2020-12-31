package com.wj.sampleproject.interfaces.impl

import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.interfaces.ArticleCollectionInterface
import com.wj.sampleproject.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * 文章收藏接口实现类
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
open class ArticleCollectionInterfaceImpl(private val repository: ArticleRepository)
    : ArticleCollectionInterface {

    override lateinit var viewModel: BaseViewModel

    /** 收藏文章[item] */
    override fun collect(item: ArticleEntity) {
        viewModel.run {
            viewModelScope.launch {
                try {
                    // 收藏
                    repository.collectArticleInside(item.id.orEmpty()).judge(onFailed = {
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
        }
    }

    /** 取消收藏文章[item] */
    override fun unCollect(item: ArticleEntity) {
        viewModel.run {
            viewModelScope.launch {
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
    }
}