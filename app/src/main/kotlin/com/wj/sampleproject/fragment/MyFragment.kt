package com.wj.sampleproject.fragment

import com.wj.sampleproject.R
import com.wj.sampleproject.activity.*
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentMyBinding
import com.wj.sampleproject.viewmodel.MyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 我的
 */
class MyFragment
    : BaseFragment<MyViewModel, AppFragmentMyBinding>() {

    override val viewModel: MyViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_my

    override fun initView() {
    }

    override fun initObserve() {
        viewModel.run {
            // 跳转设置
            jumpToSettingsData.observe(this@MyFragment, {
                SettingsActivity.actionStart(mContext)
            })
            // 跳转登录
            jumpToLoginData.observe(this@MyFragment, {
                LoginActivity.actionStart(mContext)
            })
            // 跳转我的收藏
            jumpToCollectionData.observe(this@MyFragment, {
                CollectionActivity.actionStart(mContext)
            })
            // 跳转网站收藏
            jumpToCollectedWebData.observe(this@MyFragment, {
                CollectedWebActivity.actionStart(mContext)
            })
            // 跳转学习相关界面
            jumpToStudyData.observe(this@MyFragment, {
                StudyActivity.actionStart(mContext)
            })
        }
    }

    companion object {

        /** 创建 [MyFragment] 并返回 */
        fun actionCreate(): MyFragment {
            return MyFragment()
        }
    }
}