package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 体系相关数据仓库
 */
class SystemRepository(private val mWebService: WebService) {

    /** 获取体系目录列表 */
    suspend fun getSystemCategoryList() = withContext(Dispatchers.IO) {
        mWebService.getSystemCategoryList()
    }

    /** 获取导航列表 */
    suspend fun getNavigationList() = withContext(Dispatchers.IO) {
        mWebService.getNavigationList()
    }

    /** 根据页码[pageNum]、体系目录[cid]获取并返回体系下文章列表 */
    suspend fun getSystemArticleList(pageNum: Int, cid: String) = withContext(Dispatchers.IO) {
        mWebService.getSystemArticleList(pageNum, cid)
    }
}