@file:Suppress("unused")

package cn.wj.android.base.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import cn.wj.android.base.log.Logger

/**
 * MVVM ViewModel 基类
 * - 继承 [ViewModel]
 * - 实现 [LifecycleObserver] 监听生命周期
 *
 * @author 王杰
 */
abstract class BaseMvvmViewModel
    : ViewModel(),
        LifecycleObserver {

    /**
     * 在 [Lifecycle] 生命周期 onCreate 时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        Logger.t("BaseMvvmViewModel").i("View onCreate ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onStart 时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        Logger.t("BaseMvvmViewModel").i("View onStart ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onResume 时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        Logger.t("BaseMvvmViewModel").i("View onResume ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onPause 时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        Logger.t("BaseMvvmViewModel").i("View onPause ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onStop 时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        Logger.t("BaseMvvmViewModel").i("View onStop ----> ViewModel: $this")
    }

    /**
     * 在 [Lifecycle] 生命周期 onDestroy 时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        Logger.t("BaseMvvmViewModel").i("View onDestroy ----> ViewModel: $this")
    }
}