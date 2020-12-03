package com.wj.sampleproject.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.wj.android.ui.dialog.BaseBindingLibDialog
import com.wj.sampleproject.base.viewmodel.BaseViewModel

/**
 * Dialog 基类
 * > 指定 ViewModel 类型 [VM] & 指定 DataBinding 类型 [DB]
 *
 * @author 王杰
 */
abstract class BaseDialog<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseBindingLibDialog<VM, DB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 添加观察者
        observeData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val createView = super.onCreateView(inflater, container, savedInstanceState)
        initImmersionBar()
        return createView
    }

    /** 初始化状态栏相关 */
    protected open fun initImmersionBar(immersionBar: ImmersionBar) {
    }

    /** 初始化状态栏相关 */
    private fun initImmersionBar() {
        immersionBar {
            // 同步所在 Activity 状态栏
            getTag(mContext.javaClass.simpleName)
            initImmersionBar(this)
        }
    }

    /**
     * 添加观察者
     */
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
            close?.let {
                dismiss()
            }
        })
    }

    /** 使用 [activity] 显示弹窗 */
    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, javaClass.simpleName)
    }

    /** 使用 [fragment] 显示弹窗 */
    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, javaClass.simpleName)
    }
}