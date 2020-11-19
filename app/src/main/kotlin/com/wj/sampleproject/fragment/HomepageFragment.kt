package com.wj.sampleproject.fragment

import androidx.core.view.updateLayoutParams
import cn.wj.android.base.ext.color
import cn.wj.android.base.ext.fitsStatusBar
import cn.wj.android.base.tools.getDeviceScreenWidth
import cn.wj.android.base.tools.getStatusBarHeight
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.SearchActivity
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.adapter.BannerVpAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.EVENT_COLLECTION_CANCLED
import com.wj.sampleproject.databinding.AppFragmentHomepageBinding
import com.wj.sampleproject.viewmodel.HomepageViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        // 配置标题栏
        mBinding.abl.updateLayoutParams {
            height = getDeviceScreenWidth() / 2
        }
        mBinding.ctl.run {
            setCollapsedTitleTextColor(R.color.app_white.color)
            setExpandedTitleColor(R.color.app_transparent.color)
        }
        mBinding.toolbar.run {
            updateLayoutParams {
                height += getStatusBarHeight()
            }
            fitsStatusBar()
        }

        // 配置 banner
        mBinding.vpBanner.adapter = mBannerAdapter.also { adapter ->
            adapter.viewModel = viewModel
        }

        // 配置文章列表
        mBinding.rvArticles.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel
                it.setEmptyView(R.layout.app_layout_placeholder)
                it.showHeaderWhenEmpty(true)
            }
        }
    }

    override fun initObserve() {
        // Banner 列表
        viewModel.bannerData.observe(this, { list ->
            // 更新 Banner 列表
            mBannerAdapter.refresh(list)
            // 设置 Banner 数量并开启轮播
            viewModel.bannerCount = list.size
        })
        // 文章列表
        viewModel.articleListData.observe(this, {
            // 更新文章列表
            mArticlesAdapter.submitList(it)
        })
        // WebView 跳转
        viewModel.jumpWebViewData.observe(this, {
            WebViewActivity.actionStart(mContext, it)
        })
        // 跳转搜索
        viewModel.jumpSearchData.observe(this, {
            SearchActivity.actionStart(mContext)
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
         * @return 首页 Fragment
         */
        fun actionCreate(): HomepageFragment {
            return HomepageFragment()
        }
    }
}