package com.wj.sampleproject.net

import com.wj.sampleproject.BuildConfig


/**
 * 网络接口地址
 */
object UrlDefinition {

    /** 正式环境  */
    private const val API_ONLINE = "http://kyapp.log56.com"
    /** 测试环境  */
    private const val API_OFFLINE = "http://kyapptest.log56.com"

    /** 服务器url  */
    @Suppress("ConstantConditionIf")
    val BASE_URL = (if (!BuildConfig.DEBUG) API_ONLINE else API_OFFLINE)

}
