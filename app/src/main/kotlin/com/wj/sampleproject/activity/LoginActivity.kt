package com.wj.sampleproject.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.wj.android.base.utils.AppManager
import cn.wj.android.common.ext.orFalse
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.ACTION_NET_TO_LOGIN
import com.wj.sampleproject.databinding.AppActivityLoginBinding
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.viewmodel.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 登录界面
 * <p/>
 * 创建时间：2019/10/14
 *
 * @author 王杰
 */
class LoginActivity : BaseActivity<LoginViewModel, AppActivityLoginBinding>() {

    override val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_login)

        if (intent.getBooleanExtra(ACTION_NET_TO_LOGIN, false).orFalse()) {
            // 从网络拦截跳转，提示
            viewModel.snackbarData.value = SnackbarModel(R.string.app_please_login_first)
        }
    }

    companion object {
        /**
         * 界面入口
         *
         * @param context [Context] 对象
         * @param fromNet 是否是从网络拦截跳转 默认 false
         */
        fun actionStart(context: Context, fromNet: Boolean = false) {
            if (AppManager.contains(LoginActivity::class.java)) {
                // 堆栈中已有登录页，返回
                return
            }
            val intent = Intent(context, LoginActivity::class.java).apply {
                putExtra(ACTION_NET_TO_LOGIN, fromNet)
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
            context.startActivity(intent)
        }
    }
}