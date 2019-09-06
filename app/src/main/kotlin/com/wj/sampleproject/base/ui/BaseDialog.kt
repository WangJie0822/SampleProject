package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.ui.dialog.BaseBindingLibDialog
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Dialog 基类
 *
 * @author 王杰
 */
abstract class BaseDialog<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibDialog<VM, DB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.uiClose.observe(this, Observer { close ->
            if (close) {
                // 隐藏 Dialog
                dismiss()
            }
        })
    }
}