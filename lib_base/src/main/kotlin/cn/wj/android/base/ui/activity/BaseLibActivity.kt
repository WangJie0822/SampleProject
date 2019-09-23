package cn.wj.android.base.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cn.wj.android.base.ext.hideSoftKeyboard
import cn.wj.android.base.tools.shouldHideInput
import cn.wj.android.common.Tagable
import java.util.*

/**
 * Activity 基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - 维护 [touchToHideInput]，是否在点击 [EditText] 以外的地方隐藏软键盘
 * - 添加 [initBefore] 方法，在一切流程开始前预处理一些数据
 * - 添加 [startAnim]、[finishAnim] 方法，统一处理界面跳转的动画效果
 *
 * @author 王杰
 */
abstract class BaseLibActivity
    : AppCompatActivity(),
        Tagable {

    override val mTagMaps: HashMap<String, Any> = hashMapOf()

    override var mClosed: Boolean = false

    /** 当前界面 Context 对象*/
    protected lateinit var mContext: AppCompatActivity

    /** 标记 - 触摸输入框以外范围是否隐藏软键盘*/
    protected var touchToHideInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存当前 Context 对象
        mContext = this

        // 初始化数据
        initBefore()
    }

    override fun onPause() {
        super.onPause()
        // 移除当前获取焦点控件的焦点，防止下个界面软键盘顶起布局
        currentFocus?.clearFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTags()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        // 设置动画
        startAnim()
    }

    override fun finish() {
        super.finish()
        // 设置动画
        finishAnim()
    }

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

    /**
     * 开始动画，可按需求重写
     */
    protected open fun startAnim() {
    }

    /**
     * 结束动画，可按需求重写
     */
    protected open fun finishAnim() {
    }

    /**
     * 初始化，最先调用
     */
    protected open fun initBefore() {}
}