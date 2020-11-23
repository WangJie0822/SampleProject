@file:Suppress("unused")
@file:JvmName("ThreadExt")

package cn.wj.android.base.thread

import android.os.Looper

/**
 * 将事件[runnable]延时[delay]ms之后在主线程执行
 * > [delay] 默认 `0L`
 */
@JvmOverloads
fun runOnMainThread(runnable: Runnable, delay: Long = 0) {
    MainThreadManager.postToMainThread(runnable, delay)
}

/** 判断当前线程是否时主线程 */
fun isMainThread(): Boolean {
    return Looper.getMainLooper() == Looper.myLooper()
}