package com.wj.sampleproject.entity

import kotlinx.serialization.Serializable

/**
 * 首页 Banner 数据实体类
 *
 * @param desc 描述
 * @param id Banner id
 * @param imagePath 图片地址
 * @param isVisible ？
 * @param order ？
 * @param title 标题
 * @param type ？
 * @param url 跳转链接
 */
@Serializable
data class BannerEntity(
        val desc: String? = "",
        val id: String? = "",
        val imagePath: String? = "",
        val isVisible: String? = "",
        val order: String? = "",
        val title: String? = "",
        val type: String? = "",
        val url: String? = ""
)