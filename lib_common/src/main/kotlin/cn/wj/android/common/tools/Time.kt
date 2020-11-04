@file:Suppress("unused")
@file:JvmName("TimeTools")

package cn.wj.android.common.tools

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                        时间相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/** 默认时间格式化 */
const val DATE_FORMAT_DEFAULT = "yyyy-MM-dd"

/**
 * 格式化日期、时间
 *
 * @param format 日期时间格式
 */
@JvmOverloads
fun <N : Number> N.dateFormat(format: String = DATE_FORMAT_DEFAULT): String {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(this)
    } catch (e: ParseException) {
        ""
    }
}

/**
 * 格式化日期、时间
 *
 * @param format 日期时间格式
 */
@JvmOverloads
fun Date.dateFormat(format: String = DATE_FORMAT_DEFAULT): String {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(this)
    } catch (e: ParseException) {
        ""
    }
}

/**
 * 将时间字符串格式化为指定类型
 */
@JvmOverloads
fun String.paresDate(format: String = DATE_FORMAT_DEFAULT): Date {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this)
    } catch (e: ParseException) {
        Date()
    }
}

/**
 * 将字符串时间转换为 Long 类型时间
 */
@JvmOverloads
fun String?.toLongTime(format: String = DATE_FORMAT_DEFAULT): Long {
    return if (this.isNullOrEmpty()) {
        Date().time
    } else {
        try {
            SimpleDateFormat(format, Locale.getDefault()).parse(this).time
        } catch (e: ParseException) {
            Date().time
        }
    }
}