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
 *
 * @param fromPosition 位置1
 * @param toPosition 位置2
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
 * 从当前集合中获取一个新的集合对象
 *
 * @return 相同数据的集合对象
 */
fun <T> ArrayList<T>?.newList(): ArrayList<T> {
    val list = arrayListOf<T>()
    if (null != this) {
        list.addAll(this)
    }
    return list
}

/**
 * 将当前 List 转换为新的 List
 *
 * @param list 新的数据
 * @param clean 是否清空旧数据
 */
@JvmOverloads
fun <T> List<T>?.toNewList(list: List<T>? = listOf(), clean: Boolean = false): List<T> {
    val ls = mutableListOf<T>()
    if (!clean) {
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
@JvmOverloads
fun <T> ArrayList<T>?.toNewList(list: List<T>? = listOf(), clean: Boolean = false): ArrayList<T> {
    val ls = arrayListOf<T>()
    if (!clean) {
        ls.addAll(this.orEmpty())
    }
    ls.addAll(list.orEmpty())
    return ls
}