@file:Suppress("unused")

package cn.wj.android.base.ext

import cn.wj.android.base.ui.activity.BaseLibActivity
import cn.wj.android.base.ui.dialog.BaseLibDialog
import cn.wj.android.base.ui.fragment.BaseLibFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private const val JOB_KEY = "cn.wj.android.base.ViewModelCoroutineScope.JOB_KEY"

/** Activity 协程范围 */
val BaseLibActivity.activityScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY,
                CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main))
    }

/** Fragment 协程范围 */
val BaseLibFragment.fragmentScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY,
                CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main))
    }

/** Dialog 协程范围 */
val BaseLibDialog.dialogScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY,
                CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main))
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}