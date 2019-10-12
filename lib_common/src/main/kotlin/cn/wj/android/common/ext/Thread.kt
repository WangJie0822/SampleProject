@file:Suppress("unused")
@file:JvmName("ThreadExt")

package cn.wj.android.common.ext

import android.os.Looper
import cn.wj.android.common.thread.MainThreadManager

/**
 * 在主线程执行
 *
 * @param runnable 在主线程执行的代码
 */
fun runOnMainThread(runnable: Runnable) {
    MainThreadManager.postToMainThread(runnable)
}

/**
 * 是否是主线程
 */
fun isMainThread(): Boolean {
    return Looper.getMainLooper() == Looper.myLooper()
}