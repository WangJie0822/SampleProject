@file:Suppress("unused")
@file:JvmName("CollectionExt")

package cn.wj.common.ext

import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                        集合相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/** 集合是否为 `null` 或 无数据 */
fun <T : Collection<*>> T?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

/** 集合是否不为 `null` 且 有数据 */
fun <T : Collection<*>> T?.isNotNullAndEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

/** 将集合[List]中的数据1[fromPosition]数据2[toPosition]交换位置 */
fun List<*>.swap(fromPosition: Int, toPosition: Int) {
    Collections.swap(this, fromPosition, toPosition)
}

/** [ArrayList]对象为 `null` 返回[arrayListOf] */
fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> {
    return this ?: arrayListOf()
}

/** 从[ArrayList]中复制出一个新的[ArrayList]，根据[clear]判断是否清除旧数据，并且添加新数据[list]到列表 */
@JvmOverloads
fun <T> ArrayList<T>?.copy(list: Collection<T>? = listOf(), clear: Boolean = false): ArrayList<T> {
    val newList = arrayListOf<T>()
    if (null != this && this.isNotEmpty() && !clear) {
        newList.addAll(this)
    }
    if (null != list && list.isNotEmpty()) {
        newList.addAll(list)
    }
    return newList
}

/** 从[List]中复制出一个新的[List]，根据[clear]判断是否清除旧数据，并且添加新数据[list]到列表 */
@JvmOverloads
fun <T> List<T>?.copy(list: Collection<T>? = listOf(), clear: Boolean = false): List<T> {
    val newList = mutableListOf<T>()
    if (null != this && this.isNotEmpty() && !clear) {
        newList.addAll(this)
    }
    if (null != list && list.isNotEmpty()) {
        newList.addAll(list)
    }
    return newList
}