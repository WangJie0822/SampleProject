package com.wj.android.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cn.wj.android.base.BR
import com.wj.android.ui.viewmodel.BaseLibViewModel

/**
 * Dialog 弹窗基类
 * - 添加了对 DataBinding 的支持
 * - [onCreateView] 方法中处理了对 [mBinding] 的初始化
 *
 * @param VM MVVM ViewModel 类，继承 [BaseLibViewModel]
 * @param DB [ViewDataBinding] 对象
 *
 * @author 王杰
 */
abstract class BaseBindingLibDialog<VM : BaseLibViewModel, DB : ViewDataBinding>
    : BaseMvvmLibDialog<VM>() {

    /** DataBinding 对象 */
    protected lateinit var mBinding: DB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // 取消标题栏
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 初始化 DataBinding
        mBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
    
        // 绑定生命周期管理
        mBinding.lifecycleOwner = this

        // 绑定 ViewModel
        mBinding.setVariable(BR.viewModel, viewModel)

        initView()

        // 初始化布局
        mRootView = mBinding.root

        return mRootView
    }
}