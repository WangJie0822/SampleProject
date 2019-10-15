package com.wj.sampleproject.net

import com.wj.sampleproject.entity.*
import retrofit2.http.*

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

    /**
     * 获取体系分类列表
     */
    @GET(UrlDefinition.GET_SYSTEM_CATEGORY_LIST)
    suspend fun getSystemCategoryList(): NetResult<ArrayList<SystemCategoryEntity>>

    /**
     * 获取导航列表
     */
    @GET(UrlDefinition.GET_NAVIGATION_LIST)
    suspend fun getNavigationList(): NetResult<ArrayList<NavigationListEntity>>

    /**
     * 获取公众号列表
     */
    @GET(UrlDefinition.GET_BJNEWS_LIST)
    suspend fun getBjnewsList(): NetResult<ArrayList<CategoryEntity>>

    /**
     * 获取公众号文章列表
     *
     * @param id 公众号 id
     * @param pageNum 页码
     * @param keywords 搜索关键字，为空串返回全部
     */
    @GET(UrlDefinition.GET_BJNEWS_ARTICLES)
    suspend fun getBjnewsArticles(
            @Path("id") id: String,
            @Path("pageNum") pageNum: Int,
            @Query("k") keywords: String = ""
    ): NetResult<ArticleListEntity>

    /**
     * 获取项目分类列表
     */
    @GET(UrlDefinition.GET_PROJECT_CATEGORY)
    suspend fun getProjectCategory(): NetResult<ArrayList<CategoryEntity>>

    /**
     * 获取项目列表
     *
     * @param pageNum 页码
     * @param categoryId 分类 id
     */
    @GET(UrlDefinition.GET_PROJECT_LIST)
    suspend fun getProjectList(
            @Path("pageNum") pageNum: Int,
            @Query("cid") categoryId: String
    ): NetResult<ArticleListEntity>

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST(UrlDefinition.LOGIN)
    suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): NetResult<UserInfoEntity>

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param repassword 重复密码
     */
    @FormUrlEncoded
    @POST(UrlDefinition.REGISTER)
    suspend fun register(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("repassword") repassword: String
    ): NetResult<UserInfoEntity>

    /**
     * 用户退出登录
     */
    @GET(UrlDefinition.LOGOUT)
    suspend fun logout(): NetResult<Any>
}