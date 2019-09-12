package com.wj.sampleproject.fragment

import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentMyBinding
import com.wj.sampleproject.mvvm.MyViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 我的
 */
class MyFragment
    : BaseFragment<MyViewModel, AppFragmentMyBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 体系 Fragment
         */
        fun actionCreate(): MyFragment {
            return MyFragment()
        }
    }

    override val mViewModel: MyViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_my

    override fun initView() {

    }
}