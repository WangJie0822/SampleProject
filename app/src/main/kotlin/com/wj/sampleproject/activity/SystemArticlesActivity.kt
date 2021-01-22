package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.ACTION_SYSTEM_ID
import com.wj.sampleproject.constants.ACTION_TITLE
import com.wj.sampleproject.databinding.AppActivitySystemArticlesBinding
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.router.ROUTER_PATH_SYSTEM
import com.wj.sampleproject.viewmodel.SystemArticlesViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 体系文章列表
 *
 * - 创建时间：2019/10/17
 *
 * @author 王杰
 */
@Route(path = ROUTER_PATH_SYSTEM)
class SystemArticlesActivity : BaseActivity<SystemArticlesViewModel, AppActivitySystemArticlesBinding>() {

    override val viewModel: SystemArticlesViewModel by viewModel()

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_system_articles)

        // 获取体系相关数据
        viewModel.titleStr.set(intent.getStringExtra(ACTION_TITLE).orEmpty())
        viewModel.cid = intent.getStringExtra(ACTION_SYSTEM_ID).orEmpty()

        // 配置文章列表
        mBinding.rvSystemArticles.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel.articleListItemInterface
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }

        // 自动刷新列表
        viewModel.refreshing.value = SmartRefreshState(true)
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

        /** 使用 [context] 打开 [SystemArticlesActivity] 界面，传递参数 标题[title]&体系目录id[cid] */
        fun actionStart(context: Context, title: String, cid: String) {
            context.startTargetActivity<SystemArticlesActivity>(bundleOf(
                    ACTION_TITLE to title,
                    ACTION_SYSTEM_ID to cid
            ))
        }

    }
}