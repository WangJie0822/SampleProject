package com.wj.sampleproject.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * 公众号数据实体类
 */
data class BjnewsEntity
/**
 * @param id 公众号 id
 * @param name 公众号名称
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

    companion object CREATOR : Parcelable.Creator<BjnewsEntity> {
        override fun createFromParcel(parcel: Parcel): BjnewsEntity {
            return BjnewsEntity(parcel)
        }

        override fun newArray(size: Int): Array<BjnewsEntity?> {
            return arrayOfNulls(size)
        }
    }
}
