package com.wj.android.ui.fragment

import android.os.Bundle
import com.wj.android.ui.viewmodel.BaseLibViewModel

/**
 * Fragment 基类
 * - 添加 MVVM 模式的支持
 * - [viewModel]
 *
 * @param VM MVVM ViewModel 类，继承 [BaseLibViewModel]
 *
 * @author 王杰
 */
abstract class BaseMvvmLibFragment<VM : com.wj.android.ui.viewmodel.BaseLibViewModel>
    : BaseLibFragment() {

    /** 当前界面 ViewModel 对象 */
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化观察者
        initObserve()
    }

    /**
     * 初始化观察者
     */
    protected open fun initObserve() {
    }
}