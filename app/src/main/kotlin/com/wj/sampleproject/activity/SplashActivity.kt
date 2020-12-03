package com.wj.sampleproject.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.SPLASH_DELAY_MS
import com.wj.sampleproject.databinding.AppActivitySplashBinding
import com.wj.sampleproject.viewmodel.BlankViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 启动页
 */
class SplashActivity
    : BaseActivity<BlankViewModel, AppActivitySplashBinding>() {

    override val viewModel: BlankViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_splash)

        lifecycleScope.launch {
            // 延时 1000ms
            delay(SPLASH_DELAY_MS)
            // 跳转主界面
            MainActivity.actionStart(mContext)
            // 结束当前界面
            finish()
        }
    }

    override fun initImmersionbar(immersionBar: ImmersionBar) {
        immersionBar.run {
            hideBar(BarHide.FLAG_HIDE_BAR)
        }
    }
}