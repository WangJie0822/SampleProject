package com.wj.sampleproject.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.databinding.BindingField
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * WebView 界面
 */
class WebViewViewModel
    : BaseViewModel() {

    /** 标题文本 */
    val title: BindingField<String> = BindingField()

    /** 返回点击 */
    val navigationData = MutableLiveData<Int>()

    /** 返回点击 */
    val onNavigationClick: () -> Unit = {
        // 返回
        navigationData.postValue(0)
    }
}