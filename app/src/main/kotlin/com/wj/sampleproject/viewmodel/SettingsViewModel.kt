package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.DATA_CACHE_KEY_FINGERPRINT
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.model.ProgressModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.UserRepository
import com.wj.sampleproject.tools.decodeString
import com.wj.sampleproject.tools.encode
import com.wj.sampleproject.tools.safeMMKV
import com.wj.sampleproject.tools.withMMKV
import kotlinx.coroutines.launch

/**
 * 设置界面 ViewModel
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
class SettingsViewModel(
        private val repository: UserRepository
) : BaseViewModel() {

    /** 控制进度条弹窗显示  */
    val progressData = MutableLiveData<ProgressModel>()

    /** 显示退出登录弹窗数据 */
    val showLogoutData = MutableLiveData<Int>()

    /** 显示验证弹窗数据 */
    val showVerificationData = MutableLiveData<Int>()

    /** 标记 - 是否显示指纹识别 */
    val showFingerprint: ObservableBoolean = ObservableBoolean(false)

    /** 标记 - 指纹登录开关 */
    val fingerprintChecked: ObservableBoolean = object : ObservableBoolean(withMMKV(safeMMKV) {
        "$DATA_CACHE_KEY_FINGERPRINT${UserInfoData.value?.username}".decodeString()
    }.isNotBlank()) {
        override fun set(value: Boolean) {
            super.set(value)

            if (value) {
                // 开启指纹登录
                if (!showFingerprint.get()) {
                    // 不支持
                    super.set(!value)
                    return
                }
                // 支持指纹识别，显示验证弹窗
                showVerificationData.value = 0
            } else {
                // 关闭指纹登录，清空指纹登录数据
                withMMKV(safeMMKV) {
                    "$DATA_CACHE_KEY_FINGERPRINT${UserInfoData.value?.username}".encode("")
                }
            }
        }
    }

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

    /** 退出登录点击 */
    val onLogoutClick: () -> Unit = {
        showLogoutData.value = 0
    }

    /** 用户退出登录 */
    fun logout() {
        viewModelScope.launch {
            try {
                // 显示弹窗
                progressData.value = ProgressModel()
                // 请求接口
                repository.logout()
            } catch (throwable: Throwable) {
                // 请求异常
                Logger.t("NET").e(throwable, "logout")
            } finally {
                // 隐藏弹窗
                progressData.value = null
                // 清空用户信息
                UserInfoData.value = null
                // 退出当前界面
                uiCloseData.value = UiCloseModel()
            }
        }
    }
}