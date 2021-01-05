package com.wj.sampleproject.entity

import kotlinx.serialization.Serializable

/**
 * 导航列表数据实体类
 *
 * @param cid 导航 id
 * @param name 分类名
 * @param articles 导航列表
 */
@Serializable
data class NavigationListEntity(
        val cid: String? = "",
        val name: String? = "",
        val articles: ArrayList<ArticleEntity>? = arrayListOf()
)