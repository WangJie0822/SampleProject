@file:Suppress("unused")
@file:JvmName("ContextExt")

package cn.wj.android.base.ext

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import cn.wj.android.base.tools.getDeviceScreenHeight
import cn.wj.android.base.tools.getDeviceScreenWidth
import cn.wj.android.base.tools.getStatusBarHeight

/* ----------------------------------------------------------------------------------------- */
/* |                                    Context 相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/** 当前进程名称 */
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

/** 当前进程是否是主进程 */
val Context.isMainProcess: Boolean
    get() = packageName == curProcessName

/** 屏幕宽度 */
val Context.screenWidth: Int
    get() = getDeviceScreenWidth(this)

/** 屏幕高度 */
val Context.screenHeight: Int
    get() = getDeviceScreenHeight(this)

/** 状态栏高度 */
val Context.statusBarHeight: Int
    get() = getStatusBarHeight(this)

/**
 * 通过 [Context] 对象，打开类型为 [target] 的 [Activity] 界面，并携带 [bundles] 中的数据
 * - [bundles] 默认没有数据
 * - [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
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