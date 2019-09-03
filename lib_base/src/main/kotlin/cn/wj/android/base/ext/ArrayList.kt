@file:Suppress("unused")

package cn.wj.android.base.ext

import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                        集合相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 若为空 返回空集合
 */
fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> {
    return this ?: arrayListOf()
}