@file:Suppress("unused")
@file:JvmName("AnyExt")

package cn.wj.android.common.ext

/**
 * 所以数据对象拓展
 *
 * <p/>
 * 创建时间：2019/11/25
 *
 * @author 王杰
 */

/**
 * 如果不为空，返回自身，否则返回传递的数据
 *
 * @param empty 数据为空时返回数据
 *
 * @return 非空数据
 */
fun <T> T?.orElse(empty: T): T {
    return this ?: empty
}

/**
 * 优化 toString()
 */
fun Any?.toString(): String {
    return this?.toString() ?: "null"
}