package com.wj.sampleproject.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.base.adapter.FragVpAdapter
import cn.wj.android.base.adapter.creator
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentProjectBinding
import com.wj.sampleproject.viewmodel.ProjectViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 项目
 */
class ProjectFragment
    : BaseFragment<ProjectViewModel, AppFragmentProjectBinding>() {

    override val viewModel: ProjectViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取项目分类数据
        viewModel.getProjectCategory()
    }

    override fun initView() {

    }

    override fun initObserve() {
        // 项目列表
        viewModel.listData.observe(this, Observer {
            // 配置 ViewPager
            mBinding.vpProject.adapter = FragVpAdapter.newBuilder()
                    .manager(childFragmentManager)
                    .creator {
                        count(it.size)
                        createFragment { position ->
                            ProjectArticlesFragment.actionCreate(it[position])
                        }
                    }
                    .pageTitle { _, i -> it[i].name.orEmpty() }
                    .build()
            mBinding.stlProject.setViewPager(mBinding.vpProject)
        })
    }

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
}