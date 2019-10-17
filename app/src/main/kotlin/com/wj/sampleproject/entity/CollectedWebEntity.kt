package com.wj.sampleproject.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * 收藏网站数据实体类
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */
data class CollectedWebEntity
/**
 * @param desc 描述
 * @param icon 图标
 * @param id 网站id
 * @param link 地址
 * @param name 名称
 * @param order ？
 * @param userId 用户 id
 * @param visible 是否显示
 */
constructor(
        var desc: String? = "",
        var icon: String? = "",
        var id: String? = "",
        var link: String? = "",
        var name: String? = "",
        var order: String? = "",
        var userId: String? = "",
        var visible: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(desc)
        parcel.writeString(icon)
        parcel.writeString(id)
        parcel.writeString(link)
        parcel.writeString(name)
        parcel.writeString(order)
        parcel.writeString(userId)
        parcel.writeString(visible)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CollectedWebEntity> {
        override fun createFromParcel(parcel: Parcel): CollectedWebEntity {
            return CollectedWebEntity(parcel)
        }

        override fun newArray(size: Int): Array<CollectedWebEntity?> {
            return arrayOfNulls(size)
        }
    }
}