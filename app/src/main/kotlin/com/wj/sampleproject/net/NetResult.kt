package com.wj.sampleproject.net

import cn.wj.android.base.utils.AppManager
import com.wj.sampleproject.activity.LoginActivity
import com.wj.sampleproject.constants.NET_RESPONSE_CODE_ERROR
import com.wj.sampleproject.constants.NET_RESPONSE_CODE_FAILED
import com.wj.sampleproject.constants.NET_RESPONSE_CODE_LOGIN_FAILED
import com.wj.sampleproject.constants.NET_RESPONSE_CODE_SUCCESS
import com.wj.sampleproject.ext.showMsg
import com.wj.sampleproject.model.SnackbarModel

/**
 * 网络请求返回数据基本框架
 *
 * @param errorCode 返回码
 * @param errorMsg 返回信息
 * @param data 请求返回数据
 */
data class NetResult<T>(
        val errorCode: Int = NET_RESPONSE_CODE_FAILED,
        val errorMsg: String? = "",
        val data: T? = null
) {

    /**
     * 检查返回数据并返回接口是否请求成功
     * > [errorCode] 为 [NET_RESPONSE_CODE_LOGIN_FAILED] 时跳转登录页
     */
    fun success(): Boolean {
        if (errorCode == NET_RESPONSE_CODE_LOGIN_FAILED) {
            // 登录失败，需要重新登录
            LoginActivity.actionStart(AppManager.getContext(), fromNet = true)
        }
        return errorCode == NET_RESPONSE_CODE_SUCCESS
    }

    fun toError(): SnackbarModel {
        return SnackbarModel(errorMsg.orEmpty())
    }

    companion object {

        /** 获取 [throwable] 异常对应的返回数据 */
        fun <E> fromThrowable(throwable: Throwable): NetResult<E> {
            return NetResult(errorCode = NET_RESPONSE_CODE_ERROR, errorMsg = throwable.showMsg)
        }
    }
}