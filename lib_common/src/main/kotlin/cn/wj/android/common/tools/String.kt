@file:Suppress("unused")

package cn.wj.android.common.tools

import android.graphics.Color
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import cn.wj.android.common.log.InternalLog

/* ----------------------------------------------------------------------------------------- */
/* |                                      字符串相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 解析 Html
 *
 * @return 从 Html 解析出的 Spanned 对象
 */
fun String.parseHtml(): Spanned? {
    return HtmlCompat.fromHtml(this, FROM_HTML_MODE_LEGACY)
}

/**
 * 解析 Html
 *
 * @param str Html 字符串
 *
 * @return 从 Html 解析出的 Spanned 对象
 */
fun html(str: String): Spanned? {
    return str.parseHtml()
}

/**
 * 解析颜色
 *
 * @return 颜色值
 */
fun String.parseColor(): Int? {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        InternalLog.e("String.kt", e, "parseColor")
        null
    }
}

/**
 * 解析颜色
 *
 * @param str 颜色字符串
 *
 * @return 颜色值
 */
fun color(str: String): Int? {
    return str.parseColor()
}