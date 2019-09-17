package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 公众号相关数据仓库
 */
class BjnewsRepository : KoinComponent {

    /** 网络请求服务 */
    private val mWebService: WebService by inject()

    /**
     * 获取公众号列表
     */
    suspend fun getBjnewsList() = withContext(Dispatchers.IO) {
        mWebService.getBjnewsList()
    }

    /**
     * 获取公众号文章列表
     *
     * @param bjnewsId 公众号 id
     * @param pageNum 页码
     * @param keywords 搜索关键字，为空串返回全部
     */
    suspend fun getBjnewsArticles(bjnewsId: String, pageNum: Int, keywords: String = "") = withContext(Dispatchers.IO) {
        mWebService.getBjnewsArticles(bjnewsId, pageNum, keywords)
    }
}