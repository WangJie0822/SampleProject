package com.wj.sampleproject.activity

import android.os.Bundle
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.databinding.AppActivitySplashBinding
import com.wj.sampleproject.mvvm.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * 欢迎界面
 */
class SplashActivity
    : BaseActivity<SplashViewModel, AppActivitySplashBinding>() {

    override val mViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_splash)
    }
}