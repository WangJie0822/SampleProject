package cn.wj.android.base.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity 基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - 添加 [initBefore] 方法，在一切流程开始前预处理一些数据
 * - 添加 [startAnim]、[finishAnim] 方法，统一处理界面跳转的动画效果
 *
 * @author 王杰
 */
abstract class BaseLibActivity
    : AppCompatActivity() {

    /** 当前界面 Context 对象*/
    protected lateinit var mContext: AppCompatActivity

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