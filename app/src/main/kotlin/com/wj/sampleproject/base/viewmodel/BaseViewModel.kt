package com.wj.sampleproject.base.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wj.android.ui.viewmodel.BaseLibViewModel
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel

/**
 * ViewModel 基类
 *
 * > 提供了 **Snackbar 提示** [snackbarData] 以及 **UI 关闭** [uiCloseData] 数据对象
 */
abstract class BaseViewModel
    : BaseLibViewModel() {

    /** Snackbar 控制 */
    val snackbarData = MutableLiveData<SnackbarModel>()

    /** 控制 UI 组件关闭 */
    val uiCloseData = MutableLiveData<UiCloseModel>()

    /** 界面跳转控制 */
    val uiNavigationData = MutableLiveData<String>()
}