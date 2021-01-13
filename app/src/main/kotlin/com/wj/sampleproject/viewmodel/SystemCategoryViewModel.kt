package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.SystemCategoryEntity
import com.wj.sampleproject.ext.defaultFaildBlock
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.repository.ArticleRepository
import com.wj.sampleproject.tools.toSnackbarModel
import kotlinx.coroutines.launch

/**
 * 体系目录列表 ViewModel，注入 [repository] 获取数据
 */
class SystemCategoryViewModel(
        private val repository: ArticleRepository
) : BaseViewModel() {

    /** 列表数据 */
    val listData = MutableLiveData<ArrayList<SystemCategoryEntity>>()

    /** 跳转体系列表 */
    val jumpSystemData = MutableLiveData<SystemCategoryEntity>()

    /** 目录点击 */
    val onCategoryItemClick: (SystemCategoryEntity) -> Unit = { item ->
        jumpSystemData.value = item
    }

    /** 获取分类数据 */
    fun getSystemCategoryList() {
        viewModelScope.launch {
            try {
                repository.getSystemCategoryList()
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