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

/** 从字符串[str]中解析[Html]返回[Spanned]对象 */
fun parseHtmlFromString(str: String): Spanned? {
    @Suppress("DEPRECATION")
    return Html.fromHtml(str)
}

/** 从字符串[str]中解析并返回颜色值[Int] */
fun parseColorFromString(str: String): Int? {
    return try {
        Color.parseColor(str)
    } catch (e: Exception) {
        InternalLog.e("String.kt", e, "parseColor")
        null
    }
}