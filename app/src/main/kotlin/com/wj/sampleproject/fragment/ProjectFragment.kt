package com.wj.sampleproject.fragment

import androidx.lifecycle.Observer
import cn.wj.android.base.adapter.FragVpAdapter
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentProjectBinding
import com.wj.sampleproject.mvvm.ProjectViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 项目
 */
class ProjectFragment
    : BaseFragment<ProjectViewModel, AppFragmentProjectBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 体系 Fragment
         */
        fun actionCreate(): ProjectFragment {
            return ProjectFragment()
        }
    }

    override val mViewModel: ProjectViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_project

    override fun initView() {
// 添加观察者
        mViewModel.listData.observe(this, Observer {
            // 配置 ViewPager
            mBinding.vpProject.adapter = FragVpAdapter.newBuilder()
                    .manager(childFragmentManager)
                    .creator(object : FragVpAdapter.Creator {
                        override val count: Int
                            get() = it.size

                        override fun createFragment(position: Int) = ProjectArticlesFragment.actionCreate(it[position])
                    })
                    .pageTitle { _, i -> it[i].name.orEmpty() }
                    .build()
            mBinding.stlProject.setViewPager(mBinding.vpProject)
        })
    }
}