package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.wj.android.ui.fragment.BaseBindingLibFragment
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.helper.ProgressDialogHelper

/**
 * Fragment 基类
 *
 * @author 王杰
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibFragment<VM, DB>() {

    /** 标记 - 第一次加载 */
    protected var firstLoad = true
        private set(value) {
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeData()
    }

    override fun onPause() {
        super.onPause()

        firstLoad = false
        ProgressDialogHelper.dismiss()
    }

    /**
     * 添加观察者
     */
    private fun observeData() {
        viewModel.snackbarData.observe(this, Observer {
            if (it.content.isNullOrBlank()) {
                return@Observer
            }
            val snackBar = Snackbar.make(mBinding.root, it.content.orEmpty(), it.duration)
            snackBar.setTextColor(it.contentColor)
            snackBar.setBackgroundTint(it.contentBgColor)
            if (it.actionText != null && it.onAction != null) {
                snackBar.setAction(it.actionText!!, it.onAction)
                snackBar.setActionTextColor(it.actionColor)
            }
            if (it.onCallback != null) {
                snackBar.addCallback(it.onCallback!!)
            }
            snackBar.show()
        })
        viewModel.progressData.observe(this, Observer { progress ->
            if (null == progress || !progress.show) {
                ProgressDialogHelper.dismiss()
            } else {
                ProgressDialogHelper.show(mContext, progress.cancelable)
            }
        })
        viewModel.uiCloseData.observe(this, Observer { close ->
            close?.let { model ->
                mContext.setResult(model.resultCode, model.result)
                mContext.finish()
            }
        })
        viewModel.showDialogData.observe(this, Observer { builder -> builder.build().show(mContext) })
    }
}