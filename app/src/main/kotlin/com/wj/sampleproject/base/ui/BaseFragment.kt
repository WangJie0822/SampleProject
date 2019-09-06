package com.wj.sampleproject.base.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.ui.fragment.BaseBindingLibFragment
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Fragment 基类
 *
 * @author 王杰
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibFragment<VM, DB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.uiClose.observe(this, Observer { close ->
            if (close) {
                // 关闭 Activity
                mContext.setResult(mViewModel.resultCode, mViewModel.resultData)
                mContext.finish()
            }
        })
    }
}