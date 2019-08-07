package com.wj.sampleproject.base.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 公共参数添加拦截器
 *
 * @param staticParams 静态 Param 参数 [Map] 集合
 * @param dynamicParams 动态 Param 参数 [Map] 集合
 * @param staticHeaders 静态 Header 参数 [Map] 集合
 * @param dynamicHeaders 动态 Header 参数 [Map] 集合
 * @param logger 日志打印接口 [InterceptorLogger], 默认实现 [InterceptorLogger.DEFAULT]
 *
 * @author 王杰
 */
class ParamsInterceptor(
        private val staticParams: Map<String, String> = mapOf(),
        private val dynamicParams: Map<String, () -> String> = mapOf(),
        private val staticHeaders: Map<String, String> = mapOf(),
        private val dynamicHeaders: Map<String, () -> String> = mapOf(),
        private val logger: InterceptorLogger = InterceptorLogger.DEFAULT
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // 日志文本
        val logStr = StringBuilder("---------->> Intercept to add parameters <<---------- start\n")

        // 获取请求信息
        val oldRequest = chain.request()

        // 从旧的请求中获取建造者
        val builder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())

        if (staticParams.isNotEmpty()) {
            // 添加固定参数
            for ((key, value) in staticParams) {
                builder.addQueryParameter(key, value)
                logStr.append("static params ---------->> $key ---------->> $value \n")
            }
        }

        if (dynamicParams.isNotEmpty()) {
            // 添加动态参数
            for ((key, value) in dynamicParams) {
                val dynamicParam = value.invoke()
                builder.addQueryParameter(key, dynamicParam)
                logStr.append("dynamic params ---------->> $key ---------->> $dynamicParam \n")
            }
        }

        // 生成新的 Url
        val url = builder.build()

        // 生成新的请求
        val requestBuilder = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(url)

        if (staticHeaders.isNotEmpty()) {
            // 添加固定头
            for ((key, value) in staticHeaders) {
                requestBuilder.addHeader(key, value)
                logStr.append("static headers ---------->> $key ---------->> $value \n")
            }
        }

        if (dynamicHeaders.isNotEmpty()) {
            // 添加动态头
            for ((key, value) in dynamicHeaders) {
                val dynamicHeader = value.invoke()
                requestBuilder.addHeader(key, dynamicHeader)
                logStr.append("dynamic params ---------->> $key ---------->> $dynamicHeader \n")
            }
        }

        logStr.append("---------->> Intercept to add parameters <<---------- end")

        // 打印日志
        logger.log(logStr.toString())

        return chain.proceed(requestBuilder.build())
    }
}