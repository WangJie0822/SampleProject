package com.wj.sampleproject.base.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import cn.wj.android.base.tools.fixFontScaleResources
import cn.wj.android.swipeback.helper.SwipeBackHelper
import cn.wj.android.swipeback.helper.dispatchTouchEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.wj.android.ui.activity.BaseBindingLibActivity
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.ACTIVITY_ANIM_DURATION
import com.wj.sampleproject.model.SnackbarModel

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

    /** Snackbar 转换接口 */
    protected var snackbarTransform: ((SnackbarModel) -> SnackbarModel)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate()
        super.onCreate(savedInstanceState)

        // 初始化侧滑返回帮助类
        mSwipeBackHelper = SwipeBackHelper(this)
        // 初始化状态栏工具
        initImmersionbar()
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

    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }

    /** [onCreate] 之前执行，可用于配置动画 */
    protected open fun beforeOnCreate() {
        window.run {
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                duration = ACTIVITY_ANIM_DURATION
            }
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                duration = ACTIVITY_ANIM_DURATION
            }
        }
    }

    /** 初始化状态栏相关配置 */
    protected open fun initImmersionbar(immersionBar: ImmersionBar) {
    }

    /** 初始化状态栏相关配置 */
    private fun initImmersionbar() {
        immersionBar {
            statusBarColor(R.color.app_colorPrimary)
            statusBarDarkFont(false)
            fitsSystemWindows(true)
            initImmersionbar(this)
            addTag(javaClass.simpleName)
        }
    }

    /** 添加观察者 */
    private fun observeData() {
        // snackbar 提示
        viewModel.snackbarData.observe(this, Observer {
            if (it.content.isNullOrBlank()) {
                return@Observer
            }

            // 转换处理
            val model = snackbarTransform?.invoke(it) ?: it

            val view = if (model.targetId == 0) {
                mBinding.root
            } else {
                mBinding.root.findViewById(model.targetId)
            }
            val snackBar = Snackbar.make(view, model.content.orEmpty(), model.duration)
            snackBar.setTextColor(model.contentColor)
            snackBar.setBackgroundTint(model.contentBgColor)
            if (model.actionText != null && model.onAction != null) {
                snackBar.setAction(model.actionText, model.onAction)
                snackBar.setActionTextColor(model.actionColor)
            }
            if (model.onCallback != null) {
                snackBar.addCallback(model.onCallback)
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