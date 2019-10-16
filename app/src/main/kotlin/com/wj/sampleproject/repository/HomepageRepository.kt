package com.wj.sampleproject.repository

import cn.wj.android.common.ext.orEmpty
import cn.wj.android.common.ext.orFalse
import com.wj.sampleproject.constants.NET_PAGE_START
import com.wj.sampleproject.constants.STR_TRUE
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
    suspend fun getHomepageArticleList(pageNum: Int) = withContext(Dispatchers.IO) {
        // 获取文章列表
        val ls = if (pageNum == NET_PAGE_START) {
            // 刷新获取置顶文章列表
            val tops = mWebService.getHomepageTopArticleList().data.orEmpty()
            tops.forEach { it.top = STR_TRUE }
            tops
        } else {
            // 空列表
            arrayListOf()
        }
        // 获取文章列表
        val result = mWebService.getHomepageArticleList(pageNum)
        // 添加文章列表到 ls
        ls.addAll(result.data?.datas.orEmpty())
        // 处理收藏状态
        ls.forEach { it.collected.set(it.collect?.toBoolean().orFalse()) }
        // 处理返回列表
        result.data?.datas = ls
        result
    }
}