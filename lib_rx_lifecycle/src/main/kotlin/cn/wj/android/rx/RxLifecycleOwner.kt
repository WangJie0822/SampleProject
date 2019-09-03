package cn.wj.android.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Rx 生命周期管理接口
 */
interface RxLifecycleOwner {
    /** RxJ 生命周期管理 */
    var disposables: CompositeDisposable?

    /**
     * 将 Rx 添加到生命周期管理
     */
    fun addDisposable(dis: Disposable) {
        disposables?.add(dis)
    }

    /**
     * 处理所有添加到生命周期管理的事件
     */
    fun disposeAll() {
        disposables?.clear()
        disposables = null
    }
}