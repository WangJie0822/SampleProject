package com.wj.sampleproject.mvvm

import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.tools.getString
import com.wj.sampleproject.R
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.ext.toSnackbarMsg

/**
 * 我的 ViewModel
 */
class MyViewModel
    : BaseViewModel() {

    /** 用户头像地址 */
    val avatarUrl: BindingField<String> = BindingField()

    /** 用户名 */
    val userName: BindingField<String> = BindingField(R.string.app_un_login.getString())

    /** 头部点击 */
    val onTopClick: () -> Unit = {
        snackbarData.postValue("去登陆".toSnackbarMsg())
    }

    /** 我的收藏点击 */
    val onMyCollectionClick: () -> Unit = {
        snackbarData.postValue("我的收藏".toSnackbarMsg())
    }
}