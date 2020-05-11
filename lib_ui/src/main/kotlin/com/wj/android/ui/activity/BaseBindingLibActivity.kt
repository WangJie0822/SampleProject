package com.wj.android.ui.activity

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cn.wj.android.base.BR
import com.wj.android.ui.viewmodel.BaseLibViewModel

/**
 * Activity 基类
 * - 添加了对 DataBinding 的支持
 * - [setContentView] 方法中处理了对 [mBinding] 的初始化
 *
 * @param VM MVVM ViewModel 类，继承 [BaseLibViewModel]
 * @param DB [ViewDataBinding] 对象
 *
 * @author 王杰
 */
abstract class BaseBindingLibActivity<VM : BaseLibViewModel, DB : ViewDataBinding>
    : BaseMvvmLibActivity<VM>() {

    /** DataBinding 对象 */
    protected lateinit var mBinding: DB

    override fun setContentView(layoutResID: Int) {
        // 初始化 DataBinding
        mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layoutResID, null, false
        )

        // 绑定生命周期管理
        mBinding.lifecycleOwner = this

        // 绑定 ViewModel
        mBinding.setVariable(BR.viewModel, viewModel)

        // 设置布局
        super.setContentView(mBinding.root)
    }
}