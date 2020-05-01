package com.wj.sampleproject.fragment

import androidx.fragment.app.Fragment
import cn.wj.android.base.adapter.FragVpAdapter
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.TAB_SYSTEM_SYSTEM
import com.wj.sampleproject.databinding.AppFragmentSystemBinding
import com.wj.sampleproject.viewmodel.SystemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 体系
 */
class SystemFragment
    : BaseFragment<SystemViewModel, AppFragmentSystemBinding>() {
    
    override val viewModel: SystemViewModel by viewModel()
    
    override val layoutResId: Int = R.layout.app_fragment_system
    
    override fun initView() {
        // 配置 ViewPager
        mBinding.vpSystem.adapter = FragVpAdapter.newBuilder()
                .manager(childFragmentManager)
                .creator(object : FragVpAdapter.Creator {
                    override val count: Int
                        get() = 2
            
                    override fun createFragment(position: Int): Fragment {
                        return if (position == TAB_SYSTEM_SYSTEM) {
                            SystemCategoryFragment.actionCreate()
                        } else {
                            NavigationFragment.actionCreate()
                        }
                    }
                })
                .build()
    }
    
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
}