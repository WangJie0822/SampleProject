package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.TAB_SYSTEM_NAVIGATION
import com.wj.sampleproject.constants.TAB_SYSTEM_SYSTEM

/**
 * 体系 ViewModel
 */
class SystemViewModel
    : BaseViewModel() {
    
    /** 标记 - 体系是否选中 */
    val systemSelected: ObservableBoolean = ObservableBoolean(true)
    
    /** ViewPager 下标 */
    val currentItem: ObservableInt = ObservableInt(TAB_SYSTEM_SYSTEM)
    
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