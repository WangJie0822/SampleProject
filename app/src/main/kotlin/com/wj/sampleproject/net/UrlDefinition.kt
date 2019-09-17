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

    /** 获取体系分类列表 */
    const val GET_SYSTEM_CATEGORY_LIST = "/tree/json"
    /** 获取导航列表 */
    const val GET_NAVIGATION_LIST = "/navi/json"
}
