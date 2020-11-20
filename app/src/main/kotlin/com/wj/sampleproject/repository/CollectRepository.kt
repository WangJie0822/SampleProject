package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 收藏相关数据仓库
 */
class CollectRepository : KoinComponent {

    /** 网络请求对象 */
    private val mWebService: WebService by inject()

    /** 通过文章[id]收藏站内文章 */
    suspend fun collectArticleInside(id: String) = withContext(Dispatchers.IO) {
        mWebService.collectArticleInside(id)
    }

    /** 根据文章标题[title]、作者[author]、链接[link]收藏站外文章 */
    suspend fun collectArticleOutside(title: String, author: String, link: String) = withContext(Dispatchers.IO) {
        mWebService.collectArticleOutside(title, author, link)
    }

    /** 文章列表中根据文章[id]取消收藏 */
    suspend fun unCollectArticleList(id: String) = withContext(Dispatchers.IO) {
        mWebService.unCollectArticleList(id)
    }

    /** 我的收藏列表根据文章[id]、列表下发[originId]取消收藏 */
    suspend fun unCollectArticleCollected(id: String, originId: String) = withContext(Dispatchers.IO) {
        val oId = if (originId.isBlank()) {
            "-1"
        } else {
            originId
        }
        mWebService.unCollectArticleCollected(id, oId)
    }

    /** 根据页码[pageNum]获取并返回收藏列表 */
    suspend fun getCollectionList(pageNum: Int) = withContext(Dispatchers.IO) {
        // 获取收藏列表
        val result = mWebService.getCollectionList(pageNum)
        // 处理收藏状态
        result.data?.datas?.forEach { it.collected.set(true) }
        result
    }

    /** 获取收藏网站列表 */
    suspend fun getCollectedWebList() = withContext(Dispatchers.IO) {
        mWebService.getCollectedWebList()
    }

    /** 根据网站[id]删除收藏的网站 */
    suspend fun deleteCollectedWeb(id: String) = withContext(Dispatchers.IO) {
        mWebService.deleteCollectedWeb(id)
    }

    /** 根据网站名[name]、链接[link]收藏网站 */
    suspend fun collectWeb(name: String, link: String) = withContext(Dispatchers.IO) {
        mWebService.collectWeb(name, link)
    }

    /** 根据网站[id]修改网站名[name]、链接[link] */
    suspend fun editCollectedWeb(id: String, name: String, link: String) = withContext(Dispatchers.IO) {
        mWebService.editCollectedWeb(id, name, link)
    }
}