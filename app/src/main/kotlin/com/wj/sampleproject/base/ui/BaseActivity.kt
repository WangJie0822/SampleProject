package com.wj.sampleproject.base.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.tools.fixFontScaleResources
import cn.wj.android.swipeback.helper.SwipeBackHelper
import cn.wj.android.swipeback.helper.dispatchTouchEvent
import com.google.android.material.snackbar.Snackbar
import com.wj.android.ui.activity.BaseBindingLibActivity
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel

/**
 * Activity 基类
 * > 指定 ViewModel 类型 [VM] & 指定 DataBinding 类型 [DB]
 *
 * @author 王杰
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibActivity<VM, DB>() {

    /** 侧滑返回帮助类对象 */
    private var mSwipeBackHelper: SwipeBackHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化侧滑返回帮助类
        mSwipeBackHelper = SwipeBackHelper(this)
        // 添加观察者
        observeData()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // 使用侧滑处理
        return mSwipeBackHelper.dispatchTouchEvent(ev) {
            super.dispatchTouchEvent(ev)
        }
    }

    override fun getResources(): Resources? {
        // 修复 app 字体大小跟随系统字体大小调节
        return fixFontScaleResources(super.getResources(), this)
    }

    override fun startAnim() {
        overridePendingTransition(R.anim.app_anim_right_in, R.anim.app_anim_alpha_out)
    }

    override fun finishAnim() {
        overridePendingTransition(R.anim.app_anim_alpha_in, R.anim.app_anim_right_out)
    }

    /** 添加观察者 */
    private fun observeData() {
        // snackbar 提示
        viewModel.snackbarData.observe(this, Observer {
            if (it.content.isNullOrBlank()) {
                return@Observer
            }
            val view = if (it.targetId == 0) {
                mBinding.root
            } else {
                mBinding.root.findViewById(it.targetId)
            }
            val snackBar = Snackbar.make(view, it.content.orEmpty(), it.duration)
            snackBar.setTextColor(it.contentColor)
            snackBar.setBackgroundTint(it.contentBgColor)
            if (it.actionText != null && it.onAction != null) {
                snackBar.setAction(it.actionText, it.onAction)
                snackBar.setActionTextColor(it.actionColor)
            }
            if (it.onCallback != null) {
                snackBar.addCallback(it.onCallback)
            }
            snackBar.show()
        })
        // UI 关闭
        viewModel.uiCloseData.observe(this, { close ->
            close?.let { model ->
                setResult(model.resultCode, model.result)
                finish()
            }
        })
    }
}