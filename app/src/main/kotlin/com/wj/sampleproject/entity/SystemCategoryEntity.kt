package com.wj.sampleproject.entity

/**
 * 体系目录数据实体类
 *
 * @param courseId ？
 * @param id 目录 id
 * @param name 目录名称
 * @param order ？
 * @param parentChapterId 父目录 id 0：没有父目录
 * @param visible ？
 * @param children 子目录列表
 */
data class SystemCategoryEntity(
        val courseId: String? = "",
        val id: String? = "",
        val name: String? = "",
        val order: String? = "",
        val parentChapterId: String? = "",
        val visible: String? = "",
        val children: ArrayList<SystemCategoryEntity>? = arrayListOf()
)