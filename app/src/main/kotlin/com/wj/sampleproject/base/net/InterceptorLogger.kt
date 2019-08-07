package com.wj.sampleproject.base.net

import okhttp3.internal.platform.Platform

/**
 * 日志打印接口
 *
 * @author 王杰
 */
interface InterceptorLogger {

    /**
     * 打印日志
     *
     * @param msg 日志文本
     */
    fun log(msg: String)

    companion object {
        /** 默认日志打印 */
        val DEFAULT: InterceptorLogger = object : InterceptorLogger {
            override fun log(msg: String) {
                Platform.get().log(Platform.INFO, msg, null)
            }
        }
    }

}
