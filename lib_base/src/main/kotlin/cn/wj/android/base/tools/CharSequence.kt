@file:Suppress("unused")

package cn.wj.android.base.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import cn.wj.android.base.utils.AppManager

/* ----------------------------------------------------------------------------------------- */
/* |                                      字符序列相关                                      | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 设置字符串文本不同颜色
 *
 * @param divide 开始标记符号 默认 "+"
 * @param len 不同颜色的长度 默认 2
 * @param color 不同颜色的颜色值 默认红色
 */
fun CharSequence.diff(divide: String = "+", len: Int = 2, color: Int = Color.RED): Spannable {
    val index = this.indexOf(divide)
    val span = SpannableString(this)
    span.setSpan(ForegroundColorSpan(color), index, index + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 设置字符串文本不同颜色
 *
 * @param start 开始下标
 * @param len 不同颜色的长度 默认 2
 * @param color 不同颜色的颜色值 默认红色
 */
fun CharSequence.diff(start: Int, len: Int = 2, color: Int = Color.RED): Spannable {
    val span = SpannableString(this)
    span.setSpan(ForegroundColorSpan(color), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 复制到剪切板
 *
 * @param context Context 对象
 * @param label 标签
 */
fun CharSequence.copyToClipboard(context: Context, label: CharSequence) {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    manager.primaryClip = ClipData.newPlainText(label, this)
}

/**
 * 复制到剪切板
 *
 * @param label 标签
 */
fun CharSequence.copyToClipboard(label: CharSequence) {
    this.copyToClipboard(AppManager.getApplication(), label)
}