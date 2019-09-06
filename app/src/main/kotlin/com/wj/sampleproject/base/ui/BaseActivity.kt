package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.ui.activity.BaseBindingLibActivity
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

        mViewModel.uiClose.observe(this, Observer { close ->
            if (close) {
                // 关闭 Activity
                setResult(mViewModel.resultCode, mViewModel.resultData)
                finish()
            }
        })
    }

    override fun startAnim() {
    }

    override fun finishAnim() {
    }
}