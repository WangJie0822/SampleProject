package com.wj.sampleproject.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * 公众号、项目分类数据实体类
 */
data class CategoryEntity
/**
 * @param id 分类 id
 * @param name 分类名称
 */
constructor(
        var id: String? = "",
        var name: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryEntity> {
        override fun createFromParcel(parcel: Parcel): CategoryEntity {
            return CategoryEntity(parcel)
        }

        override fun newArray(size: Int): Array<CategoryEntity?> {
            return arrayOfNulls(size)
        }
    }
}
