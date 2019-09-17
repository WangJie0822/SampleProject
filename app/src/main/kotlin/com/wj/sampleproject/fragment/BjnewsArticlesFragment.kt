package com.wj.sampleproject.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.ACTION_BJNEWS
import com.wj.sampleproject.databinding.AppFragmentBjnewsArticlesBinding
import com.wj.sampleproject.entity.BjnewsEntity
import com.wj.sampleproject.mvvm.BjnewsArticlesViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 公众号文章页面
 */
class BjnewsArticlesFragment
    : BaseFragment<BjnewsArticlesViewModel, AppFragmentBjnewsArticlesBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 公众号文章列表 Fragment
         */
        fun actionCreate(bjnews: BjnewsEntity): BjnewsArticlesFragment {
            return BjnewsArticlesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ACTION_BJNEWS, bjnews)
                }
            }
        }
    }

    override val pageTitle: String? by lazy { bjnews?.name.orEmpty() }

    override val mViewModel: BjnewsArticlesViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_bjnews_articles

    private val bjnews: BjnewsEntity? by lazy { arguments?.getParcelable<BjnewsEntity>(ACTION_BJNEWS) }

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun initBefore() {
        // 保存公众号 id
        mViewModel.bjnewsId = bjnews?.id.orEmpty()
    }

    override fun initView() {
        // 配置文章列表
        mArticlesAdapter.mViewModel = mViewModel
        mBinding.rvBjnewsArticles.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvBjnewsArticles.adapter = mArticlesAdapter

        // 添加观察者
        // 文章列表
        mViewModel.articleListData.observe(this, Observer {
            // 更新文章列表
            mArticlesAdapter.loadData(it.list, it.refresh)
        })
    }
}