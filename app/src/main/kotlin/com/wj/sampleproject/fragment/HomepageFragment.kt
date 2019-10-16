package com.wj.sampleproject.fragment

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.adapter.BannerVpAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.EVENT_COLLECTION_CANCLED
import com.wj.sampleproject.databinding.AppFragmentHomepageBinding
import com.wj.sampleproject.databinding.AppLayoutHomepageBannerBinding
import com.wj.sampleproject.viewmodel.HomepageViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 主界面 - 首页
 */
class HomepageFragment
    : BaseFragment<HomepageViewModel, AppFragmentHomepageBinding>() {

    override val viewModel: HomepageViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_homepage

    /** Banner 列表适配器 */
    private val mBannerAdapter: BannerVpAdapter by inject()
    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onResume() {
        super.onResume()

        if (firstLoad) {
            // 获取 Banner 数据
            viewModel.getHomepageBannerList()
            // 刷新文章列表
            viewModel.refreshing.set(true)
        }
    }

    override fun onStart() {
        super.onStart()
        // 开启轮播
        viewModel.startCarousel()
    }

    override fun onStop() {
        super.onStop()
        // 关闭轮播
        viewModel.stopCarousel()
    }

    override fun initView() {

        // 配置文章列表
        mArticlesAdapter.viewModel = viewModel
        mBinding.rvArticles.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvArticles.adapter = mArticlesAdapter
    }

    override fun initObserve() {
        // Banner 列表
        viewModel.bannerData.observe(this, Observer {
            // 配置 Banner 列表
            val mBannerBinding = AppLayoutHomepageBannerBinding.inflate(
                    LayoutInflater.from(mContext), null, false
            )
            mBannerAdapter.mViewModel = viewModel
            mBannerBinding.vpBanner.adapter = mBannerAdapter
            mArticlesAdapter.addHeaderView(mBannerBinding.root)
            // 更新 Banner 列表
            mBannerAdapter.refresh(it)
            // 设置 Banner 数量并开启轮播
            viewModel.bannerCount = it.size
        })
        // 文章列表
        viewModel.articleListData.observe(this, Observer {
            // 更新文章列表
            mArticlesAdapter.submitList(it)
        })
        // 取消收藏事件
        LiveEventBus.get(EVENT_COLLECTION_CANCLED, String::class.java)
                .observe(this, Observer { id ->
                    val item = mArticlesAdapter.mDiffer.currentList.firstOrNull { it.id == id }
                    item?.collected?.set(false)
                })
    }

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
}