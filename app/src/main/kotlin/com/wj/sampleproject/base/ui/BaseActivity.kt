package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.ui.activity.BaseBindingLibActivity
import com.google.android.material.snackbar.Snackbar
import com.wj.sampleproject.base.mvvm.BaseViewModel

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

    override fun startAnim() {
    }

    override fun finishAnim() {
    }

    /**
     * 添加观察者
     */
    private fun observeData() {
        mViewModel.snackbarData.observe(this, Observer {
            val snackbar = Snackbar.make(mBinding.root, it.content, it.duration)
            snackbar.setTextColor(it.contentColor)
            snackbar.setBackgroundTint(it.contentBgColor)
            if (it.actionText != null && it.onAction != null) {
                snackbar.setAction(it.actionText!!, it.onAction)
                snackbar.setActionTextColor(it.actionColor)
            }
            if (it.onCallback != null) {
                snackbar.addCallback(it.onCallback!!)
            }
            snackbar.show()
        })
        mViewModel.uiCloseData.observe(this, Observer { close ->
            if (close) {
                // 关闭 Activity
                setResult(mViewModel.resultCode, mViewModel.resultData)
                finish()
            }
        })
    }
}