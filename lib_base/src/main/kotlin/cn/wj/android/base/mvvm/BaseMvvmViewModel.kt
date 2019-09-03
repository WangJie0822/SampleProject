@file:Suppress("unused")

package cn.wj.android.base.mvvm

import androidx.lifecycle.*
import cn.wj.android.base.log.InternalLog

/**
 * MVVM ViewModel 基类
 * - 继承 [ViewModel]
 * - 实现 [LifecycleObserver] 监听生命周期
 *
 * @author 王杰
 */
abstract class BaseMvvmViewModel
    : ViewModel(),
        LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate(source)
            Lifecycle.Event.ON_START -> onStart(source)
            Lifecycle.Event.ON_RESUME -> onResume(source)
            Lifecycle.Event.ON_PAUSE -> onPause(source)
            Lifecycle.Event.ON_STOP -> onStop(source)
            Lifecycle.Event.ON_DESTROY -> onDestroy(source)
            Lifecycle.Event.ON_ANY -> {
            }
        }
    }

    /**
     * 在 [Lifecycle] 生命周期 onCreate 时调用
     */
    open fun onCreate(source: LifecycleOwner) {
        InternalLog.i("BaseMvvmViewModel", "View onCreate ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onStart 时调用
     */
    open fun onStart(source: LifecycleOwner) {
        InternalLog.i("BaseMvvmViewModel", "View onStart ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onResume 时调用
     */
    open fun onResume(source: LifecycleOwner) {
        InternalLog.i("BaseMvvmViewModel", "View onResume ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onPause 时调用
     */
    open fun onPause(source: LifecycleOwner) {
        InternalLog.i("BaseMvvmViewModel", "View onPause ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onStop 时调用
     */
    open fun onStop(source: LifecycleOwner) {
        InternalLog.i("BaseMvvmViewModel", "View onStop ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onDestroy 时调用
     */
    open fun onDestroy(source: LifecycleOwner) {
        InternalLog.i("BaseMvvmViewModel", "View onDestroy ----> ViewModel: $this")
    }

    override fun onCleared() {
        InternalLog.i("BaseMvvmViewModel", "View onCleared ----> ViewModel: $this")
    }
}