package com.wj.sampleproject.fragment

import cn.wj.android.base.ext.string
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.CollectedWebActivity
import com.wj.sampleproject.activity.CollectionActivity
import com.wj.sampleproject.activity.LoginActivity
import com.wj.sampleproject.activity.StudyActivity
import com.wj.sampleproject.base.ui.BaseFragment
import com.wj.sampleproject.databinding.AppFragmentMyBinding
import com.wj.sampleproject.dialog.GeneralDialog
import com.wj.sampleproject.helper.ProgressDialogHelper
import com.wj.sampleproject.helper.UserInfoData
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
        // 用户信息
        UserInfoData.observe(this, { userInfo ->
            if (null == userInfo) {
                viewModel.userName.set(R.string.app_un_login.string)
                viewModel.avatarUrl.set("")
            } else {
                viewModel.userName.set(userInfo.username)
                viewModel.avatarUrl.set(userInfo.icon)
            }
        })
        // 进度弹窗
        viewModel.progressData.observe(this, { progress ->
            if (null == progress) {
                ProgressDialogHelper.dismiss()
            } else {
                ProgressDialogHelper.show(mContext, progress.cancelable, progress.hint)
            }
        })
        // 显示退出登录弹窗
        viewModel.showLogoutDialogData.observe(this, {
            GeneralDialog.newBuilder()
                    .contentStr(R.string.app_are_you_sure_to_logout.string)
                    .setOnPositiveAction {
                        // 退出登录
                        viewModel.logout()
                    }
                    .build()
                    .show(this)
        })
        // 跳转登录
        viewModel.jumpLoginData.observe(this, {
            LoginActivity.actionStart(mContext)
        })
        // 跳转我的收藏
        viewModel.jumpCollectionData.observe(this, {
            CollectionActivity.actionStart(mContext)
        })
        // 跳转网站收藏
        viewModel.jumpCollectedWebData.observe(this, {
            CollectedWebActivity.actionStart(mContext)
        })
        // 跳转学习相关界面
        viewModel.jumpToStudyData.observe(this, {
            StudyActivity.actionStart(mContext)
        })
    }

    companion object {

        /** 创建 [MyFragment] 并返回 */
        fun actionCreate(): MyFragment {
            return MyFragment()
        }
    }
}