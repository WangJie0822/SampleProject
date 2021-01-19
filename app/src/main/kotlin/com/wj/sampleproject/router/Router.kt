package com.wj.sampleproject.router

import com.wj.sampleproject.activity.*

/**
 * 路由相关
 *
 * - 创建时间：2021/1/19
 *
 * @author 王杰
 */

/** 路由组 - 公共 */
const val ROUTER_GROUP_COMMON = "/common"

/** 路由组 - 用户 */
const val ROUTER_GROUP_USER = "/user"

/** 路由组 - 文章 */
const val ROUTER_GROUP_ARTICLE = "/article"

/** 主界面 [MainActivity] */
const val ROUTER_PATH_MAIN = "$ROUTER_GROUP_COMMON/main"

/** 设置 [SettingsActivity] */
const val ROUTER_PATH_SETTING = "$ROUTER_GROUP_COMMON/setting"

/** 学习 [StudyActivity] */
const val ROUTER_PATH_STUDY = "$ROUTER_GROUP_COMMON/study"

/** 网页 [WebViewActivity] */
const val ROUTER_PATH_WEB = "$ROUTER_GROUP_COMMON/web"

/** 登录 [LoginActivity] */
const val ROUTER_PATH_LOGIN = "$ROUTER_GROUP_USER/login"

/** 积分 [CoinActivity] */
const val ROUTER_PATH_COIN = "$ROUTER_GROUP_USER/coin"

/** 收藏的网站 [CollectedWebActivity] */
const val ROUTER_PATH_COLLECTED_WEB = "$ROUTER_GROUP_ARTICLE/collected_web"

/** 收藏 [CollectionActivity] */
const val ROUTER_PATH_COLLECTION = "$ROUTER_GROUP_ARTICLE/collection"

/** 问答 [QuestionAnswerActivity] */
const val ROUTER_PATH_QA = "$ROUTER_GROUP_ARTICLE/qa"

/** 搜索 [SearchActivity] */
const val ROUTER_PATH_SEARCH = "$ROUTER_GROUP_ARTICLE/search"

/** 体系 [SystemArticlesActivity] */
const val ROUTER_PATH_SYSTEM = "$ROUTER_GROUP_ARTICLE/system"
