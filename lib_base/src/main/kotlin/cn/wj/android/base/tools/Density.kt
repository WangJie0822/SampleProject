@file:Suppress("unused")

package cn.wj.android.base.tools

import android.content.Context
import cn.wj.android.base.utils.AppManager

/* ----------------------------------------------------------------------------------------- */
/* |                                        密度相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 将 dp 单位的值转换为 px 单位的值
 *
 * @return dp 对应的 px 值
 */
fun <N : Number> N.dip2px(): Float {
    return this.dip2px(AppManager.getApplication())
}

/**
 * 将 dp 单位的值转换为 px 单位的值
 *
 * @return dp 对应的 px 值
 */
fun <N : Number> N.dip2px(context: Context): Float {
    val density = context.resources.displayMetrics.density
    return this.toFloat() * density
}

/**
 * 将 px 单位的值转换为 dp 单位的值
 *
 * @return px 对应的 dp 值
 */
fun <N : Number> N.px2dip(): Float {
    return this.px2dip(AppManager.getApplication())
}

/**
 * 将 px 单位的值转换为 dp 单位的值
 *
 * @return px 对应的 dp 值
 */
fun <N : Number> N.px2dip(context: Context): Float {
    val density = context.resources.displayMetrics.density
    return this.toFloat() / density
}

/**
 * 将 sp 单位的值转换为 px 单位的值
 *
 * @return 对应的 px 值
 */
fun <N : Number> N.sp2px(): Float {
    return this.sp2px(AppManager.getApplication())
}

/**
 * 将 sp 单位的值转换为 px 单位的值
 *
 * @return 对应的 px 值
 */
fun <N : Number> N.sp2px(context: Context): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return this.toFloat() * density
}

/**
 * 将 px 单位的值转换为 sp 单位的值
 *
 * @return 对应的 sp 值
 */
fun <N : Number> N.px2sp(): Float {
    return this.px2sp(AppManager.getApplication())
}

/**
 * 将 px 单位的值转换为 sp 单位的值
 *
 * @return 对应的 sp 值
 */
fun <N : Number> N.px2sp(context: Context): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return this.toFloat() / density
}