package com.wj.sampleproject.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 收藏网站数据实体类
 *
 * @param desc 描述
 * @param icon 图标
 * @param id 网站id
 * @param link 地址
 * @param name 名称
 * @param order ？
 * @param userId 用户 id
 * @param visible 是否显示
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
@Parcelize
data class CollectedWebEntity(
        val desc: String? = "",
        val icon: String? = "",
        val id: String? = "",
        val link: String? = "",
        val name: String? = "",
        val order: String? = "",
        val userId: String? = "",
        val visible: String? = ""
) : Parcelable