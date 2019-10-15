package com.wj.sampleproject.viewmodel

import cn.wj.android.base.databinding.BindingField
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.TAB_SYSTEM_NAVIGATION
import com.wj.sampleproject.constants.TAB_SYSTEM_SYSTEM

/**
 * 体系 ViewModel
 */
class SystemViewModel
    : BaseViewModel() {

    /** 标记 - 体系是否选中 */
    val systemSelected: BindingField<Boolean> = BindingField(true)

    /** ViewPager 下标 */
    val currentItem: BindingField<Int> = BindingField(TAB_SYSTEM_SYSTEM)

    /** 体系点击 */
    val onSystemClick: () -> Unit = {
        systemSelected.set(true)
        currentItem.set(TAB_SYSTEM_SYSTEM)
    }

    /** 导航点击 */
    val onNavigationClick: () -> Unit = {
        systemSelected.set(false)
        currentItem.set(TAB_SYSTEM_NAVIGATION)
    }
}