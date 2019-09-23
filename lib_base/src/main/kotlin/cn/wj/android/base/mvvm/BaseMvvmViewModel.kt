@file:Suppress("unused")

package cn.wj.android.base.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import cn.wj.android.base.log.InternalLog
import cn.wj.android.common.Tagable
import java.util.*

/**
 * MVVM ViewModel 基类
 * - 继承 [ViewModel]
 * - 实现 [LifecycleObserver] 监听生命周期
 *
 * @author 王杰
 */
abstract class BaseMvvmViewModel
    : ViewModel(),
        Tagable {

    override val mTagMaps: HashMap<String, Any> = hashMapOf()

    override var mClosed: Boolean = false

    override fun onCleared() {
        clearTags()
        InternalLog.i("BaseMvvmViewModel", "View onCleared ----> ViewModel: $this")
    }
}