package com.wj.sampleproject.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
        var desc: String? = "",
        var icon: String? = "",
        var id: String? = "",
        var link: String? = "",
        var name: String? = "",
        var order: String? = "",
        var userId: String? = "",
        var visible: String? = ""
) : Parcelable