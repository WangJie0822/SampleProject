package com.wj.sampleproject.entity

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
class BannerEntity(
        var desc: String? = "",
        var id: String? = "",
        var imagePath: String? = "",
        var isVisible: String? = "",
        var order: String? = "",
        var title: String? = "",
        var type: String? = "",
        var url: String? = ""
)