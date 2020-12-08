package com.wj.sampleproject.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 公众号、项目分类数据实体类
 *
 * @param id 分类 id
 * @param name 分类名称
 */
@Parcelize
data class CategoryEntity(
        val id: String? = "",
        val name: String? = ""
) : Parcelable
