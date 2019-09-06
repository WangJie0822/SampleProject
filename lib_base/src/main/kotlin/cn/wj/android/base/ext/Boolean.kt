@file:Suppress("unused")

package cn.wj.android.base.ext

/**
 * 如果为 null 则为 false
 */
fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

/**
 * 如果为 null 则为 true
 */
fun Boolean?.orTrue(): Boolean {
    return this ?: true
}

/**
 * 判断条件节点
 */
val Boolean?.condition: Boolean
    get() = this == true