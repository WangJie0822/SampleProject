package com.wj.sampleproject.repository

import cn.wj.android.base.ext.orEmpty
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.constants.STR_TRUE
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 首页相关数据仓库
 */
class HomepageRepository : KoinComponent {

    /** 网络请求对象 */
    private val mWebService: WebService by inject()

    /**
     * 获取首页 Banner 列表
     */
    suspend fun getHomepageBannerList() = withContext(Dispatchers.IO) {
        mWebService.getHomepageBannerList()
    }

    /**
     * 获取首页文章列表
     *
     * @param pageNum 页码
     */
    suspend fun getHompageArticleList(pageNum: Int) = withContext(Dispatchers.IO) {
        // 获取文章列表
        val result = mWebService.getHomepageArticleList(pageNum)
        if (pageNum == NET_PAGE_START) {
            // 刷新，获取置顶文章列表
            val top = mWebService.getHomepageTopArticleList()
            val ls = arrayListOf<ArticleEntity>()
            ls.addAll(top.data.orEmpty())
            ls.forEach {
                it.top = STR_TRUE
            }
            ls.addAll(result.data?.datas.orEmpty())
            result.data?.datas = ls
        }
        result
    }
}