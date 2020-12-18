package com.wj.android.ui.activity

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cn.wj.android.base.ext.hideSoftKeyboard
import cn.wj.android.base.tools.shouldHideInput

/**
 * Activity 基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - 维护 [touchToHideInput]，是否在点击 [EditText] 以外的地方隐藏软键盘
 *
 * @author 王杰
 */
abstract class BaseLibActivity
    : AppCompatActivity() {

    /** 当前界面 Context 对象*/
    protected lateinit var mContext: AppCompatActivity

    /** 标记 - 触摸输入框以外范围是否隐藏软键盘*/
    protected var touchToHideInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存当前 Context 对象
        mContext = this
    }

    override fun onPause() {
        super.onPause()
        // 移除当前获取焦点控件的焦点，防止下个界面软键盘顶起布局
        currentFocus?.clearFocus()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchToHideInput) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (shouldHideInput(currentFocus, ev)) {
                    // 需要隐藏软键盘
                    currentFocus?.hideSoftKeyboard()
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
}