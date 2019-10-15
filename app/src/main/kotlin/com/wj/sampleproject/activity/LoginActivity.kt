package com.wj.sampleproject.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivityLoginBinding
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
    }

    companion object {
        /**
         * 界面入口
         *
         * @param context [Context] 对象
         */
        fun actionStart(context: Context) {
            if (AppManager.contains(LoginActivity::class.java)) {
                return
            }
            val intent = Intent(context, LoginActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}