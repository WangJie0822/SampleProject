package cn.wj.android.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable

/**
 * Rx 生命周期观察者
 *
 * @param lifecycleOwner Android 生命周期管理接口
 */
class RxLifecycleObserver(lifecycleOwner: LifecycleOwner) : LifecycleEventObserver, RxLifecycleOwner {

    override var disposables: CompositeDisposable? = null

    init {
        disposables = CompositeDisposable()
        // 注册观察者
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // 生命周期走到 onDestroy，消费所有事件
            disposeAll()
            // 移除观察者
            source.lifecycle.removeObserver(this)
            // 从 Map 集合中移除
            rxLifecycles.remove(source)
        }
    }
}