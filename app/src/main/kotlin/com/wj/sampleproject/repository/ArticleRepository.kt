package com.wj.sampleproject.repository

import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.orFalse
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.constants.STR_TRUE
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.ext.netRequest
import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.async

/**
 * 文章相关数据仓库，注入 [mWebService] 用于网络请求
 *
 * - 创建时间：2020/12/29
 *
 * @author 王杰
 */
class ArticleRepository(private val mWebService: WebService) {

    /** 获取首页 Banner 列表 */
    suspend fun getHomepageBannerList() = netRequest {
        mWebService.getHomepageBannerList()
    }

    /**
     * 根据页码[pageNum]获取首页文章列表
     * > [pageNum] 为 [NET_PAGE_START] 时，将同时获取置顶数据合并返回
     */
    suspend fun getHomepageArticleList(pageNum: Int) = netRequest {
        // 获取文章列表
        val ls = arrayListOf<ArticleEntity>()
        if (pageNum == NET_PAGE_START) {
            // 刷新获取置顶文章列表
            val tops = async {
                mWebService.getHomepageTopArticleList().data.orEmpty()
            }
            tops.await().forEach {
                ls.add(it.copy(top = STR_TRUE))
            }
        }
        // 获取文章列表
        val resultAsync = async {
            mWebService.getHomepageArticleList(pageNum)
        }
        val result = resultAsync.await()
        // 添加文章列表到 ls
        ls.addAll(result.data?.datas.orEmpty())
        // 处理收藏状态
        ls.forEach {
            it.collected.set(it.collect?.toBoolean().orFalse())
        }
        // 处理返回列表
        result.copy(data = result.data?.copy(datas = ls))
    }

    /** 通过文章[id]收藏站内文章 */
    suspend fun collectArticleInside(id: String) = netRequest {
        mWebService.collectArticleInside(id)
    }

    /** 根据文章标题[title]、作者[author]、链接[link]收藏站外文章 */
    suspend fun collectArticleOutside(title: String, author: String, link: String) = netRequest {
        mWebService.collectArticleOutside(title, author, link)
    }

    /** 文章列表中根据文章[id]取消收藏 */
    suspend fun unCollectArticleList(id: String) = netRequest {
        mWebService.unCollectArticleList(id)
    }

    /** 我的收藏列表根据文章[id]、列表下发[originId]取消收藏 */
    suspend fun unCollectArticleCollected(id: String, originId: String) = netRequest {
        val oId = if (originId.isBlank()) {
            "-1"
        } else {
            originId
        }
        mWebService.unCollectArticleCollected(id, oId)
    }

    /** 根据页码[pageNum]获取并返回收藏列表 */
    suspend fun getCollectionList(pageNum: Int) = netRequest {
        // 获取收藏列表
        val result = mWebService.getCollectionList(pageNum)
        // 处理收藏状态
        result.data?.datas?.forEach { it.collected.set(true) }
        result
    }

    /** 获取收藏网站列表 */
    suspend fun getCollectedWebList() = netRequest {
        mWebService.getCollectedWebList()
    }

    /** 根据网站[id]删除收藏的网站 */
    suspend fun deleteCollectedWeb(id: String) = netRequest {
        mWebService.deleteCollectedWeb(id)
    }

    /** 根据网站名[name]、链接[link]收藏网站 */
    suspend fun collectWeb(name: String, link: String) = netRequest {
        mWebService.collectWeb(name, link)
    }

    /** 根据网站[id]修改网站名[name]、链接[link] */
    suspend fun editCollectedWeb(id: String, name: String, link: String) = netRequest {
        mWebService.editCollectedWeb(id, name, link)
    }

    /** 获取公众号列表 */
    suspend fun getBjnewsList() = netRequest {
        mWebService.getBjnewsList()
    }

    /**
     * 根据公众号id[bjnewsId]、页码[pageNum]、搜索关键字[keywords]获取并返回公众号文章列表
     * > [keywords]为可选参数，默认`""`返回所有数据
     */
    suspend fun getBjnewsArticles(bjnewsId: String, pageNum: Int, keywords: String = "") = netRequest {
        // 获取文章列表
        val result = mWebService.getBjnewsArticles(bjnewsId, pageNum, keywords)
        // 处理收藏状态
        result.data?.datas?.forEach {
            it.collected.set(it.collect?.toBoolean().orFalse())
        }
        result
    }

    /** 获取新项目分类列表 */
    suspend fun getProjectCategory() = netRequest {
        mWebService.getProjectCategory()
    }

    /** 根据分类id[categoryId]、页码[pageNum]获取并返回项目列表 */
    suspend fun getProjectList(categoryId: String, pageNum: Int) = netRequest {
        // 获取项目列表
        val result = mWebService.getProjectList(pageNum, categoryId)
        // 处理收藏状态
        result.data?.datas?.forEach { it.collected.set(it.collect?.toBoolean().orFalse()) }
        result
    }

    /** 获取搜索热词 */
    suspend fun getHotSearch() = netRequest {
        mWebService.getHotSearch()
    }

    /** 根据关键字[keywords]、页码[pageNum]搜索并返回文章列表 */
    suspend fun search(pageNum: Int, keywords: String) = netRequest {
        mWebService.search(pageNum, keywords)
    }

    /** 获取体系目录列表 */
    suspend fun getSystemCategoryList() = netRequest {
        mWebService.getSystemCategoryList()
    }

    /** 获取导航列表 */
    suspend fun getNavigationList() = netRequest {
        mWebService.getNavigationList()
    }

    /** 根据页码[pageNum]、体系目录[cid]获取并返回体系下文章列表 */
    suspend fun getSystemArticleList(pageNum: Int, cid: String) = netRequest {
        mWebService.getSystemArticleList(pageNum, cid)
    }
}