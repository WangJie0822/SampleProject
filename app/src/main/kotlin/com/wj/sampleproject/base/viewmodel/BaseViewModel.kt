package com.wj.sampleproject.base.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wj.android.ui.viewmodel.BaseLibViewModel
import com.wj.sampleproject.dialog.GeneralDialog
import com.wj.sampleproject.model.ProgressModel
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import org.koin.core.KoinComponent

/**
 * MVVM ViewModel 基类
 */
abstract class BaseViewModel
    : BaseLibViewModel(),
        KoinComponent {
    
    /** 弹窗显示数据  */
    val showDialogData = MutableLiveData<GeneralDialog.Builder>()
    
    /** Snackbar 控制 */
    val snackbarData = MutableLiveData<SnackbarModel>()
    
    /** 控制进度条弹窗显示  */
    val progressData = MutableLiveData<ProgressModel>()
    
    /** 控制 UI 组件关闭 */
    val uiCloseData = MutableLiveData<UiCloseModel>()
}