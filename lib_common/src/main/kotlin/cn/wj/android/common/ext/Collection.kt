@file:Suppress("unused")
@file:JvmName("CollectionExt")

package cn.wj.android.common.ext

import java.util.*

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
 * 交换位置
 */
fun List<*>.swap(fromPosition: Int, toPosition: Int) {
    Collections.swap(this, fromPosition, toPosition)
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

/**
 * 将当前 List 转换为新的 List
 *
 * @param list 新的数据
 * @param clean 是否清空旧数据
 */
fun <T> List<T>?.toNewList(list: List<T>?, clean: Boolean? = false): List<T> {
    val ls = mutableListOf<T>()
    if (!clean.condition) {
        ls.addAll(this.orEmpty())
    }
    ls.addAll(list.orEmpty())
    return ls
}

/**
 * 将当前 List 转换为新的 List
 *
 * @param list 新的数据
 * @param clean 是否清空旧数据
 */
fun <T> ArrayList<T>?.toNewList(list: List<T>?, clean: Boolean? = false): ArrayList<T> {
    val ls = arrayListOf<T>()
    if (!clean.condition) {
        ls.addAll(this.orEmpty())
    }
    ls.addAll(list.orEmpty())
    return ls
}