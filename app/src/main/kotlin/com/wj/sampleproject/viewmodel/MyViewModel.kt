package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.ext.string
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.repository.UserRepository

/**
 * 我的 ViewModel，注入 [repository] 获取数据
 */
class MyViewModel(
        private val repository: UserRepository
) : BaseViewModel() {

    /** 跳转设置 */
    val jumpToSettingsData = MutableLiveData<Int>()

    /** 跳转登录 */
    val jumpToLoginData = MutableLiveData<Int>()

    /** 跳转我的收藏 */
    val jumpToCollectionData = MutableLiveData<Int>()

    /** 跳转收藏网站 */
    val jumpToCollectedWebData = MutableLiveData<Int>()

    /** 跳转学习数据 */
    val jumpToStudyData = MutableLiveData<Int>()

    /** 用户头像地址 */
    val avatarUrl: ObservableField<String> = ObservableField()

    /** 用户名 */
    val userName: ObservableField<String> = ObservableField(R.string.app_un_login.string)

    /** 设置点击 */
    val onSettingsClick: (MenuItem) -> Boolean = {
        if (it.itemId == R.id.menu_setting) {
            if (null == UserInfoData.value) {
                // 未登录，跳转登录
                jumpToLoginData.value = 0
            } else {
                // 跳转设置
                jumpToSettingsData.value = 0
            }
        }
        false
    }

    /** 头部点击 */
    val onTopClick: () -> Unit = {
        if (null == UserInfoData.value) {
            // 未登录，跳转登录
            jumpToLoginData.value = 0
        }
    }

    /** 我的收藏点击 */
    val onMyCollectionClick: () -> Unit = {
        if (null == UserInfoData.value) {
            // 未登录，跳转登录
            jumpToLoginData.value = 0
        } else {
            // 已登录，跳转我的收藏列表
            jumpToCollectionData.value = 0
        }
    }

    /** 收藏网站点击 */
    val onCollectedWebClick: () -> Unit = {
        if (null == UserInfoData.value) {
            // 未登录，跳转登录
            jumpToLoginData.value = 0
        } else {
            // 已登录，跳转收藏网站列表
            jumpToCollectedWebData.value = 0
        }
    }

    /** 学习入口点击 */
    val onStudyClick: () -> Unit = {
        jumpToStudyData.value = 0
    }
}