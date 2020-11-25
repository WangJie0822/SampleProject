package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 搜索相关数据仓库
 */
class SearchRepository(private val mWebService: WebService) {

    /** 获取搜索热词 */
    suspend fun getHotSearch() = withContext(Dispatchers.IO) {
        mWebService.getHotSearch()
    }

    /** 根据关键字[keywords]、页码[pageNum]搜索并返回文章列表 */
    suspend fun search(pageNum: Int, keywords: String) = withContext(Dispatchers.IO) {
        mWebService.search(pageNum, keywords)
    }
}