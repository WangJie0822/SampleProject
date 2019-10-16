package com.wj.sampleproject.base.ui

import android.content.res.Resources
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.ui.activity.BaseBindingLibActivity
import com.google.android.material.snackbar.Snackbar
import com.wj.sampleproject.R
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.helper.ProgressDialogHelper

/**
 * Activity 基类
 *
 * @author 王杰
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibActivity<VM, DB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeData()
    }

    override fun onPause() {
        super.onPause()
        ProgressDialogHelper.dismiss()
    }

    override fun getResources(): Resources? {
        // 禁止app字体大小跟随系统字体大小调节
        val resources = super.getResources()
        if (resources != null && resources.configuration.fontScale != 1.0f) {
            val configuration = resources.configuration
            configuration.fontScale = 1.0f
//            resources.updateConfiguration(configuration, resources.displayMetrics)
            createConfigurationContext(configuration)
        }
        return resources
    }

    override fun startAnim() {
        overridePendingTransition(R.anim.app_anim_right_in, R.anim.app_anim_alpha_out)
    }

    override fun finishAnim() {
        overridePendingTransition(R.anim.app_anim_alpha_in, R.anim.app_anim_right_out)
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
                setResult(model.resultCode, model.result)
                finish()
            }
        })
        viewModel.showDialogData.observe(this, Observer { builder -> builder.build().show(mContext) })
    }
}