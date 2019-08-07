package com.wj.sampleproject.base.ui

import androidx.databinding.ViewDataBinding
import cn.wj.android.base.ui.dialog.BaseBindingLibDialog
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Dialog 基类
 *
 * @author 王杰
 */
abstract class BaseDialog<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibDialog<VM, DB>()