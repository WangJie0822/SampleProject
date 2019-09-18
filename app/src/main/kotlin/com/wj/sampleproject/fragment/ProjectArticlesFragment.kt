package com.wj.sampleproject.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.ACTION_CATEGORY
import com.wj.sampleproject.databinding.AppFragmentProjectArticlesBinding
import com.wj.sampleproject.entity.CategoryEntity
import com.wj.sampleproject.mvvm.ProjectArticlesViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 项目文章页面
 */
class ProjectArticlesFragment
    : BaseFragment<ProjectArticlesViewModel, AppFragmentProjectArticlesBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 公众号文章列表 Fragment
         */
        fun actionCreate(bjnews: CategoryEntity): ProjectArticlesFragment {
            return ProjectArticlesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ACTION_CATEGORY, bjnews)
                }
            }
        }
    }

    override val mViewModel: ProjectArticlesViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_project_articles

    private val category: CategoryEntity? by lazy { arguments?.getParcelable<CategoryEntity>(ACTION_CATEGORY) }

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun initBefore() {
        // 保存分类 id
        mViewModel.categoryId = category?.id.orEmpty()
    }

    override fun initView() {
        // 配置文章列表
        mArticlesAdapter.mViewModel = mViewModel
        mBinding.rvProjectArticles.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvProjectArticles.adapter = mArticlesAdapter

        // 添加观察者
        // 文章列表
        mViewModel.articleListData.observe(this, Observer {
            // 更新文章列表
            mArticlesAdapter.loadData(it.list, it.refresh)
        })
    }
}