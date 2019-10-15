@file:Suppress("unused")

package cn.wj.android.common.thread

import android.os.Looper

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