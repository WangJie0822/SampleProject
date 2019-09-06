package com.wj.sampleproject.fragment

import androidx.lifecycle.Observer
import cn.wj.android.base.ext.condition
import cn.wj.android.base.ext.orEmpty
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.BannerVpAdapter
import com.wj.sampleproject.adapter.HomepageArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentHomepageBinding
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

    /** Banner 列表适配器对象 */
    private val mBannerAdapter: BannerVpAdapter by inject()
    /** 文章列表适配器对象 */
    private val mArticleAdapter: HomepageArticleListRvAdapter by inject()

    override fun initView() {
        // 配置 ViewPager
        mBannerAdapter.mViewModel = mViewModel
        mBinding.vpBanner.adapter = mBannerAdapter

        // 配置 RecyclerView
        mArticleAdapter.mViewModel = mViewModel
        mBinding.rvArticles.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvArticles.adapter = mArticleAdapter

        // 注册数据监听
        mViewModel.bannerListData.observe(this, Observer {
            mViewModel.bannerCount = it.size
            mBannerAdapter.refresh(it)
        })
        mViewModel.articleListData.observe(this, Observer {
            if (it.refresh.condition) {
                mArticleAdapter.mData.clear()
            }
            mArticleAdapter.mData.addAll(it.list.orEmpty())
            mArticleAdapter.notifyDataSetChanged()
        })
    }
}