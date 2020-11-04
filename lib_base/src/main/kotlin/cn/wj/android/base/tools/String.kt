@file:Suppress("unused")
@file:JvmName("StringTools")

package cn.wj.android.base.tools

import android.graphics.Color
import android.text.Html
import android.text.Spanned
import cn.wj.android.base.log.InternalLog

/* ----------------------------------------------------------------------------------------- */
/* |                                      字符串相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 解析 [Html]
 *
 * @param str [Html] 文本
 *
 * @return 从 [Html] 解析出的 [Spanned] 对象
 */
fun parseHtmlFromString(str: String): Spanned? {
    @Suppress("DEPRECATION")
    return Html.fromHtml(str)
}

/**
 * 解析颜色
 *
 * @param str 颜色 文本
 *
 * @return 颜色值
 */
fun parseColorFromString(str: String): Int? {
    return try {
        Color.parseColor(str)
    } catch (e: Exception) {
        InternalLog.e("String.kt", e, "parseColor")
        null
    }
}