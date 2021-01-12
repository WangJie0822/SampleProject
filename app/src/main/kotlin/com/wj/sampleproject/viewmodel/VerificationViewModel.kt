package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.wj.android.base.ext.string
import com.orhanobut.logger.Logger
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.constants.PASSWORD_MIN_LENGTH
import com.wj.sampleproject.constants.USER_INFO_SPLIT
import com.wj.sampleproject.ext.judge
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.model.ProgressModel
import com.wj.sampleproject.model.UiCloseModel
import com.wj.sampleproject.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * 验证弹窗 ViewModel
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
class VerificationViewModel(
        private val repository: UserRepository
) : BaseViewModel() {

    /** 验证成功数据 */
    var verificationInfo = ""

    /** 控制进度条弹窗显示  */
    val progressData = MutableLiveData<ProgressModel>()

    /** 密码 */
    val password: ObservableField<String> = ObservableField<String>()

    /** 密码错误文本 */
    val passwordError: ObservableField<String> = object : ObservableField<String>(password) {
        override fun get(): String? {
            val get = password.get()
            if (!get.isNullOrBlank() && get.length >= PASSWORD_MIN_LENGTH) {
                return ""
            }
            return super.get()
        }
    }

    /** 取消点击 */
    val onCancelClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

    /** 验证点击 */
    val onVerificationClick: () -> Unit = fun() {
        val userName = UserInfoData.value?.username
        if (userName.isNullOrBlank()) {
            // 用户名为空
            snackbarData.value = R.string.app_user_name_must_not_be_empty.string.toSnackbarModel()
            return
        }
        val pwd = password.get().orEmpty()
        if (pwd.isBlank()) {
            // 密码为空
            passwordError.set(R.string.app_password_must_not_be_empty.string)
            return
        }
        if (pwd.length < PASSWORD_MIN_LENGTH) {
            // 密码长度小于最低长度
            passwordError.set(R.string.app_password_length_must_larger_than_six.string)
            return
        }
        // 验证密码
        verification(userName, pwd)
    }

    /** 验证密码 */
    private fun verification(userName: String, password: String) {
        viewModelScope.launch {
            try {
                progressData.value = ProgressModel()
                repository.login(userName, password)
                        .judge(onSuccess = {
                            // 登录成功
                            verificationInfo = "$userName$USER_INFO_SPLIT$password"
                            // 关闭弹窗
                            uiCloseData.value = UiCloseModel()
                        }, onFailed = {
                            snackbarData.value = toSnackbarModel()
                        })
            } catch (throwable: Throwable) {
                Logger.t("NET").e(throwable, "verification")
                snackbarData.value = throwable.toSnackbarModel()
            } finally {
                progressData.value = null
            }
        }
    }
}