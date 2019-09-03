package com.wj.sampleproject.base.ui

import androidx.databinding.ViewDataBinding
import cn.wj.android.base.ui.activity.BaseBindingLibActivity
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Activity 基类
 *
 * @author 王杰
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibActivity<VM, DB>() {

    override fun startAnim() {
    }

    override fun finishAnim() {
    }
}