package com.wj.sampleproject.base.mvvm

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.mvvm.BaseMvvmViewModel
import com.wj.sampleproject.base.SnackbarEntity
import com.wj.sampleproject.dialog.GeneralDialog
import com.wj.sampleproject.model.ProgressModel
import org.koin.core.KoinComponent

/**
 * MVVM ViewModel 基类
 */
abstract class BaseViewModel
    : BaseMvvmViewModel(),
        KoinComponent {

    /** 弹窗显示数据  */
    val showDialogData = MutableLiveData<GeneralDialog.Builder>()

    /** Snackbar 控制 */
    val snackbarData = MutableLiveData<SnackbarEntity>()

    /** 控制进度条弹窗显示  */
    val progressData = MutableLiveData<ProgressModel>()

    /** 控制 UI 组件关闭 */
    val uiCloseData = MutableLiveData<Boolean>()

    /** 返回码 */
    var resultCode: Int = Activity.RESULT_CANCELED
    /** 返回数据 */
    var resultData: Intent? = null

    /**
     * 设置返回数据，仅对 Activity 有效
     */
    protected fun setResult(resultCode: Int = Activity.RESULT_OK, resultData: Intent? = null) {
        this.resultCode = resultCode
        this.resultData = resultData
    }
}