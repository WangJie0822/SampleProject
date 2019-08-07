package com.wj.sampleproject.base.ui

import android.view.MotionEvent
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import cn.wj.android.base.expanding.hideSoftKeyboard
import cn.wj.android.base.tools.shouldHideInput
import cn.wj.android.base.ui.activity.BaseBindingLibActivity
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * Activity 基类
 *
 * @author 王杰
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibActivity<VM, DB>() {

    /** 标记 - 触摸输入框以外范围是否隐藏软键盘*/
    protected var touchToHideInput = true

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchToHideInput) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                if (shouldHideInput(v, ev)) {
                    // 需要隐藏软键盘
                    (v as? EditText)?.hideSoftKeyboard()
                }
                return super.dispatchTouchEvent(ev)
            }
            if (window.superDispatchTouchEvent(ev)) {
                return true
            }
            return onTouchEvent(ev)
        } else {
            return super.dispatchTouchEvent(ev)
        }
    }

    override fun startAnim() {
    }

    override fun finishAnim() {
    }
}