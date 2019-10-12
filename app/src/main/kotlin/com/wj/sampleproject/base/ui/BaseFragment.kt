package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.ui.fragment.BaseBindingLibFragment
import com.google.android.material.snackbar.Snackbar
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Fragment 基类
 *
 * @author 王杰
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibFragment<VM, DB>() {

    /** 标记 - 第一次加载 */
    protected var firstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeData()
    }

    override fun onPause() {
        super.onPause()

        firstLoad = false
    }

    /**
     * 添加观察者
     */
    private fun observeData() {
        viewModel.snackbarData.observe(this, Observer {
            if (it.content.isNullOrBlank()) {
                return@Observer
            }
            val snackbar = Snackbar.make(mBinding.root, it.content.orEmpty(), it.duration)
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
        viewModel.uiCloseData.observe(this, Observer { close ->
            if (close) {
                // 关闭 Activity
                mContext.setResult(viewModel.resultCode, viewModel.resultData)
                mContext.finish()
            }
        })
    }
}