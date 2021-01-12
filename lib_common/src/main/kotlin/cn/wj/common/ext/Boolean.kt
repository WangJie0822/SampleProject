@file:Suppress("unused")
@file:JvmName("BooleanExt")

package cn.wj.common.ext

/** [Boolean]为`null`则默认`false` */
fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

/** [Boolean]为`null`则默认`true` */
fun Boolean?.orTrue(): Boolean {
    return this ?: true
}

/** 仅为`true`时为`true` */
val Boolean?.condition: Boolean
    get() = this == true