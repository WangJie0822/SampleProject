package com.wj.sampleproject.base.ui

import androidx.databinding.ViewDataBinding
import cn.wj.android.base.ui.fragment.BaseBindingLibFragment
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Fragment 基类
 *
 * @author 王杰
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibFragment<VM, DB>()