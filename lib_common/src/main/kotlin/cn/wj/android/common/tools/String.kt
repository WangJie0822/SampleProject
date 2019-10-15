@file:Suppress("unused")
@file:JvmName("StringTools")

package cn.wj.android.common.tools

import android.graphics.Color
import android.text.Html
import android.text.Spanned
import android.util.Base64
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
    @Suppress("DEPRECATION")
    return Html.fromHtml(this)
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

/**
 * 对字符串进行 Base64 加密
 *
 * @return 加密后的字符串
 */
fun String.toBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT).replace("\n", "")
}

/**
 * 从 Base64 字符串解密
 *
 * @return 解密后的字符串
 */
fun String.fromBase64(): String {
    return String(Base64.decode(this, Base64.DEFAULT))
}