package com.wj.android.ui.dialog

import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.wj.android.base.R

/**
 * Dialog 弹窗基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - [onCreateView] 方法中对 [layoutResId] 对应的 [View] 进行加载，并在 [initView] 中进行初始化操作
 * - 维护了 [mRootView]、[dialogWidth]、[dialogHeight]、[gravity] 等参数，在 [onActivityCreated] 方法中进行配置
 *
 * @author 王杰
 */
abstract class BaseLibDialog
    : DialogFragment() {
    
    /** Context 对象 */
    protected lateinit var mContext: FragmentActivity
    
    /** 根布局对象 */
    protected lateinit var mRootView: View
    
    /** Dialog 宽度 单位：px  */
    open var dialogWidth: Int = WindowManager.LayoutParams.MATCH_PARENT
    
    /** Dialog 高度 单位：px */
    open var dialogHeight: Int = WindowManager.LayoutParams.MATCH_PARENT
    
    /** Dialog 重心 [Gravity] */
    open var gravity: Int = Gravity.CENTER
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 设置样式
        setStyle(STYLE_NO_TITLE, dialogTheme)
        
        // 保存 Context 对象
        mContext = activity as FragmentActivity
    }
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // 取消标题栏
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        
        // 加载布局
        mRootView = inflater.inflate(layoutResId, container, false)
        
        // 初始化布局
        initView()
        
        return mRootView
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        // 配置 Dialog 宽高、重心
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = dialogWidth
        layoutParams?.height = dialogHeight
        layoutParams?.gravity = gravity
        dialog?.window?.attributes = layoutParams
    }
    
    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        dialog?.setCanceledOnTouchOutside(cancelable)
    }
    
    /**
     * 设置动画效果
     *
     * @param resId Style id
     */
    fun setWindowAnimations(@StyleRes resId: Int) {
        dialog?.window?.setWindowAnimations(resId)
    }
    
    /**
     * 设置软键盘弹出效果
     */
    fun setWindowInputMode(mode: Int) {
        dialog?.window?.setSoftInputMode(mode)
    }
    
    /**
     * 显示 Dialog
     *
     * @param activity [FragmentActivity] 对象
     * > Activity 被销毁则不显示
     * @param tag [Fragment] Tag
     */
    fun show(activity: FragmentActivity, tag: String) {
        if (activity.isFinishing || activity.isDestroyed) {
            // Activity 已销毁
            return
        }
        show(activity.supportFragmentManager, tag)
    }
    
    /**
     * 显示 Dialog
     *
     * @param fragment [Fragment] 对象
     * > Fragment 已分离则不显示
     * @param tag [Fragment] Tag
     */
    fun show(fragment: Fragment, tag: String) {
        if (fragment.isDetached) {
            // Fragment 已分离
            return
        }
        show(fragment.childFragmentManager, tag)
    }
    
    protected open val dialogTheme: Int = R.style.BaseDialogTheme
    
    /** 界面布局 id */
    abstract val layoutResId: Int
    
    /**
     * 初始化布局
     */
    abstract fun initView()
}