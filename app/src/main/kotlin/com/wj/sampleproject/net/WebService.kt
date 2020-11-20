package com.wj.sampleproject.net

import com.wj.sampleproject.entity.*
import retrofit2.http.*

/**
 * 网络请求接口
 */
interface WebService {

    /** 获取并返回首页 Banner 列表 */
    @GET(UrlDefinition.GET_HOMEPAGE_BANNER_LIST)
    suspend fun getHomepageBannerList(): NetResult<ArrayList<BannerEntity>>

    /** 获取并返回首页置顶文章列表 */
    @GET(UrlDefinition.GET_HOMEPAGE_TOP_ARTICLE_LIST)
    suspend fun getHomepageTopArticleList(): NetResult<ArrayList<ArticleEntity>>

    /** 根据页码[pageNum]获取并返回首页文章列表 */
    @GET(UrlDefinition.GET_HOMEPAGE_ARTICLE_LIST)
    suspend fun getHomepageArticleList(@Path("pageNum") pageNum: Int): NetResult<ArticleListEntity>

    /** 根据页码[pageNum]获取并返回收藏列表 */
    @GET(UrlDefinition.GET_COLLECTION_LIST)
    suspend fun getCollectionList(@Path("pageNum") pageNum: Int): NetResult<ArticleListEntity>

    /** 通过文章[id]收藏站内文章 */
    @POST(UrlDefinition.COLLECT_ARTICLE_INSIDE)
    suspend fun collectArticleInside(@Path("id") id: String): NetResult<Any>

    /** 根据文章标题[title]、作者[author]、链接[link]收藏站外文章 */
    @FormUrlEncoded
    @POST(UrlDefinition.COLLECT_ARTICLE_OUTSIDE)
    suspend fun collectArticleOutside(
            @Field("title") title: String,
            @Field("author") author: String,
            @Field("link") link: String
    ): NetResult<Any>

    /** 文章列表中根据文章[id]取消收藏 */
    @POST(UrlDefinition.UN_COLLECT_ARTICLE_LIST)
    suspend fun unCollectArticleList(@Path("id") id: String): NetResult<Any>

    /** 我的收藏列表根据文章[id]、列表下发[originId]取消收藏 */
    @FormUrlEncoded
    @POST(UrlDefinition.UN_COLLECT_ARTICLE_COLLECTED)
    suspend fun unCollectArticleCollected(
            @Path("id") id: String,
            @Field("originId") originId: String
    ): NetResult<Any>

    /** 获取并返回收藏网站列表 */
    @GET(UrlDefinition.GET_COLLECTED_WEB_LIST)
    suspend fun getCollectedWebList(): NetResult<ArrayList<CollectedWebEntity>>

    /** 根据网站[id]删除收藏的网站 */
    @FormUrlEncoded
    @POST(UrlDefinition.DELETE_COLLECTED_WEB)
    suspend fun deleteCollectedWeb(@Field("id") id: String): NetResult<Any>

    /** 根据网站名[name]、链接[link]收藏网站 */
    @FormUrlEncoded
    @POST(UrlDefinition.COLLECT_WEB)
    suspend fun collectWeb(
            @Field("name") name: String,
            @Field("link") link: String
    ): NetResult<Any>

    /** 根据网站[id]修改网站名[name]、链接[link] */
    @FormUrlEncoded
    @POST(UrlDefinition.EDIT_COLLECTED_WEB)
    suspend fun editCollectedWeb(
            @Field("id") id: String,
            @Field("name") name: String,
            @Field("link") link: String
    ): NetResult<Any>

    /** 获取并返回体系分类列表 */
    @GET(UrlDefinition.GET_SYSTEM_CATEGORY_LIST)
    suspend fun getSystemCategoryList(): NetResult<ArrayList<SystemCategoryEntity>>

    /** 获取并返回导航列表 */
    @GET(UrlDefinition.GET_NAVIGATION_LIST)
    suspend fun getNavigationList(): NetResult<ArrayList<NavigationListEntity>>

    /** 根据页码[pageNum]、体系目录[cid]获取并返回体系下文章列表 */
    @GET(UrlDefinition.GET_SYSTEM_ARTICLE_LIST)
    suspend fun getSystemArticleList(
            @Path("pageNum") pageNum: Int,
            @Query("cid") cid: String
    ): NetResult<ArticleListEntity>

    /** 获取并返回公众号列表 */
    @GET(UrlDefinition.GET_BJNEWS_LIST)
    suspend fun getBjnewsList(): NetResult<ArrayList<CategoryEntity>>

    /**
     * 根据公众号id[id]、页码[pageNum]、搜索关键字[keywords]获取并返回公众号文章列表
     * > [keywords]为可选参数，默认`""`返回所有数据
     */
    @GET(UrlDefinition.GET_BJNEWS_ARTICLES)
    suspend fun getBjnewsArticles(
            @Path("id") id: String,
            @Path("pageNum") pageNum: Int,
            @Query("k") keywords: String = ""
    ): NetResult<ArticleListEntity>

    /** 获取并返回项目分类列表 */
    @GET(UrlDefinition.GET_PROJECT_CATEGORY)
    suspend fun getProjectCategory(): NetResult<ArrayList<CategoryEntity>>

    /** 根据分类id[categoryId]、页码[pageNum]获取并返回项目列表 */
    @GET(UrlDefinition.GET_PROJECT_LIST)
    suspend fun getProjectList(
            @Path("pageNum") pageNum: Int,
            @Query("cid") categoryId: String
    ): NetResult<ArticleListEntity>

    /** 通过用户名[username]、密码[password]登录并返回用户信息 */
    @FormUrlEncoded
    @POST(UrlDefinition.LOGIN)
    suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): NetResult<UserInfoEntity>

    /** 通过用户名[username]、密码[password]注册用户并返回用户信息 */
    @FormUrlEncoded
    @POST(UrlDefinition.REGISTER)
    suspend fun register(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("repassword") repassword: String = password
    ): NetResult<UserInfoEntity>

    /** 用户退出登录 */
    @GET(UrlDefinition.LOGOUT)
    suspend fun logout(): NetResult<Any>

    /** 获取搜索热词并返回 */
    @GET(UrlDefinition.GET_HOT_SEARCH)
    suspend fun getHotSearch(): NetResult<ArrayList<HotSearchEntity>>

    /** 根据关键字[keywords]、页码[pageNum]搜索并返回文章列表 */
    @FormUrlEncoded
    @POST(UrlDefinition.SEARCH)
    suspend fun search(
            @Path("pageNum") pageNum: Int,
            @Field("k") keywords: String
    ): NetResult<ArticleListEntity>
}