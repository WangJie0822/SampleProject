package com.wj.sampleproject.fragment

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

    }
}