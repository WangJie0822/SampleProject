@file:Suppress("unused")

package cn.wj.android.base.expanding

import android.app.ActivityManager
import android.content.Context

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
 * 是否是主进程
 */
val Context.isMainProcess: Boolean
    get() = packageName == curProcessName