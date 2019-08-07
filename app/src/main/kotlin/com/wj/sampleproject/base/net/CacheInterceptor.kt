package com.wj.sampleproject.base.net

import cn.wj.android.base.tools.isNetworkReachable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 本地缓存拦截器
 *
 * @author 王杰
 */
class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 有网络时 设置缓存超时时间1个小时
        val maxAge = 60 * 60
        // 无网络时，设置超时为1周
        val maxStale = 60 * 60 * 24 * 7
        var request = chain.request()

        request = if (isNetworkReachable()) {
            // 有网络时只从网络获取
            request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
        } else {
            // 无网络时只从缓存中读取
            request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }

        var response = chain.proceed(request)

        response = if (isNetworkReachable()) {
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
        } else {
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
        }

        return response
    }
}