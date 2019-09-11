package com.wj.sampleproject.mvvm

import cn.wj.android.base.databinding.BindingField
import com.wj.sampleproject.base.mvvm.BaseViewModel

/**
 * H5 界面
 */
class WebViewViewModel
    : BaseViewModel() {

    /** 标题文本 */
    val title: BindingField<String> = BindingField()
}