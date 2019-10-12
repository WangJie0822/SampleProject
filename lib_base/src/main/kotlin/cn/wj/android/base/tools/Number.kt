@file:Suppress("unused")
@file:JvmName("NumberTools")

package cn.wj.android.base.tools

import java.math.BigDecimal
import java.text.DecimalFormat

/* ----------------------------------------------------------------------------------------- */
/* |                                        数字相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 将字符串转换为 Int 类型
 *
 * @return Int 类型数据，若转换失败则返回 0
 */
fun String?.toIntOrZero(): Int {
    return toDoubleOrZero().toInt()
}

/**
 * 将字符串转换为 Long 类型
 *
 * @return Long 类型数据，若转换失败则返回 0L
 */
fun String?.toLongOrZero(): Long {
    return toDoubleOrZero().toLong()
}

/**
 * 将字符串转换为 Float 类型
 *
 * @return Float 类型数据，若转换失败则返回 0.0f
 */
fun String?.toFloatOrZero(): Float {
    return toDoubleOrZero().toFloat()
}

/**
 * 将字符串转换为 Double 类型
 *
 * @return Double 类型数据，若转换失败则返回 0.0
 */
fun String?.toDoubleOrZero(): Double {
    return this?.toDoubleOrNull() ?: 0.0
}

/**
 * 将任意数字类型转换为 Double 类型
 *
 * @return Double 类型数据，若转换失败则返回 0.0
 */
fun <N : Number> N?.toDoubleOrZero(): Double {
    return this?.toDouble() ?: 0.0
}

/**
 * 将字符转换为 BigDecimal 类型
 *
 * @return BigDecimal 对象，转换失败值为 0.0
 */
fun String?.toBigDecimalOrZero(): BigDecimal {
    return BigDecimal(toDoubleOrZero().toString())
}

/**
 * 将数字转换为 BigDecimal 类型
 *
 * @return BigDecimal 对象，转换失败值为 0.0
 */
fun <N : Number> N?.toBigDecimalOrZero(): BigDecimal {
    return BigDecimal(toDoubleOrZero().toString())
}

/**
 * 格式化数字
 *
 * @param format 数字格式 默认 0.00
 *
 * @return 格式化之后的字符串
 */
fun String?.decimalFormat(format: String = "0.00"): String {
    return DecimalFormat(format).format(toDoubleOrZero())
}

/**
 * 格式化数字
 *
 * @param format 数字格式 默认 0.00
 *
 * @return 格式化之后的字符串
 */
fun <N : Number> N?.decimalFormat(format: String = "0.00"): String {
    return DecimalFormat(format).format(toDoubleOrZero())
}