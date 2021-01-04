package com.wj.sampleproject.ext

import cn.wj.android.base.ext.string
import cn.wj.android.base.utils.AppManager
import com.google.android.material.snackbar.Snackbar
import com.wj.sampleproject.R
import com.wj.sampleproject.activity.LoginActivity
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.constants.NET_RESPONSE_CODE_LOGIN_FAILED
import com.wj.sampleproject.constants.NET_RESPONSE_CODE_SUCCESS
import com.wj.sampleproject.model.SnackbarModel
import com.wj.sampleproject.net.NetResult

/**
 * NetResult 相关
 *
 * - 创建时间：2021/1/4
 *
 * @author 王杰
 */

/**
 * 对返回数据进行判断，并处理相应逻辑
 * > 成功: [onSuccess] or 失败: [onFailed] or 登录失效: [onFailed4Login]
 */
inline fun <T> NetResult<T>.judge(
        crossinline onSuccess: NetResult<T>.() -> Unit = {},
        crossinline onFailed: NetResult<T>.() -> Unit = {},
        crossinline onFailed4Login: NetResult<T>.() -> Boolean = { false }
) {
    when (errorCode) {
        NET_RESPONSE_CODE_SUCCESS -> {
            // 请求成功
            onSuccess.invoke(this)
        }
        NET_RESPONSE_CODE_LOGIN_FAILED -> {
            // 登录失效，需要重新登录
            if (onFailed4Login.invoke(this)) {
                // 已消费事件
                return
            }
            (AppManager.peekActivity() as? BaseActivity<*, *>)?.run {
                viewModel.snackbarData.value = SnackbarModel(
                        content = errorMsg,
                        duration = Snackbar.LENGTH_INDEFINITE,
                        actionText = R.string.app_go_login.string,
                        onAction = {
                            // 登录失败，需要重新登录
                            LoginActivity.actionStart(this)
                        }
                )
            }
        }
        else -> {
            // 请求失败
            onFailed.invoke(this)
        }
    }
}