@file:Suppress("unused")
@file:JvmName("ContextExt")

package cn.wj.android.base.ext

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent

/* ----------------------------------------------------------------------------------------- */
/* |                                    Context 相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 当前进程名称
 */
val Context.curProcessName: String?
    get() {
        val pid = android.os.Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

/**
 * 当前进程是否是主进程
 */
val Context.isMainProcess: Boolean
    get() = packageName == curProcessName

/**
 * 打开 [Activity]
 * - 用于打开普通 [Activity]
 *
 * @param target 目标 [Activity] 类对象
 * @param bundles 添加参数方法块
 */
@JvmOverloads
fun Context.startTargetActivity(target: Class<out Activity>, bundles: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, target)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    bundles?.invoke(intent)
    this.startActivity(intent)
}