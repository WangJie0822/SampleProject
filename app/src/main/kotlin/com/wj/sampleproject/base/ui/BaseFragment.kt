package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.wj.android.ui.fragment.BaseBindingLibFragment
import com.wj.sampleproject.base.viewmodel.BaseViewModel

/**
 * Fragment 基类
 * > 指定 ViewModel 类型 [VM] & 指定 DataBinding 类型 [DB]
 *
 * @author 王杰
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibFragment<VM, DB>() {

    /** 标记 - 第一次加载 */
    protected var firstLoad = true
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 绑定观察者
        observeData()
    }

    override fun onPause() {
        super.onPause()

        // 标记不是第一次加载
        firstLoad = false
    }

    /**
     * 添加观察者
     */
    private fun observeData() {
        // snackbar 提示
        viewModel.snackbarData.observe(this, Observer {
            if (it.content.isNullOrBlank()) {
                return@Observer
            }
            val snackBar = Snackbar.make(mBinding.root, it.content.orEmpty(), it.duration)
            snackBar.setTextColor(it.contentColor)
            snackBar.setBackgroundTint(it.contentBgColor)
            if (it.actionText != null && it.onAction != null) {
                snackBar.setAction(it.actionText, it.onAction)
                snackBar.setActionTextColor(it.actionColor)
            }
            if (it.onCallback != null) {
                snackBar.addCallback(it.onCallback)
            }
            snackBar.show()
        })
        // UI 关闭
        viewModel.uiCloseData.observe(this, { close ->
            close?.let { model ->
                mContext.setResult(model.resultCode, model.result)
                mContext.finish()
            }
        })
    }
}