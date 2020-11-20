package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 我的相关数据仓库
 */
class MyRepository : KoinComponent {

    /** 网络请求服务 */
    private val mWebService: WebService by inject()

    /** 通过用户名[username]、密码[password]登录并返回用户信息 */
    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        mWebService.login(username, password)
    }

    /** 通过用户名[username]、密码[password]注册用户并返回用户信息 */
    suspend fun register(username: String, password: String) = withContext(Dispatchers.IO) {
        mWebService.register(username, password)
    }

    /** 用户退出登录 */
    suspend fun logout() = withContext(Dispatchers.IO) {
        mWebService.logout()
    }
}