package com.wj.sampleproject.mvvm

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
    val navigationData = MutableLiveData<Long>()

    /** 返回点击 */
    val onNavigationClick: () -> Unit = {
        // 返回
        navigationData.postValue(System.currentTimeMillis())
    }
}