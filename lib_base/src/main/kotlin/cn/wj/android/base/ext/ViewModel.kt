@file:Suppress("unused")

package cn.wj.android.base.ext

import cn.wj.android.common.Tagable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private const val JOB_KEY = "cn.wj.android.lifecycle.ViewModelCoroutineScope.JOB_KEY"

/**
 * Tagable 接口的 协程范围
 */
val Tagable.coroutineScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY,
                MyCloseableCoroutineScope(SupervisorJob() + Dispatchers.Main))
    }

internal class MyCloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {

    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}