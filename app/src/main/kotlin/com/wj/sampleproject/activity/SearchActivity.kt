package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.recyclerview.adapter.simple.SimpleRvAdapter
import cn.wj.android.recyclerview.layoutmanager.FlowLayoutManager
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivitySearchBinding
import com.wj.sampleproject.entity.HotSearchEntity
import com.wj.sampleproject.viewmodel.SearchViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 搜索界面
 */
class SearchActivity
    : BaseActivity<SearchViewModel, AppActivitySearchBinding>() {

    override val viewModel: SearchViewModel by viewModel()

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    /** 搜索热词列表适配器 */
    private val mHotSearchAdapter = SimpleRvAdapter<HotSearchEntity>(R.layout.app_recycler_item_hot_search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_search)

        // 配置热搜列表
        mBinding.rvHotSearch.let { rv ->
            rv.layoutManager = FlowLayoutManager()
            rv.adapter = mHotSearchAdapter.also {
                it.viewModel = viewModel
            }
        }
        // 配置文章列表
        mBinding.rvSearch.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel.articleListViewModel
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }

        // 获取热搜数据
        viewModel.getHotSearch()
    }

    override fun initObserve() {
        // 搜索热词
        viewModel.hotSearchData.observe(this, {
            // 刷新热词列表
            mHotSearchAdapter.refresh(it)
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
    }

    companion object {

        /** 使用 [context] 对象打开 [SearchActivity] 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<SearchActivity>()
        }
    }
}