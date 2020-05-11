@file:Suppress("unused")
@file:JvmName("DensityTools")

package cn.wj.android.base.tools

import android.content.Context
import cn.wj.android.base.utils.AppManager
import kotlin.math.roundToInt

/* ----------------------------------------------------------------------------------------- */
/* |                                        密度相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 获取设备屏幕宽度
 *
 * @param context [Context] 对象
 *
 * @return 屏幕宽度，单位：px
 */
@JvmOverloads
fun getDeviceScreenWidth(context: Context = AppManager.getContext()): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获取设备屏幕高度
 *
 * @param context [Context] 对象
 *
 * @return 屏幕高度，单位：px
 */
@JvmOverloads
fun getDeviceScreenHeight(context: Context = AppManager.getContext()): Int {
    return context.resources.displayMetrics.heightPixels
}

/**
 * 将 dp 单位的值转换为 px 单位的值
 *
 * @param dpValue dp 值
 * @param context [Context] 对象
 *
 * @return dp 对应的 px 值
 */
@JvmOverloads
fun <N : Number> dip2px(dpValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.density
    return dpValue.toFloat() * density
}

/**
 * 将 px 单位的值转换为 dp 单位的值
 *
 * @param pxValue px 值
 * @param context [Context] 对象
 *
 * @return px 对应的 dp 值
 */
@JvmOverloads
fun <N : Number> px2dp(pxValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.density
    return pxValue.toFloat() / density
}

/**
 * 将 px 单位的值转换为 dp 单位的值
 *
 * @param pxValue px 值
 * @param context [Context] 对象
 *
 * @return px 对应的 dp 值
 */
@JvmOverloads
fun <N : Number> px2dpi(pxValue: N, context: Context = AppManager.getContext()): Int {
    return px2dp(pxValue, context).roundToInt()
}

/**
 * 将 sp 单位的值转换为 px 单位的值
 *
 * @param spValue sp 值
 * @param context [Context] 对象
 *
 * @return 对应的 px 值
 */
@JvmOverloads
fun <N : Number> sp2px(spValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return spValue.toFloat() * density
}

/**
 * 将 px 单位的值转换为 sp 单位的值
 *
 * @param pxValue px 值
 * @param context [Context] 对象
 *
 * @return 对应的 sp 值
 */
@JvmOverloads
fun <N : Number> px2sp(pxValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return pxValue.toFloat() / density
}
