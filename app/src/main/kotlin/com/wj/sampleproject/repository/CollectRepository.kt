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

    /**
     * 收藏文章 - 站内
     *
     * @param id 文章 id
     */
    suspend fun collectArticleInside(id: String) = withContext(Dispatchers.IO) {
        mWebService.collectArticleInside(id)
    }

    /**
     * 收藏文章 - 站外
     *
     * @param title 标题
     * @param author 作者
     * @param link 链接
     */
    suspend fun collectArticleOutside(title: String, author: String, link: String) = withContext(Dispatchers.IO) {
        mWebService.collectArticleOutside(title, author, link)
    }

    /**
     * 取消收藏 - 文章列表
     *
     * @param id 文章 id
     */
    suspend fun unCollectArticleList(id: String) = withContext(Dispatchers.IO) {
        mWebService.unCollectArticleList(id)
    }

    /**
     * 取消收藏 - 我的收藏列表
     *
     * @param id 文章 id
     * @param originId 我的收藏列表下发 id
     */
    suspend fun unCollectArticleCollected(id: String, originId: String) = withContext(Dispatchers.IO) {
        val oId = if (originId.isBlank()) {
            "-1"
        } else {
            originId
        }
        mWebService.unCollectArticleCollected(id, oId)
    }

    /**
     * 获取收藏列表
     *
     * @param pageNum 页码
     */
    suspend fun getCollectionList(pageNum: Int) = withContext(Dispatchers.IO) {
        // 获取收藏列表
        val result = mWebService.getCollectionList(pageNum)
        // 处理收藏状态
        result.data?.datas?.forEach { it.collected.set(true) }
        result
    }
}