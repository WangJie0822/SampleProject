package com.wj.sampleproject.fragment

import cn.wj.android.base.adapter.FragVpAdapter
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentSystemBinding
import com.wj.sampleproject.mvvm.SystemViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 体系
 */
class SystemFragment
    : BaseFragment<SystemViewModel, AppFragmentSystemBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 体系 Fragment
         */
        fun actionCreate(): SystemFragment {
            return SystemFragment()
        }
    }

    override val viewModel: SystemViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_system

    override fun initView() {
        // 配置 ViewPager
        mBinding.vpSystem.adapter = FragVpAdapter.newBuilder()
                .manager(childFragmentManager)
                .frags(arrayListOf(
                        SystemCategoryFragment.actionCreate(),
                        NavigationFragment.actionCreate())
                )
                .build()
    }
}