package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.WebViewActivity
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.model.UiCloseModel

/**
 * WebView 界面 ViewModel 类
 */
class WebViewViewModel : BaseViewModel() {

    /** 网页相关数据 */
    val webData = MutableLiveData<WebViewActivity.ActionModel>()

    /** 浏览器打开 */
    val jumpBrowser = MutableLiveData<Int>()

    /** 标题文本 */
    val title: LiveData<String> = webData.map { input ->
        input.title
    }

    /** 菜单 Item 点击 */
    val onMenuItemClick: (MenuItem) -> Boolean = { item ->
        if (item.itemId == R.id.menu_browser) {
            // 浏览器打开
            jumpBrowser.value = 0
        }
        true
    }

    /** 返回点击 */
    val onNavigationClick: () -> Unit = {
        // 返回
        uiCloseData.value = UiCloseModel()
    }
}