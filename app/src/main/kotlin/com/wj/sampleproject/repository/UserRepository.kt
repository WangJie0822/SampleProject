package com.wj.sampleproject.repository

import com.wj.sampleproject.ext.netRequest
import com.wj.sampleproject.net.WebService

/**
 * 用户相关数据仓库，注入 [webService] 进行网络请求
 */
class UserRepository(private val webService: WebService) {

    /** 通过用户名[username]、密码[password]登录并返回用户信息 */
    suspend fun login(username: String, password: String) = netRequest {
        webService.login(username, password)
    }

    /** 通过用户名[username]、密码[password]注册用户并返回用户信息 */
    suspend fun register(username: String, password: String) = netRequest {
        webService.register(username, password)
    }

    /** 用户退出登录 */
    suspend fun logout() = netRequest {
        webService.logout()
    }
}