@file:Suppress("unused")
@file:JvmName("FragmentExt")

package cn.wj.android.base.ext

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

/* ----------------------------------------------------------------------------------------- */
/* |                                    Fragment 相关                                       | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 打开 [Activity]
 * > 用于打开普通 [Activity]
 *
 * @param target 目标 [Activity] 类对象
 * @param bundles 添加参数方法块
 */
@JvmOverloads
fun Fragment.startTargetActivity(target: Class<out Activity>, bundles: (Intent.() -> Unit)? = null) {
    this.activity?.startTargetActivity(target, bundles)
}