package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityCollectionBinding
import com.wj.sampleproject.viewmodel.CollectionViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 收藏界面
 * > 用户收藏的文章列表，可取消收藏
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
class CollectionActivity : BaseActivity<CollectionViewModel, AppActivityCollectionBinding>() {

    override val viewModel: CollectionViewModel by viewModel()

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_collection)

        // 配置文章列表
        mBinding.rvArticles.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel.articleListViewModel
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }

        // 自动加载数据
        viewModel.refreshing.set(true)
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
    }

    companion object {

        /** 使用 [context] 打开 [CollectionActivity] 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<CollectionActivity>()
        }
    }
}