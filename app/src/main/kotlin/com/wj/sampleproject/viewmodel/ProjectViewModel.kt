package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.ext.tagableScope
import cn.wj.android.common.ext.orEmpty
import cn.wj.android.logger.Logger
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.entity.CategoryEntity
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.repository.ProjectRepository
import kotlinx.coroutines.launch

/**
 * 项目 ViewModel
 */
class ProjectViewModel
/**
 * @param repository 项目相关数据仓库
 */
constructor(private val repository: ProjectRepository)
    : BaseViewModel() {

    /** 项目分类数据 */
    val listData = MutableLiveData<ArrayList<CategoryEntity>>()

    /**
     * 获取新项目分类列表
     */
    fun getProjectCategory() {
        tagableScope.launch {
            try {
                val result = repository.getProjectCategory()
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