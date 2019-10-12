@file:Suppress("unused")
@file:JvmName("CollectionExt")

package cn.wj.android.common.ext

/* ----------------------------------------------------------------------------------------- */
/* |                                        集合相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 是否为 null 或 无数据
 */
fun <T : Collection<*>> T?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

/**
 * 是否不为 null 且 有数据
 */
fun <T : Collection<*>> T?.isNotNullAndEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * 若为空 返回空集合
 */
fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> {
    return this ?: arrayListOf()
}

/**
 * 若为空 返回空集合
 */
fun <T> List<T>?.orEmpty(): List<T> {
    return this ?: mutableListOf()
}