package cn.wj.android.views

import android.content.Context

/**
 * 将 dp 单位的值转换为 px 单位的值
 *
 * @return dp 对应的 px 值
 */
internal fun <N : Number> N.dip2px(context: Context): Float {
    val density = context.resources.displayMetrics.density
    return this.toFloat() * density
}

/**
 * 将 sp 单位的值转换为 px 单位的值
 *
 * @return 对应的 px 值
 */
internal fun <N : Number> N.sp2px(context: Context): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return this.toFloat() * density
}