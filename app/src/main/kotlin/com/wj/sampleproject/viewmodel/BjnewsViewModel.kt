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
 * 公众号 ViewModel，注入 [repository] 获取数据
 */
class BjnewsViewModel(
        private val repository: ArticleRepository
) : BaseViewModel() {

    /** 公众号数据 */
    val bjnewsData = MutableLiveData<ArrayList<CategoryEntity>>()

    /** 获取公众号列表 */
    fun getBjnewsList() {
        viewModelScope.launch {
            try {
                repository.getBjnewsList()
                        .judge(onSuccess = {
                            // 获取成功
                            bjnewsData.value = data.orEmpty()
                        }, onFailed = defaultFaildBlock)
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "NET_ERROR")
                snackbarData.value = throwable.toSnackbarModel()
            }
        }
    }

}