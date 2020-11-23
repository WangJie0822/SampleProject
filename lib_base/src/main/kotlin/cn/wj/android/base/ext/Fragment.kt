@file:Suppress("unused")
@file:JvmName("FragmentExt")

package cn.wj.android.base.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/* ----------------------------------------------------------------------------------------- */
/* |                                    Fragment 相关                                       | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 通过 [Fragment] 对象，打开类型为 [A] 的 [Activity] 界面，并执行 [bundles] 方法块
 * > [bundles] 默认空实现
 *
 * > [Fragment] 中的 [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
 */
@JvmOverloads
inline fun <reified A : Activity> Fragment.startTargetActivity(bundles: (Intent.() -> Unit) = { }) {
    this.activity?.startTargetActivity<A>(bundles)
}

/**
 * 通过 [Fragment] 对象，打开类型为 [A] 的 [Activity] 界面，并携带 [bundle] 中的数据
 * - [bundle] [Bundle] 对象，其中的数据会被添加到 [Intent] 中
 *
 * - [Fragment] 中的 [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
 */
inline fun <reified A : Activity> Fragment.startTargetActivity(bundle: Bundle) {
    context?.startTargetActivity<A>(bundle)
}