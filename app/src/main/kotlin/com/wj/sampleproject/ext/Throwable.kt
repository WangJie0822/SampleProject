package com.wj.sampleproject.ext

import cn.wj.android.base.ext.string
import com.wj.sampleproject.R
import com.wj.sampleproject.model.SnackbarModel
import java.net.ConnectException
import java.net.SocketTimeoutException

/** 异常对应的错误信息 */
val Throwable.showMsg: String
    get() = when (this) {
        is SocketTimeoutException -> R.string.app_net_error_timeout
        is ConnectException -> R.string.app_net_error_connect
        else -> R.string.app_net_error_failed
    }.string

/** 用于提示的错误信息 */
val Throwable.snackbarMsg: SnackbarModel
    get() = showMsg.toSnackbarMsg()