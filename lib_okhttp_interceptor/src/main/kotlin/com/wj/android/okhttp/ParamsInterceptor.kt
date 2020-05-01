@file:Suppress("unused")

package com.wj.android.okhttp

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 公共参数添加拦截器
 *
 * @param staticParams 静态 Param 参数 [Map] 集合
 * @param dynamicParams 动态 Param 参数 [Map] 集合
 * @param staticHeaders 静态 Header 参数 [Map] 集合
 * @param dynamicHeaders 动态 Header 参数 [Map] 集合
 * @param logger 日志打印接口 [InterceptorLogger], 默认实现 [DEFAULT_LOGGER]
 *
 * @author 王杰
 */
class ParamsInterceptor private constructor(
        private val staticParams: Map<String, String> = mapOf(),
        private val dynamicParams: Map<String, DynamicParams> = mapOf(),
        private val staticHeaders: Map<String, String> = mapOf(),
        private val dynamicHeaders: Map<String, DynamicParams> = mapOf(),
        private val logger: InterceptorLogger = DEFAULT_LOGGER
) : Interceptor {

    companion object {

        /**
         * 新建 Builder 对象
         */
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        // 日志文本
        val logStr = StringBuilder("---------->> Intercept to add parameters <<---------- start\n")

        // 获取请求信息
        val oldRequest = chain.request()

        // 从旧的请求中获取建造者
        val builder = oldRequest.url
                .newBuilder()
                .scheme(oldRequest.url.scheme)
                .host(oldRequest.url.host)

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
                .method(oldRequest.method, oldRequest.body)
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
        logger.invoke(logStr.toString())

        return chain.proceed(requestBuilder.build())
    }
    
    //    override fun intercept(chain: Interceptor.Chain): Response {
    //
    //        // 日志文本
    //        val logStr = StringBuilder("---------->> Intercept to add parameters <<---------- start\n")
    //
    //        // 获取请求信息
    //        val oldRequest = chain.request()
    //
    //        // 从旧的请求中获取建造者
    //        val builder = oldRequest.url
    //                .newBuilder()
    //                .scheme(oldRequest.url.scheme)
    //                .host(oldRequest.url.host)
    //
    //        if (staticParams.isNotEmpty()) {
    //            // 添加固定参数
    //            for ((key, value) in staticParams) {
    //                builder.addQueryParameter(key, value)
    //                logStr.append("static params ---------->> $key ---------->> $value \n")
    //            }
    //        }
    //
    //        if (dynamicParams.isNotEmpty()) {
    //            // 添加动态参数
    //            for ((key, value) in dynamicParams) {
    //                val dynamicParam = value.invoke()
    //                builder.addQueryParameter(key, dynamicParam)
    //                logStr.append("dynamic params ---------->> $key ---------->> $dynamicParam \n")
    //            }
    //        }
    //
    //        // 生成新的 Url
    //        val url = builder.build()
    //
    //        // 生成新的请求
    //        val requestBuilder = oldRequest.newBuilder()
    //                .method(oldRequest.method, oldRequest.body)
    //                .url(url)
    //
    //        if (staticHeaders.isNotEmpty()) {
    //            // 添加固定头
    //            for ((key, value) in staticHeaders) {
    //                requestBuilder.addHeader(key, value)
    //                logStr.append("static headers ---------->> $key ---------->> $value \n")
    //            }
    //        }
    //
    //        if (dynamicHeaders.isNotEmpty()) {
    //            // 添加动态头
    //            for ((key, value) in dynamicHeaders) {
    //                val dynamicHeader = value.invoke()
    //                requestBuilder.addHeader(key, dynamicHeader)
    //                logStr.append("dynamic params ---------->> $key ---------->> $dynamicHeader \n")
    //            }
    //        }
    //
    //        logStr.append("---------->> Intercept to add parameters <<---------- end")
    //
    //        // 打印日志
    //        logger.invoke(logStr.toString())
    //
    //        return chain.proceed(requestBuilder.build())
    //    }

    /**
     * 建造者类
     */
    class Builder {
        /** 静态参数 拼接到 url */
        private val staticParams: HashMap<String, String> = hashMapOf()
        /** 动态参数 拼接到 url */
        private val dynamicParams: HashMap<String, DynamicParams> = hashMapOf()
        /** 静态参数 添加到 Headers */
        private val staticHeaders: HashMap<String, String> = hashMapOf()
        /** 动态参数 添加到 Headers */
        private val dynamicHeaders: HashMap<String, DynamicParams> = hashMapOf()
        /** 日志打印接口 */
        private var logger = DEFAULT_LOGGER

        /**
         * 添加静态参数
         *
         * @param key 键
         * @param value 值
         */
        fun addStaticParam(key: String, value: String): Builder {
            staticParams[key] = value
            return this
        }

        /**
         * 添加动态参数
         *
         * @param key 键
         * @param value 值
         */
        fun addDynamicParam(key: String, value: DynamicParams): Builder {
            dynamicParams[key] = value
            return this
        }

        /**
         * 添加静态 Header
         *
         * @param key 键
         * @param value 值
         */
        fun addStaticHeader(key: String, value: String): Builder {
            staticHeaders[key] = value
            return this
        }

        /**
         * 添加动态 Header
         *
         * @param key 键
         * @param value 值
         */
        fun addDynamicHeader(key: String, value: DynamicParams): Builder {
            dynamicHeaders[key] = value
            return this
        }

        /**
         * 设置日志打印接口
         *
         * @param logger 日志打印接口
         */
        fun logger(logger: InterceptorLogger): Builder {
            this.logger = logger
            return this
        }

        /**
         * 构造公告参数拦截器对象
         */
        fun build(): ParamsInterceptor {
            return ParamsInterceptor(
                    staticParams,
                    dynamicParams,
                    staticHeaders,
                    dynamicHeaders,
                    logger
            )
        }
    }
}