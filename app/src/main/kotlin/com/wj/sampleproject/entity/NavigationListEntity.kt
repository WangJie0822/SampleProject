package com.wj.sampleproject.entity

/**
 * 导航列表数据实体类
 */
data class NavigationListEntity
/**
 * @param cid 导航 id
 * @param name 分类名
 * @param articles 导航列表
 */
constructor(
        var cid: String? = "",
        var name: String? = "",
        var articles: ArrayList<ArticleEntity>? = arrayListOf()
)