package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.ext.tagableScope
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.logger.Logger
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.entity.CategoryEntity
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.repository.BjnewsRepository
import kotlinx.coroutines.launch

/**
 * 公众号 ViewModel
 */
class BjnewsViewModel
/**
 * @param repository 公众号相关数据仓库
 */
constructor(private val repository: BjnewsRepository)
    : BaseViewModel() {

    /** 公众号数据 */
    val bjnewsData = MutableLiveData<ArrayList<CategoryEntity>>()

    /**
     * 获取公众号列表
     */
    fun getBjnewsList() {
        tagableScope.launch {
            try {
                val result = repository.getBjnewsList()
                if (result.success()) {
                    // 获取成功
                    bjnewsData.postValue(result.data.orEmpty())
                } else {
                    snackbarData.postValue(result.toError())
                }
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.postValue(throwable.snackbarMsg)
            }
        }
    }

}