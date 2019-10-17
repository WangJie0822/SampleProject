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
     * 获取收藏列表
     *
     * @param pageNum 页码
     */
    @GET(UrlDefinition.GET_COLLECTION_LIST)
    suspend fun getCollectionList(@Path("pageNum") pageNum: Int): NetResult<ArticleListEntity>

    /**
     * 收藏文章 - 站内
     *
     * @param id 文章 id
     */
    @POST(UrlDefinition.COLLECT_ARTICLE_INSIDE)
    suspend fun collectArticleInside(@Path("id") id: String): NetResult<Any>

    /**
     * 收藏文章 - 站外
     *
     * @param title 标题
     * @param author 作者
     * @param link 链接
     */
    @FormUrlEncoded
    @POST(UrlDefinition.COLLECT_ARTICLE_OUTSIDE)
    suspend fun collectArticleOutside(
            @Field("title") title: String,
            @Field("author") author: String,
            @Field("link") link: String
    ): NetResult<Any>

    /**
     * 取消收藏 - 文章列表
     *
     * @param id 文章 id
     */
    @POST(UrlDefinition.UN_COLLECT_ARTICLE_LIST)
    suspend fun unCollectArticleList(@Path("id") id: String): NetResult<Any>

    /**
     * 取消收藏 - 我的收藏列表
     *
     * @param id 文章 id
     * @param originId 我的收藏列表下发 id
     */
    @FormUrlEncoded
    @POST(UrlDefinition.UN_COLLECT_ARTICLE_COLLECTED)
    suspend fun unCollectArticleCollected(
            @Path("id") id: String,
            @Field("originId") originId: String
    ): NetResult<Any>

    /**
     * 获取收藏网站列表
     */
    @GET(UrlDefinition.GET_COLLECTED_WEB_LIST)
    suspend fun getCollectedWebList(): NetResult<ArrayList<CollectedWebEntity>>

    /**
     * 删除收藏网站
     *
     * @param id 网站 id
     */
    @FormUrlEncoded
    @POST(UrlDefinition.DELETE_COLLECTED_WEB)
    suspend fun deleteCollectedWeb(@Field("id") id: String): NetResult<Any>

    /**
     * 收藏网站
     *
     * @param name 网站名
     * @param link 网站链接
     */
    @FormUrlEncoded
    @POST(UrlDefinition.COLLECT_WEB)
    suspend fun collectWeb(
            @Field("name") name: String,
            @Field("link") link: String
    ): NetResult<Any>

    /**
     * 编辑收藏网站
     *
     * @param id 网站 id
     * @param name 网站名
     * @param link 网站链接
     */
    @FormUrlEncoded
    @POST(UrlDefinition.EDIT_COLLECTED_WEB)
    suspend fun editCollectedWeb(
            @Field("id") id: String,
            @Field("name") name: String,
            @Field("link") link: String
    ): NetResult<Any>

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
     * 获取体系下文章列表
     *
     * @param pageNum 页码
     * @param cid 体系目录 id
     */
    @GET(UrlDefinition.GET_SYSTEM_ARTICLE_LIST)
    suspend fun getSystemArticleList(
            @Path("pageNum") pageNum: Int,
            @Query("cid") cid: String
    ): NetResult<ArticleListEntity>

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

    /**
     * 获取搜索热词
     */
    @GET(UrlDefinition.GET_HOT_SEARCH)
    suspend fun getHotSearch(): NetResult<ArrayList<HotSearchEntity>>

    /**
     * 搜索
     *
     * @param pageNum 页码
     * @param keywords 关键字
     */
    @FormUrlEncoded
    @POST(UrlDefinition.SEARCH)
    suspend fun search(
            @Path("pageNum") pageNum: Int,
            @Field("k") keywords: String
    ): NetResult<ArticleListEntity>
}