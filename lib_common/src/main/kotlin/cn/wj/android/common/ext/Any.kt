@file:Suppress("unused")
@file:JvmName("AnyExt")

package cn.wj.android.common.ext

/**
 * 所以数据对象拓展
 *
 * 创建时间：2019/11/25
 *
 * @author 王杰
 */

/** 若对象为`null`，返回[empty]，否则返回对象本身 */
fun <T> T?.orElse(empty: T): T {
    return this ?: empty
}

/** 优化[toString]，对象为`null`时返回`"null"` */
fun Any?.toSafeString(): String {
    return this?.toString() ?: "null"
}