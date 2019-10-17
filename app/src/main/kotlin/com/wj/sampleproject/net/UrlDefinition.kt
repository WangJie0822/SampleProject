package com.wj.sampleproject.net

import com.wj.sampleproject.BuildConfig


/**
 * 网络接口地址
 */
object UrlDefinition {

    /** 正式环境  */
    private const val API_ONLINE = "https://www.wanandroid.com"
    /** 测试环境  */
    private const val API_OFFLINE = "https://www.wanandroid.com"

    /** 服务器url  */
    @Suppress("ConstantConditionIf")
    val BASE_URL = (if (BuildConfig.IS_ONLINE_ENV) API_ONLINE else API_OFFLINE)

    /** 获取首页 Banner 列表 */
    const val GET_HOMEPAGE_BANNER_LIST = "/banner/json"
    /** 获取首页置顶文章列表 */
    const val GET_HOMEPAGE_TOP_ARTICLE_LIST = "/article/top/json"
    /** 获取首页文章列表 */
    const val GET_HOMEPAGE_ARTICLE_LIST = "/article/list/{pageNum}/json"

    /** 获取收藏列表 */
    const val GET_COLLECTION_LIST = "/lg/collect/list/{pageNum}/json"
    /** 收藏站内文章 */
    const val COLLECT_ARTICLE_INSIDE = "/lg/collect/{id}/json"
    /** 收藏站内文章 */
    const val COLLECT_ARTICLE_OUTSIDE = "/lg/collect/add/json"
    /** 取消收藏 - 文章列表 */
    const val UN_COLLECT_ARTICLE_LIST = "/lg/uncollect_originId/{id}/json"
    /** 取消收藏 - 我的收藏 */
    const val UN_COLLECT_ARTICLE_COLLECTED = "/lg/uncollect/{id}/json"
    /** 获取收藏网站列表 */
    const val GET_COLLECTED_WEB_LIST = "/lg/collect/usertools/json"
    /** 删除收藏网站 */
    const val DELETE_COLLECTED_WEB = "/lg/collect/deletetool/json"
    /** 收藏网站 */
    const val COLLECT_WEB = "/lg/collect/addtool/json"
    /** 编辑网站 */
    const val EDIT_COLLECTED_WEB = "/lg/collect/updatetool/json"

    /** 获取体系分类列表 */
    const val GET_SYSTEM_CATEGORY_LIST = "/tree/json"
    /** 获取导航列表 */
    const val GET_NAVIGATION_LIST = "/navi/json"

    /** 获取公众号列表 */
    const val GET_BJNEWS_LIST = "/wxarticle/chapters/json"
    /** 获取公众号文章列表 */
    const val GET_BJNEWS_ARTICLES = "/wxarticle/list/{id}/{pageNum}/json"

    /** 获取项目分类列表 */
    const val GET_PROJECT_CATEGORY = "/project/tree/json"
    /** 获取项目列表 */
    const val GET_PROJECT_LIST = "/project/list/{pageNum}/json"

    /** 登录 */
    const val LOGIN = "/user/login"
    /** 注册 */
    const val REGISTER = "/user/register"
    /** 退出登录 */
    const val LOGOUT = "/user/logout/json"

    /** 获取搜索热词 */
    const val GET_HOT_SEARCH = "/hotkey/json"
    /** 搜索 */
    const val SEARCH = "/article/query/{pageNum}/json"
}
