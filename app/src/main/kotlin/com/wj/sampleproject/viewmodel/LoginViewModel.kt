package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.ext.string
import cn.wj.android.common.ext.condition
import cn.wj.android.common.ext.isNotNullAndBlank
import cn.wj.android.common.ext.orElse
import cn.wj.android.logger.Logger
import com.tencent.mmkv.MMKV
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.PASSWORD_MIN_LENGTH
import com.wj.sampleproject.constants.SP_KEY_USER_NAME
import com.wj.sampleproject.ext.snackbarMsg
import com.wj.sampleproject.helper.UserHelper
import com.wj.sampleproject.model.ProgressModel
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.MyRepository
import kotlinx.coroutines.launch

/**
 * 登录 ViewModel
 *
 * @param repository 我的相关数据仓库
 *
 * * 创建时间：2019/10/14
 *
 * @author 王杰
 */
class LoginViewModel(
        private val repository: MyRepository
) : BaseViewModel() {

    /** 标记 - 是否是注册 */
    val register: ObservableBoolean = ObservableBoolean(true)

    /** 用户名 */
    val userName: ObservableField<String> = ObservableField(MMKV.defaultMMKV().decodeString(SP_KEY_USER_NAME, ""))

    /** 标记 - 是否显示清空用户名 */
    val showUserNameClear: ObservableBoolean = object : ObservableBoolean(userName) {
        override fun get(): Boolean {
            return userName.get().isNotNullAndBlank()
        }
    }

    /** 是否允许按钮点击 */
    val buttonEnable: ObservableBoolean = ObservableBoolean(false)

    /** 按钮文本 */
    val buttonStr: ObservableField<String> = object : ObservableField<String>(register) {
        override fun get(): String? {
            return if (register.get()) {
                // 注册
                R.string.app_register
            } else {
                // 登录
                R.string.app_login
            }.string
        }
    }

    /** 密码 */
    val password: ObservableField<String> = object : ObservableField<String>("") {
        override fun set(value: String?) {
            super.set(value)
            checkBtnEnable()
        }
    }

    /** 再次输入密码 */
    val passwordAgain: ObservableField<String> = object : ObservableField<String>("") {
        override fun set(value: String?) {
            super.set(value)
            checkBtnEnable()
        }
    }

    /** 关闭点击 */
    val onCloseClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

    /** 注册、登录点击 */
    val onTabClick: (Boolean) -> Unit = { registerClick ->
        register.set(registerClick)
    }

    /** 清空用户名点击 */
    val onUserNameClearClick: () -> Unit = {
        userName.set("")
    }

    /** 按钮点击 */
    val onButtonClick: () -> Unit = fun() {
        if (!buttonEnable.get().condition) {
            // 按钮不允许点击
            return
        }
        if (userName.get().isNullOrBlank()) {
            // 用户名为空
            snackbarData.value = SnackbarModel(R.string.app_please_enter_user_name)
            return
        }
        if (password.get().isNullOrBlank()) {
            // 密码为空
            snackbarData.value = SnackbarModel(R.string.app_please_enter_password)
            return
        }
        if (password.get().orEmpty().length < PASSWORD_MIN_LENGTH) {
            // 密码长度小于最低长度
            snackbarData.value = SnackbarModel(R.string.app_password_length_must_larger_than_six)
            return
        }
        if (register.get().condition && password.get() != passwordAgain.get()) {
            // 注册且密码不匹配
            snackbarData.value = SnackbarModel(R.string.app_re_set_password_not_match)
            return
        }
        if (register.get().condition) {
            // 注册
            register()
        } else {
            // 登录
            login()
        }
    }

    /**
     * 注册
     */
    private fun register() {
        viewModelScope.launch {
            try {
                // 显示进度条弹窗
                progressData.value = ProgressModel(cancelable = false)
                val result = repository.register(userName.get().orEmpty(), password.get().orEmpty(), passwordAgain.get().orEmpty())
                if (result.success()) {
                    // 注册成功，保存用户信息
                    UserHelper.userInfo = result.data
                    // 保存用户账号
                    MMKV.defaultMMKV().encode(SP_KEY_USER_NAME, userName.get().orEmpty())
                    // 关闭当前界面
                    uiCloseData.value = UiCloseModel()
                } else {
                    // 注册失败，提示错误
                    snackbarData.value = SnackbarModel(result.errorMsg)
                }
            } catch (throwable: Throwable) {
                // 打印错误日志
                Logger.t("NET").e(throwable, "register")
                snackbarData.value = throwable.snackbarMsg
            } finally {
                // 隐藏进度条弹窗
                progressData.value = null
            }
        }
    }

    /**
     * 登录
     */
    private fun login() {
        viewModelScope.launch {
            try {
                // 显示进度条弹窗
                progressData.value = ProgressModel(cancelable = false)
                val result = repository.login(userName.get().orEmpty(), password.get().orEmpty())
                if (result.success()) {
                    // 登录成功，保存用户信息
                    UserHelper.userInfo = result.data
                    // 保存用户账号
                    MMKV.defaultMMKV().encode(SP_KEY_USER_NAME, userName.get().orEmpty())
                    // 关闭当前界面
                    uiCloseData.value = UiCloseModel()
                } else {
                    // 登录失败，提示错误
                    snackbarData.value = SnackbarModel(result.errorMsg)
                }
            } catch (throwable: Throwable) {
                // 打印错误日志
                Logger.t("NET").e(throwable, "login")
                snackbarData.value = throwable.snackbarMsg
            } finally {
                // 隐藏进度条弹窗
                progressData.value = null
                // 重置按钮状态
                checkBtnEnable()
            }
        }
    }

    /**
     * 检查并设置按钮是否允许点击
     */
    private fun checkBtnEnable() {
        buttonEnable.set(if (register.get().condition) {
            // 注册，两次输入密码长度一致且长度大于最小密码长度
            password.get()?.length == passwordAgain.get()?.length
                    && passwordAgain.get()?.length.orElse(0) >= PASSWORD_MIN_LENGTH
        } else {
            // 登录，密码长度大于最小密码长度
            password.get()?.length.orElse(0) >= PASSWORD_MIN_LENGTH
        })
    }
}