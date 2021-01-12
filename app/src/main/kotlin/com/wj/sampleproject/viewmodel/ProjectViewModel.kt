package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.CategoryEntity
import com.wj.sampleproject.ext.defaultFaildBlock
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.repository.ArticleRepository
import kotlinx.coroutines.launch

/**
 * 项目 ViewModel，注入 [repository] 获取数据
 */
class ProjectViewModel(
        private val repository: ArticleRepository
) : BaseViewModel() {

    /** 项目分类数据 */
    val listData = MutableLiveData<ArrayList<CategoryEntity>>()

    /** 获取新项目分类列表 */
    fun getProjectCategory() {
        viewModelScope.launch {
            try {
                repository.getProjectCategory()
                        .judge(onSuccess = {
                            // 获取成功
                            listData.value = data.orEmpty()
                        }, onFailed = defaultFaildBlock)
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.value = throwable.toSnackbarModel()
            }
        }
    }
}