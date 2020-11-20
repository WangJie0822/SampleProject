package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 搜索相关数据仓库
 */
class SearchRepository : KoinComponent {

    /** 网络请求对象 */
    private val mWebService: WebService by inject()

    /** 获取搜索热词 */
    suspend fun getHotSearch() = withContext(Dispatchers.IO) {
        mWebService.getHotSearch()
    }

    /** 根据关键字[keywords]、页码[pageNum]搜索并返回文章列表 */
    suspend fun search(pageNum: Int, keywords: String) = withContext(Dispatchers.IO) {
        mWebService.search(pageNum, keywords)
    }
}