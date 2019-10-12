@file:JvmName("ThrowableExt")

package cn.wj.android.common.ext

import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

/**
 * 获取堆栈跟踪字符串
 */
fun Throwable?.getStackTraceString(): String {
    if (this == null) {
        return ""
    }
    var t = this
    while (t != null) {
        if (t is UnknownHostException) {
            return ""
        }
        t = t.cause
    }

    val sw = StringWriter()
    val pw = PrintWriter(sw)
    this.printStackTrace(pw)
    pw.flush()
    return sw.toString()
}