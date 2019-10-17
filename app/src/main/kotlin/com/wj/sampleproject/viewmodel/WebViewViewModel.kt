package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.databinding.BindingField
import com.wj.sampleproject.R
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * WebView 界面
 */
class WebViewViewModel
    : BaseViewModel() {

    /** 返回点击数据 */
    val navigationData = MutableLiveData<Int>()
    /** 浏览器打开 */
    val jumpBorwser = MutableLiveData<Int>()

    /** 标题文本 */
    val title: BindingField<String> = BindingField()

    /** 菜单 Item 点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = { item ->
        if (item.itemId == R.id.menu_browser) {
            // 浏览器打开
            jumpBorwser.postValue(0)
        }
        true
    }

    /** 返回点击 */
    val onNavigationClick: () -> Unit = {
        // 返回
        navigationData.postValue(0)
    }
}