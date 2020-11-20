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
 * 通过 [Fragment] 对象，打开类型为 [target] 的 [Activity] 界面，并携带 [bundles] 中的数据
 * - [bundles] 默认没有数据
 * - [Fragment] 中的 [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
 */
@JvmOverloads
fun Fragment.startTargetActivity(target: Class<out Activity>, bundles: (Intent.() -> Unit)? = null) {
    this.activity?.startTargetActivity(target, bundles)
}

/**
 * 通过 [Fragment] 对象，打开类型为 [target] 的 [Activity] 界面，并携带 [bundle] 中的数据
 * - [bundle] [Bundle] 对象，其中的数据会被添加到 [Intent] 中
 * - [Fragment] 中的 [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
 */
fun Fragment.startTargetActivity(target: Class<out Activity>, bundle: Bundle) {
    context?.startTargetActivity(target, bundle)
}