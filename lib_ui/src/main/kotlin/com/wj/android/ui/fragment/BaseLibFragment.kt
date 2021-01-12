package com.wj.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Fragment基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - [onCreateView] 方法中对 [layoutResId] 对应的 [View] 进行加载，并在 [initView] 中进行初始化操作
 * - 维护了 [rootView]等参数
 *
 * @author 王杰
 */
abstract class BaseLibFragment
    : Fragment() {

    /** 当前界面 Context 对象*/
    protected lateinit var mContext: FragmentActivity

    /** 跟布局对象 */
    protected var rootView: View? = null

    /** 页面标题 */
    open val pageTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存当前 Context 对象
        mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(layoutResId, container, false)
            // 初始化布局
            initView()
        } else {
            (rootView?.parent as? ViewGroup?)?.removeView(rootView)
        }

        return rootView
    }

    /** 界面布局 id */
    abstract val layoutResId: Int

    /**
     * 初始化布局
     */
    abstract fun initView()
}