@file:Suppress("unused")

package cn.wj.android.base.tools

import android.os.Looper

/* ----------------------------------------------------------------------------------------- */
/* |                                        线程相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 是否是主线程
 */
fun isMainThread(): Boolean {
    return Looper.getMainLooper() == Looper.myLooper()
}