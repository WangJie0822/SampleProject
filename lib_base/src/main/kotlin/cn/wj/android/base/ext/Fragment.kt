@file:Suppress("unused")
@file:JvmName("FragmentExt")

package cn.wj.android.base.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment

/* ----------------------------------------------------------------------------------------- */
/* |                                    Fragment 相关                                       | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 执行 [bundles] 方法块配置 [Intent] 后，通过 [Context] 对象，打开类型为 [A] 的 [Activity] 界面，并传递 [options]
 * > [bundles] 默认空实现
 *
 * > [options] 启动配置相关，默认 `null`
 *
 * > [Fragment] 中的 [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
 */
inline fun <reified A : Activity> Fragment.startTargetActivity(
        bundles: (Intent.() -> Unit) = { },
        options: ActivityOptionsCompat? = null
) {
    this.activity?.startTargetActivity<A>(bundles, options)
}

/**
 * 将 [bundle] 中的数据添加到 [Intent] 后，通过 [Context] 对象，打开类型为 [A] 的 [Activity] 界面，并传递 [options]
 * > [bundle] [Bundle] 对象，其中的数据会被添加到 [Intent] 中
 *
 * > [options] 启动配置相关，默认 `null`
 *
 * - [Fragment] 中的 [Context] 对象若不是 [Activity]，则会调用 [Intent.addFlags] ([Intent.FLAG_ACTIVITY_NEW_TASK])
 * 在新的栈中打开
 */
inline fun <reified A : Activity> Fragment.startTargetActivity(
        bundle: Bundle,
        options: ActivityOptionsCompat? = null
) {
    context?.startTargetActivity<A>(bundle, options)
}