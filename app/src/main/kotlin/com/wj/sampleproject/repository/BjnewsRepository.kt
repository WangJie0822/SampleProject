package com.wj.sampleproject.repository

import cn.wj.android.common.ext.orFalse
import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 公众号相关数据仓库
 */
class BjnewsRepository(private val mWebService: WebService) {

    /** 获取公众号列表 */
    suspend fun getBjnewsList() = withContext(Dispatchers.IO) {
        mWebService.getBjnewsList()
    }

    /**
     * 根据公众号id[bjnewsId]、页码[pageNum]、搜索关键字[keywords]获取并返回公众号文章列表
     * > [keywords]为可选参数，默认`""`返回所有数据
     */
    suspend fun getBjnewsArticles(bjnewsId: String, pageNum: Int, keywords: String = "") = withContext(Dispatchers.IO) {
        // 获取文章列表
        val result = mWebService.getBjnewsArticles(bjnewsId, pageNum, keywords)
        // 处理收藏状态
        result.data?.datas?.forEach {
            it.collected.set(it.collect?.toBoolean().orFalse())
        }
        result
    }
}