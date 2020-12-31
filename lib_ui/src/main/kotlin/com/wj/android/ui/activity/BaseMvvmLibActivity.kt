package com.wj.android.ui.activity

import android.os.Bundle
import com.wj.android.ui.viewmodel.BaseLibViewModel

/**
 * Activity 基类
 * - 添加 MVVM 模式的支持
 * - [viewModel]
 *
 * @param VM MVVM ViewModel 类，继承 [BaseLibViewModel]
 *
 * @author 王杰
 */
abstract class BaseMvvmLibActivity<VM : BaseLibViewModel>
    : BaseLibActivity() {

    /** 当前界面 ViewModel 对象 */
    abstract val viewModel: VM

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