package com.wj.sampleproject.fragment

import android.os.Bundle
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.ACTION_CATEGORY
import com.wj.sampleproject.constants.EVENT_COLLECTION_CANCLED
import com.wj.sampleproject.databinding.AppFragmentProjectArticlesBinding
import com.wj.sampleproject.entity.CategoryEntity
import com.wj.sampleproject.viewmodel.ProjectArticlesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 项目文章页面
 */
class ProjectArticlesFragment
    : BaseFragment<ProjectArticlesViewModel, AppFragmentProjectArticlesBinding>() {

    override val viewModel: ProjectArticlesViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_project_articles

    private val category: CategoryEntity? by lazy { arguments?.getParcelable(ACTION_CATEGORY) }

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onResume() {
        super.onResume()

        if (firstLoad) {
            // 刷新列表
            viewModel.refreshing.set(true)
        }
    }

    override fun initView() {
        // 保存分类 id
        viewModel.categoryId = category?.id.orEmpty()

        // 配置文章列表
        mBinding.rvProjectArticles.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }
    }

    override fun initObserve() {
        // 文章列表
        viewModel.articleListData.observe(this, {
            // 更新文章列表
            mArticlesAdapter.submitList(it)
        })
        // WebView 跳转
        viewModel.jumpWebViewData.observe(this, {
            WebViewActivity.actionStart(mContext, it)
        })
        // 取消收藏事件
        LiveEventBus.get(EVENT_COLLECTION_CANCLED, String::class.java)
                .observe(this, { id ->
                    val item = mArticlesAdapter.mDiffer.currentList.firstOrNull { it.id == id }
                    item?.collected?.set(false)
                })
    }

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
}