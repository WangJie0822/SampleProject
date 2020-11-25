package com.wj.sampleproject.fragment

import androidx.core.os.bundleOf
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.constants.ACTION_CATEGORY
import com.wj.sampleproject.constants.EVENT_COLLECTION_CANCELLED
import com.wj.sampleproject.databinding.AppFragmentBjnewsArticlesBinding
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.entity.CategoryEntity
import com.wj.sampleproject.viewmodel.BjnewsArticlesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 公众号文章页面
 */
class BjnewsArticlesFragment
    : BaseFragment<BjnewsArticlesViewModel, AppFragmentBjnewsArticlesBinding>() {

    override val viewModel: BjnewsArticlesViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_bjnews_articles

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onResume() {
        super.onResume()

        if (firstLoad) {
            // 刷新数据
            viewModel.refreshing.value = SmartRefreshState(true)
        }
    }

    override fun initView() {
        // 获取公众号 id
        viewModel.bjnewsId = arguments?.getParcelable<CategoryEntity>(ACTION_CATEGORY)?.id.orEmpty()

        // 配置文章列表
        mBinding.rvBjnewsArticles.let { rv ->
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
        LiveEventBus.get(EVENT_COLLECTION_CANCELLED, String::class.java)
                .observe(this, { id ->
                    val item = mArticlesAdapter.mDiffer.currentList.firstOrNull { it.id == id }
                    item?.collected?.set(false)
                })
    }

    companion object {

        /** 创建并返回 [BjnewsArticlesFragment]，携带参数目录信息[bjnews] */
        fun actionCreate(bjnews: CategoryEntity): BjnewsArticlesFragment {
            return BjnewsArticlesFragment().apply {
                arguments = bundleOf(
                        ACTION_CATEGORY to bjnews
                )
            }
        }
    }
}