package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.base.ext.string
import com.gyf.immersionbar.ImmersionBar
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.*
import com.wj.sampleproject.databinding.AppActivityMainBinding
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.fragment.*
import com.wj.sampleproject.simple.setFragmentAdapter
import com.wj.sampleproject.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.absoluteValue

/**
 * 应用首页
 */
class MainActivity
    : BaseActivity<MainViewModel, AppActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModel()

    /** 上次返回点击时间 */
    private var lastBackPressMs = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_main)

        // 配置适配器
        mBinding.vpMain.setFragmentAdapter(this) {
            count(5)
            createFragment { position ->
                when (position) {
                    TAB_MAIN_BOTTOM_HOMEPAGE -> HomepageFragment.actionCreate()
                    TAB_MAIN_BOTTOM_SYSTEM -> SystemFragment.actionCreate()
                    TAB_MAIN_BOTTOM_BJNEWS -> BjnewsFragment.actionCreate()
                    TAB_MAIN_BOTTOM_PROJECT -> ProjectFragment.actionCreate()
                    else -> MyFragment.actionCreate()
                }
            }
        }

        // 配置 Snackbar 转换
        snackbarTransform = { snackbarModel ->
            snackbarModel.copy(targetId = R.id.cl_target)
        }
    }

    override fun onBackPressed() {
        // 当前时间
        val currentBackPressMs = System.currentTimeMillis()
        if ((currentBackPressMs - lastBackPressMs).absoluteValue > MAIN_BACK_PRESS_INTERVAL_MS) {
            // 间隔时间外，提示
            viewModel.snackbarData.value = R.string.app_press_again_to_exit.string.toSnackbarModel()
            // 保存时间
            lastBackPressMs = currentBackPressMs
        } else {
            // 间隔时间内，退到后台
            moveTaskToBack(true)
        }
    }

    override fun initImmersionbar(immersionBar: ImmersionBar) {
        immersionBar.run {
            transparentStatusBar()
            fitsSystemWindows(false)
        }
    }

    companion object {

        /** 使用 [context] 打开 [MainActivity] 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<MainActivity>()
        }
    }
}