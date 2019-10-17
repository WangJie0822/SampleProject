package com.wj.sampleproject.fragment

import androidx.lifecycle.Observer
import cn.wj.android.base.tools.getString
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.CollectedWebActivity
import com.wj.sampleproject.activity.CollectionActivity
import com.wj.sampleproject.activity.LoginActivity
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentMyBinding
import com.wj.sampleproject.helper.UserHelper
import com.wj.sampleproject.viewmodel.MyViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 我的
 */
class MyFragment
    : BaseFragment<MyViewModel, AppFragmentMyBinding>() {

    override val viewModel: MyViewModel by viewModel()

    override val layoutResId: Int = R.layout.app_fragment_my

    override fun onResume() {
        super.onResume()

        if (null == UserHelper.userInfo) {
            viewModel.userName.set(R.string.app_un_login.getString())
            viewModel.avatarUrl.set("")
        } else {
            viewModel.userName.set(UserHelper.userInfo?.username)
            viewModel.avatarUrl.set(UserHelper.userInfo?.icon)
        }
    }

    override fun initView() {
    }

    override fun initObserve() {
        // 跳转登录
        viewModel.jumpLoginData.observe(this, Observer {
            LoginActivity.actionStart(mContext)
        })
        // 跳转我的收藏
        viewModel.jumpCollectionData.observe(this, Observer {
            CollectionActivity.actionStart(mContext)
        })
        // 跳转网站收藏
        viewModel.jumpCollectedWebData.observe(this, Observer {
            CollectedWebActivity.actionStart(mContext)
        })
    }

    companion object {
        /**
         * 创建 Fragment
         *
         * @return 体系 Fragment
         */
        fun actionCreate(): MyFragment {
            return MyFragment()
        }
    }
}