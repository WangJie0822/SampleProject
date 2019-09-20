@file:Suppress("unused")

package cn.wj.android.base.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import cn.wj.android.base.log.InternalLog

/**
 * MVVM ViewModel 基类
 * - 继承 [ViewModel]
 * - 实现 [LifecycleObserver] 监听生命周期
 *
 * @author 王杰
 */
abstract class BaseMvvmViewModel
    : ViewModel() {

    override fun onCleared() {
        InternalLog.i("BaseMvvmViewModel", "View onCleared ----> ViewModel: $this")
    }
}