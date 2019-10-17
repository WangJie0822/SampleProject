package com.wj.sampleproject.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import cn.wj.android.base.utils.AppManager
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivitySearchBinding
import com.wj.sampleproject.viewmodel.SearchViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 搜索界面
 */
class SearchActivity
    : BaseActivity<SearchViewModel, AppActivitySearchBinding>() {

    override val viewModel: SearchViewModel by viewModel()

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_search)

        // 配置文章列表
        mArticlesAdapter.viewModel = viewModel
        mArticlesAdapter.setEmptyView(R.layout.app_layout_placeholder)
        mArticlesAdapter.showHeaderWhenEmpty = true
        mBinding.rvSearch.layoutManager = WrapContentLinearLayoutManager()
        mBinding.rvSearch.adapter = mArticlesAdapter
    }

    override fun initObserve() {
        // 文章列表
        viewModel.articleListData.observe(this, Observer {
            // 更新文章列表
            mArticlesAdapter.submitList(it)
        })
        // WebView 跳转
        viewModel.jumpWebViewData.observe(this, Observer {
            WebViewActivity.actionStart(mContext, it.title, it.url)
        })
    }

    companion object {
        /**
         * 界面入口
         *
         * @param context Context 对象
         */
        fun actionStart(context: Context = AppManager.getContext()) {
            context.startActivity(Intent(context, SearchActivity::class.java).apply {
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }
}