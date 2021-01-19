package com.wj.sampleproject.fragment

import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentMyBinding
import com.wj.sampleproject.viewmodel.MyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 我的
 */
class MyFragment
    : BaseFragment<MyViewModel, AppFragmentMyBinding>() {

    override val viewModel: MyViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_my

    override fun initView() {
    }

    companion object {

        /** 创建 [MyFragment] 并返回 */
        fun actionCreate(): MyFragment {
            return MyFragment()
        }
    }
}