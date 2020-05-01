@file:Suppress("unused")
@file:JvmName("ThreadExt")

package cn.wj.android.base.thread

import android.os.Looper

/**
 * 在主线程执行
 *
 * @param runnable 在主线程执行的代码
 * @param delay 延时时间：ms
 */
@JvmOverloads
fun runOnMainThread(runnable: Runnable, delay: Long = 0) {
    MainThreadManager.postToMainThread(runnable, delay)
}

/**
 * 是否是主线程
 *
 * @return 是否是主线程
 */
fun isMainThread(): Boolean {
    return Looper.getMainLooper() == Looper.myLooper()
}