@file:Suppress("unused")
@file:JvmName("NumberExt")

package cn.wj.android.base.ext

import cn.wj.android.base.tools.*

/**
 * 资源相关拓展
 *
 * - 创建时间：2019/11/15
 *
 * @author 王杰
 */

/** 单位标记 - DP 单位 */
val Number.dp: Float
    get() = px2dp(this)

/** 单位标记 - SP */
val Number.sp: Float
    get() = px2sp(this)

/** 单位标记 - DP 单位 */
val Number.dpi: Int
    get() = px2dpi(this)

/** String 字符串 */
val Int.string: String
    get() = getStringById(this)

/** 颜色值 */
val Int.color: Int
    get() = getColorById(this)