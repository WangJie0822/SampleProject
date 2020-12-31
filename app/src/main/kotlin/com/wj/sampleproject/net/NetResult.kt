package com.wj.sampleproject.net

import com.wj.sampleproject.constants.NET_RESPONSE_CODE_FAILED

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
)