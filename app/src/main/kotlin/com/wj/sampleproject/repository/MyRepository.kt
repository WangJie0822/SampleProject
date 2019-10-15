package com.wj.sampleproject.repository

import com.wj.sampleproject.net.UrlDefinition
import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.http.POST

/**
 * 我的相关数据仓库
 */
class MyRepository : KoinComponent {

    /** 网络请求服务 */
    private val mWebService: WebService by inject()

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        mWebService.login(username, password)
    }


    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param repassword 重复密码
     */
    @POST(UrlDefinition.REGISTER)
    suspend fun register(username: String, password: String, repassword: String) = withContext(Dispatchers.IO) {
        mWebService.register(username, password, repassword)
    }

    /**
     * 用户退出登录
     */
    suspend fun logout() = withContext(Dispatchers.IO) {
        mWebService.logout()
    }
}