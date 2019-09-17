package com.wj.sampleproject.fragment

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.adapter.BannerVpAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentHomepageBinding
import com.wj.sampleproject.databinding.AppLayoutHomepageBannerBinding
import com.wj.sampleproject.mvvm.HomepageViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 主界面 - 首页
 */
class HomepageFragment
    : BaseFragment<HomepageViewModel, AppFragmentHomepageBinding>() {

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 首页 Fragment
         */
        fun actionCreate(): HomepageFragment {
            return HomepageFragment()
        }
    }

    override val mViewModel: HomepageViewModel by viewModel()

    override val layoutResID: Int = R.layout.app_fragment_homepage

    /** Banner 列表适配器 */
    private val mBannerAdapter: BannerVpAdapter by inject()
    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun initView() {
        // 配置 Banner 列表
        val mBannerBinding = AppLayoutHomepageBannerBinding.inflate(
                LayoutInflater.from(mContext), null, false
        )
        mBannerAdapter.mViewModel = mViewModel
        mBannerBinding.vpBanner.adapter = mBannerAdapter

        // 配置文章列表
        mArticlesAdapter.mViewModel = mViewModel
        mArticlesAdapter.addHeaderView(mBannerBinding.root)
        mArticlesAdapter.setEmptyView(R.layout.app_layout_no_data)
        mBinding.rvArticles.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvArticles.adapter = mArticlesAdapter

        // 添加观察者
        // Banner 列表
        mViewModel.bannerData.observe(this, Observer {
            // 更新 Banner 列表
            mBannerAdapter.refresh(it)
            // 设置 Banner 数量并开启轮播
            mViewModel.bannerCount = it.size
        })
        // 文章列表
        mViewModel.articleListData.observe(this, Observer {
            // 更新文章列表
            mArticlesAdapter.loadData(it.list, it.refresh)
        })
    }
}