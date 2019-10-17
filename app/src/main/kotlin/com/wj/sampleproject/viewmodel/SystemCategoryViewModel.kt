package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.logger.Logger
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.entity.SystemCategoryEntity
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.repository.SystemRepository
import kotlinx.coroutines.launch

/**
 * 体系目录列表 ViewModel
 */
class SystemCategoryViewModel
/**
 * @param repository 体系相关数据仓库
 */
constructor(private val repository: SystemRepository)
    : BaseViewModel() {

    /** 列表数据 */
    val listData = MutableLiveData<ArrayList<SystemCategoryEntity>>()
    /** 跳转体系列表 */
    val jumpSystemData = MutableLiveData<SystemCategoryEntity>()

    /** 目录点击 */
    val onCategoryItemClick: (SystemCategoryEntity) -> Unit = { item ->
        jumpSystemData.postValue(item)
    }

    /**
     * 获取分类数据
     */
    fun getSystemCategoryList() {
        viewModelScope.launch {
            try {
                val result = repository.getSystemCategoryList()
                if (result.success()) {
                    // 获取成功
                    listData.postValue(result.data.orEmpty())
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