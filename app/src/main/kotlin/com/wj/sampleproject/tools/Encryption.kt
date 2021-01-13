@file:Suppress("unused")

package com.wj.sampleproject.tools

import android.util.Base64

/**
 * 加密相关
 *
 * - 创建时间：2021/1/12
 *
 * @author 王杰
 */

fun String.toBase64ByteArray(): ByteArray {
    return Base64.decode(this, Base64.URL_SAFE)
}

fun ByteArray.toBase64String(): String {
    return Base64.encodeToString(this, Base64.URL_SAFE)
}