package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.base.utils.AppManager
import com.gyf.immersionbar.ImmersionBar
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityLoginBinding
import com.wj.sampleproject.helper.ProgressDialogHelper
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 登录界面
 *
 * 创建时间：2019/10/14
 *
 * @author 王杰
 */
class LoginActivity : BaseActivity<LoginViewModel, AppActivityLoginBinding>() {

    override val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_login)

        // 关闭除首页外所有界面
        AppManager.finishAllWithout(LoginActivity::class.java, MainActivity::class.java)

        // 清除登录信息
        UserInfoData.value = null
    }

    override fun initImmersionbar(immersionBar: ImmersionBar) {
        immersionBar.run {
            statusBarColor(R.color.app_white)
            statusBarDarkFont(true)
        }
    }

    override fun initObserve() {
        // 进度弹窗
        viewModel.progressData.observe(this, { progress ->
            if (null == progress) {
                ProgressDialogHelper.dismiss()
            } else {
                ProgressDialogHelper.show(mContext, progress.cancelable, progress.hint)
            }
        })
    }

    companion object {

        /**
         * 使用 [context] 打开 [LoginActivity] 界面
         * > 栈堆已有登录页时不会重复打开
         */
        fun actionStart(context: Context) {
            if (AppManager.contains(LoginActivity::class.java)) {
                // 堆栈中已有登录页，返回
                return
            }
            context.startTargetActivity<LoginActivity>()
        }
    }
}