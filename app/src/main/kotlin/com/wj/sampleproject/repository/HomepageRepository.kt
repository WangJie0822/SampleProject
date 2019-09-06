package com.wj.sampleproject.repository

import cn.wj.android.base.ext.orEmpty
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.constants.STR_TRUE
import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleListEntity
import com.wj.sampleproject.entity.BannerEntity
import com.wj.sampleproject.net.NetResult
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
    suspend fun getHomepageBannerList(): NetResult<ArrayList<BannerEntity>> {
        return withContext(Dispatchers.IO) {
            mWebService.getHomepageBannerList()
        }
    }

    /**
     * 获取首页文章列表
     *
     * @param pageNum 页码
     */
    suspend fun getHompageArticleList(pageNum: Int): NetResult<ArticleListEntity> {
        // 获取文章列表
        val result = withContext(Dispatchers.IO) {
            mWebService.getHomepageArticleList(pageNum)
        }
        if (pageNum == NET_PAGE_START) {
            // 刷新，获取置顶文章列表
            val top = withContext(Dispatchers.IO) {
                mWebService.getHomepageTopArticleList()
            }
            val ls = arrayListOf<ArticleEntity>()
            ls.addAll(top.data.orEmpty())
            ls.forEach {
                it.top = STR_TRUE
            }
            ls.addAll(result.data?.datas.orEmpty())
            result.data?.datas = ls
        }
        return result
    }
}