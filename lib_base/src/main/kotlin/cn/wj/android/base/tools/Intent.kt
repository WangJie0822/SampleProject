package cn.wj.android.base.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import cn.wj.android.base.utils.AppManager
import cn.wj.android.common.log.InternalLog

/**
 * Intent 相关
 *
 * - 创建时间：2019/10/16
 *
 * @author 王杰
 */

fun String.jumpToBrowser(context: Context = AppManager.getContext()): Boolean {
    return jumpToBrowser(context, this)
}

/**
 * 跳转浏览器
 *
 * @param context Context 对象
 * @param url 跳转 url
 */
fun jumpToBrowser(context: Context, url: String): Boolean {
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        return false
    }
    try {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        context.startActivity(intent)
        return true
    } catch (throwable: Throwable) {
        InternalLog.e("INTENT", throwable, "jumpToBorwser")
        return false
    }
}