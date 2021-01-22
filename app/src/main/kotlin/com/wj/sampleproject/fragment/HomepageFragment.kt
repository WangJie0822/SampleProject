package com.wj.sampleproject.fragment

import cn.wj.android.recyclerview.adapter.simple.SimpleRvAdapter
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.EVENT_COLLECTION_CANCELLED
import com.wj.sampleproject.databinding.AppFragmentHomepageBinding
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.helper.UserInfoData
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
    private val mBannerAdapter = SimpleRvAdapter<BannerEntity>(R.layout.app_viewpager_item_banner)

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onResume() {
        super.onResume()

        if (firstLoad) {
            // 获取 Banner 数据
            viewModel.getHomepageBannerList()
            // 刷新文章列表
            viewModel.refreshing.value = SmartRefreshState(true)
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
        // 配置 banner
        mBinding.vpBanner.adapter = mBannerAdapter.also { adapter ->
            adapter.viewModel = viewModel
        }

        // 配置文章列表
        mBinding.rvArticles.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel.articleListItemInterface
                it.setEmptyView(R.layout.app_layout_placeholder)
                it.showHeaderWhenEmpty(true)
            }
        }

        // [Snackbar] 转换
        snackbarTransform = { snackbarModel ->
            snackbarModel.copy(targetId = R.id.cl_target)
        }
    }

    override fun initObserve() {
        // 用户信息
        UserInfoData.observe(this, { userInfo ->
            if (null == userInfo) {
                // 用户未登录，清空收藏状态
                mArticlesAdapter.mDiffer.currentList.forEach {
                    it.collected.set(false)
                }
            } else {
                // 已登录
                if (mArticlesAdapter.mDiffer.currentList.isEmpty()) {
                    // 列表为空，第一次回调，不处理
                    return@observe
                }
                // 刷新列表
                viewModel.refreshing.value = SmartRefreshState(true)
            }
        })
        // 取消收藏事件
        LiveEventBus.get(EVENT_COLLECTION_CANCELLED, String::class.java)
                .observe(this, { id ->
                    val item = mArticlesAdapter.mDiffer.currentList.firstOrNull {
                        it.id == id
                    }
                    item?.collected?.set(false)
                })
        viewModel.run {
            // Banner 列表
            bannerData.observe(this@HomepageFragment, { list ->
                // 更新 Banner 列表
                mBannerAdapter.refresh(list)
                // 设置 Banner 数量并开启轮播
                bannerCount = list.size
            })
            // 文章列表
            articleListData.observe(this@HomepageFragment, {
                // 更新文章列表
                mArticlesAdapter.submitList(it)
            })
            // WebView 跳转
            jumpWebViewData.observe(this@HomepageFragment, {
                WebViewActivity.actionStart(mContext, it)
            })
        }
    }

    companion object {

        /** 创建 [HomepageFragment] 并返回 */
        fun actionCreate(): HomepageFragment {
            return HomepageFragment()
        }
    }
}