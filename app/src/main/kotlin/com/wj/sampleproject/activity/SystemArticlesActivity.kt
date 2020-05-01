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
import com.wj.sampleproject.constants.ACTION_SYSTEM_ID
import com.wj.sampleproject.constants.ACTION_TITLE
import com.wj.sampleproject.databinding.AppActivitySystemArticlesBinding
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
                it.viewModel = viewModel
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }

        // 自动刷新列表
        viewModel.refreshing.set(true)
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
         * @param title 标题
         * @param cid 体系目录 id
         */
        fun actionStart(context: Context = AppManager.getContext(), title: String, cid: String) {
            context.startActivity(Intent(context, SystemArticlesActivity::class.java).apply {
                putExtra(ACTION_TITLE, title)
                putExtra(ACTION_SYSTEM_ID, cid)
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }

    }
}