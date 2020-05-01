@file:Suppress("unused")
@file:JvmName("CharSequenceTools")

package cn.wj.android.base.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import cn.wj.android.base.utils.AppManager
import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                      字符序列相关                                      | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 复制到剪切板
 *
 * @param text 要复制的文本
 * @param context Context 对象
 * @param label 标签
 */
@JvmOverloads
fun copyToClipboard(text: String, context: Context = AppManager.getContext(), label: CharSequence = "Label") {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    manager.setPrimaryClip(ClipData.newPlainText(label, text))
}

/**
 * 将 ByteArray 转换为 16 进制 String
 *
 * @param bytes Byte 数组
 *
 * @return 转换后的 16 进制 String
 */
fun bytesToHexString(bytes: ByteArray?): String {
    val stringBuilder = StringBuilder("")
    if (bytes == null || bytes.isEmpty()) {
        return ""
    }
    for (element in bytes) {
        val v = element.toInt() and 0xFF
        val hv = Integer.toHexString(v).toUpperCase(Locale.getDefault())
        if (hv.length < 2) {
            stringBuilder.append(0)
        }
        stringBuilder.append(hv)
    }
    return stringBuilder.toString()
}

/**
 * 将 16 进制 String 转换为 ByteArray
 *
 * @param str 16 进制 String
 *
 * @return 转换后的 Byte 数组
 */
fun hexStringToBytes(str: String?): ByteArray? {
    if (str == null || str == "") {
        return null
    }
    val result = str.toUpperCase(Locale.getDefault())
    val length = result.length / 2
    val hexChars = result.toCharArray()
    val d = ByteArray(length)
    fun charToByte(c: Char) = "0123456789ABCDEF".indexOf(c).toByte()
    for (i in 0 until length) {
        val pos = i * 2
        d[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
    }
    return d
}