package com.wj.sampleproject.viewmodel

import android.view.MenuItem
import androidx.lifecycle.*
import cn.wj.android.base.ext.string
import cn.wj.common.ext.orEmpty
import com.orhanobut.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.entity.CoinEntity
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.repository.UserRepository
import com.wj.sampleproject.tools.toSnackbarModel
import kotlinx.coroutines.launch

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

    /** 积分信息 */
    val coinsData: LiveData<CoinEntity?> = UserInfoData.switchMap {
        getCoinInfo(null != it)
    }

    /** 用户头像地址 */
    val avatarUrl: LiveData<String> = UserInfoData.map {
        it?.icon.orEmpty()
    }

    /** 用户等级 */
    val level: LiveData<String> = coinsData.map {
        it?.level.orEmpty("1")
    }

    /** 用户名 */
    val userName: LiveData<String> = UserInfoData.map {
        it?.username.orEmpty(R.string.app_un_login.string)
    }

    /** 用户积分 */
    val coins: LiveData<String> = coinsData.map {
        it?.coinCount.orEmpty("0")
    }

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

    /** 获取用户积分信息 */
    private fun getCoinInfo(loggedIn: Boolean): LiveData<CoinEntity?> {
        val result = MutableLiveData<CoinEntity?>()
        if (loggedIn) {
            viewModelScope.launch {
                try {
                    repository.coinsInfo()
                            .judge(onSuccess = {
                                // 请求成功
                                result.value = data
                            })
                } catch (throwable: Throwable) {
                    Logger.e(throwable, "getCoinInfo")
                    snackbarData.value = throwable.toSnackbarModel()
                }
            }

        }
        return result
    }
}