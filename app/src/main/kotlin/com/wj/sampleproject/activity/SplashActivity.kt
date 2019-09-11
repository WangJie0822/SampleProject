package com.wj.sampleproject.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
        setContentView(R.layout.app_activity_splash)
    }
}