@file:Suppress("unused")
@file:JvmName("NumberExt")

package cn.wj.android.base.ext

import cn.wj.android.base.tools.dip2px
import cn.wj.android.base.tools.getColorById
import cn.wj.android.base.tools.getStringById
import cn.wj.android.base.tools.sp2px
import kotlin.math.roundToInt

/**
 * 资源相关拓展
 *
 * - 创建时间：2019/11/15
 *
 * @author 王杰
 */

/** 单位标记 - DP 单位 */
val Number.dp: Float
    get() = dip2px(this)

/** 单位标记 - SP */
val Number.sp: Float
    get() = sp2px(this)

/** 单位标记 - DP 单位 */
val Number.dpi: Int
    get() = dip2px(this).roundToInt()

/** String 字符串 */
val Int.string: String
    get() = getStringById(this)

/** 颜色值 */
val Int.color: Int
    get() = getColorById(this)