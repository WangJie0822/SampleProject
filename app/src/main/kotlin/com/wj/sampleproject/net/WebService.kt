package com.wj.sampleproject.net

import com.wj.sampleproject.entity.ArticleEntity
import com.wj.sampleproject.entity.ArticleListEntity
import com.wj.sampleproject.entity.BannerEntity
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 网络请求接口
 */
interface WebService {

    /**
     * 获取首页 Banner 列表
     */
    @GET(UrlDefinition.GET_HOMEPAGE_BANNER_LIST)
    suspend fun getHomepageBannerList(): NetResult<ArrayList<BannerEntity>>

    /**
     * 获取首页置顶文章列表
     */
    @GET(UrlDefinition.GET_HOMEPAGE_TOP_ARTICLE_LIST)
    suspend fun getHomepageTopArticleList(): NetResult<ArrayList<ArticleEntity>>

    /**
     * 获取首页文章列表
     *
     * @param pageNum 页码
     */
    @GET(UrlDefinition.GET_HOMEPAGE_ARTICLE_LIST)
    suspend fun getHomepageArticleList(@Path("pageNum") pageNum: Int): NetResult<ArticleListEntity>
}