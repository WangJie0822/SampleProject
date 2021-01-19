package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.wj.sampleproject.R
import com.wj.sampleproject.adapter.ArticleListRvAdapter
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityQuestionAnswerBinding
import com.wj.sampleproject.databinding.SmartRefreshState
import com.wj.sampleproject.router.ROUTER_PATH_QA
import com.wj.sampleproject.viewmodel.QuestionAnswerViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 问答列表界面
 *
 * - 创建时间：2021/1/19
 *
 * @author 王杰
 */
@Route(path = ROUTER_PATH_QA)
class QuestionAnswerActivity
    : BaseActivity<QuestionAnswerViewModel, AppActivityQuestionAnswerBinding>() {

    override val viewModel: QuestionAnswerViewModel by viewModel()

    /** 文章列表适配器 */
    private val mArticlesAdapter: ArticleListRvAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_question_answer)

        // 配置文章列表
        mBinding.rvQa.let { rv ->
            rv.layoutManager = WrapContentLinearLayoutManager()
            rv.adapter = mArticlesAdapter.also {
                it.viewModel = viewModel.articleListEventInterface
                it.setEmptyView(R.layout.app_layout_placeholder)
            }
        }

        // 自动加载数据
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

        /** 使用 [context] 对象打开 QuestionAnswerActivity 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<QuestionAnswerActivity>()
        }
    }
}