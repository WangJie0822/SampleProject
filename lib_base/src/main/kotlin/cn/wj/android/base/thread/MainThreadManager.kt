@file:Suppress("unused")

package cn.wj.android.base.thread

import android.os.Handler
import android.os.Looper

/**
 * 主线程管理器
 * - 在任意地方将代码 post 到主线程执行
 * ```
 * MainThreadManager.postToMainThread({
 *      // do something
 * })
 * ```
 */
object MainThreadManager {
    
    /**
     * 主线程 Handler
     */
    @Volatile
    private var mMainHandler: Handler? = null
    
    /**
     * 将事件延时后 post 到主线程执行
     *
     * @param runnable 在主线程执行的代码
     * @param delay 延时时间 ms，默认不延时
     */
    @JvmStatic
    fun postToMainThread(runnable: Runnable, delay: Long = 0L) {
        if (mMainHandler == null) {
            synchronized(MainThreadManager::class.java) {
                if (mMainHandler == null) {
                    mMainHandler = Handler(Looper.getMainLooper())
                }
            }
        }
        mMainHandler?.postDelayed(runnable, delay)
    }
}