package com.wj.sampleproject.fragment

import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentBjnewsBinding
import com.wj.sampleproject.mvvm.BjnewsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 公众号
 */
class BjnewsFragment
    : BaseFragment<BjnewsViewModel, AppFragmentBjnewsBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 公众号 Fragment
         */
        fun actionCreate(): BjnewsFragment {
            return BjnewsFragment()
        }
    }

    override val mViewModel: BjnewsViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_bjnews

    override fun initView() {

    }
}