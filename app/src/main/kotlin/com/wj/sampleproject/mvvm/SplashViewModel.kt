package com.wj.sampleproject.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.activity.MainActivity
import com.wj.sampleproject.base.mvvm.BaseViewModel
import com.wj.sampleproject.constants.SPLASH_DELAY_MS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 欢迎界面 ViewModel
 */
class SplashViewModel
    : BaseViewModel() {

    override fun onCreate(source: LifecycleOwner) {
        super.onCreate(source)

        viewModelScope.launch {
            // 延时 2000ms
            delay(SPLASH_DELAY_MS)
            // 跳转主界面
            MainActivity.actionStart(AppManager.getContext())
            // 结束当前界面
            uiCloseData.postValue(true)
        }
    }
}