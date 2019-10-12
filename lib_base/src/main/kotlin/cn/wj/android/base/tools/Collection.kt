@file:Suppress("unused")
@file:JvmName("CollectionTools")

package cn.wj.android.base.tools

import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                        集合相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 交换位置
 */
fun List<*>.swap(fromPosition: Int, toPosition: Int) {
    Collections.swap(this, fromPosition, toPosition)
}