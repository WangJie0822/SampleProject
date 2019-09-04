package com.wj.sampleproject.net

import retrofit2.http.GET

/**
 * 网络请求接口
 */
interface WebService {

    @GET("https://www.baidu.com")
    suspend fun login(): String
}