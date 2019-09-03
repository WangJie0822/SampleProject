@file:Suppress("unused")

package cn.wj.android.rx

import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions

/**
 * 订阅并关联生命周期管理
 *
 * @param owner Rx 生命周期管理接口
 * @param onNext 事件处理
 * @param onComplete 事件完成
 * @param onSubscribe 事件订阅
 *
 * @return [Disposable] 事件对象
 */
fun <T> Observable<T>.subscribeWithOwner(owner: RxLifecycleOwner,
                                         onNext: Consumer<T> = Functions.emptyConsumer(),
                                         onError: Consumer<Throwable> = Functions.ON_ERROR_MISSING,
                                         onComplete: Action = Functions.EMPTY_ACTION,
                                         onSubscribe: Consumer<Disposable> = Functions.emptyConsumer()
): Disposable {
    val disposable = this.subscribe(onNext, onError, onComplete, onSubscribe)
    owner.addDisposable(disposable)
    return disposable
}

/**
 * 订阅并关联生命周期管理
 *
 * @param owner Rx 生命周期管理接口
 * @param onNext 事件处理
 * @param onComplete 事件完成
 * @param onSubscribe 事件订阅
 *
 * @return [Disposable] 事件对象
 */
fun <T> Observable<T>.subscribeWithOwner(owner: RxLifecycleOwner,
                                         onNext: ((T) -> Unit)? = null,
                                         onError: ((Throwable) -> Unit)? = null,
                                         onComplete: (() -> Unit)? = null,
                                         onSubscribe: ((Disposable) -> Unit)? = null
): Disposable {
    val onNextConsumer: Consumer<T> = if (onNext == null) {
        Functions.emptyConsumer()
    } else {
        Consumer { t -> onNext.invoke(t) }
    }
    val onErrorConsumer: Consumer<Throwable> = if (onError == null) {
        Functions.emptyConsumer()
    } else {
        Consumer { t -> onError.invoke(t) }
    }
    val onCompleteAction: Action = if (onComplete == null) {
        Functions.EMPTY_ACTION
    } else {
        Action { onComplete.invoke() }
    }
    val onSubscribeConsumer: Consumer<Disposable> = if (onSubscribe == null) {
        Functions.emptyConsumer()
    } else {
        Consumer { d -> onSubscribe.invoke(d) }
    }
    return this.subscribeWithOwner(owner,
            onNextConsumer,
            onErrorConsumer,
            onCompleteAction,
            onSubscribeConsumer)
}

/**
 * 订阅并关联生命周期管理
 *
 * @param owner Rx 生命周期管理接口
 * @param observer 观察者对象
 */
fun <T> Observable<T>.subscribeWithOwner(owner: RxLifecycleOwner,
                                         observer: Observer<T>
) {
    this.subscribe(object : Observer<T> {
        override fun onComplete() {
            observer.onComplete()
        }

        override fun onSubscribe(d: Disposable) {
            observer.onSubscribe(d)
            owner.addDisposable(d)
        }

        override fun onNext(t: T) {
            observer.onNext(t)
        }

        override fun onError(e: Throwable) {
            observer.onError(e)
        }

    })
}

/**
 * 订阅并关联生命周期管理
 *
 * @param owner Android 生命周期接口
 * @param onNext 事件处理
 * @param onComplete 事件完成
 * @param onSubscribe 事件订阅
 *
 * @return [Disposable] 事件对象
 */
fun <T> Observable<T>.subscribeWithOwner(owner: LifecycleOwner,
                                         onNext: Consumer<T> = Functions.emptyConsumer(),
                                         onError: Consumer<Throwable> = Functions.ON_ERROR_MISSING,
                                         onComplete: Action = Functions.EMPTY_ACTION,
                                         onSubscribe: Consumer<Disposable> = Functions.emptyConsumer()
): Disposable {
    val disposable = this.subscribe(onNext, onError, onComplete, onSubscribe)
    getObserver(owner).addDisposable(disposable)
    return disposable
}

/**
 * 订阅并关联生命周期管理
 *
 * @param owner Android 生命周期接口
 * @param onNext 事件处理
 * @param onComplete 事件完成
 * @param onSubscribe 事件订阅
 *
 * @return [Disposable] 事件对象
 */
fun <T> Observable<T>.subscribeWithOwner(owner: LifecycleOwner,
                                         onNext: ((T) -> Unit)? = null,
                                         onError: ((Throwable) -> Unit)? = null,
                                         onComplete: (() -> Unit)? = null,
                                         onSubscribe: ((Disposable) -> Unit)? = null
): Disposable {
    val onNextConsumer: Consumer<T> = if (onNext == null) {
        Functions.emptyConsumer()
    } else {
        Consumer { t -> onNext.invoke(t) }
    }
    val onErrorConsumer: Consumer<Throwable> = if (onError == null) {
        Functions.emptyConsumer()
    } else {
        Consumer { t -> onError.invoke(t) }
    }
    val onCompleteAction: Action = if (onComplete == null) {
        Functions.EMPTY_ACTION
    } else {
        Action { onComplete.invoke() }
    }
    val onSubscribeConsumer: Consumer<Disposable> = if (onSubscribe == null) {
        Functions.emptyConsumer()
    } else {
        Consumer { d -> onSubscribe.invoke(d) }
    }
    return this.subscribeWithOwner(owner,
            onNextConsumer,
            onErrorConsumer,
            onCompleteAction,
            onSubscribeConsumer)
}

/**
 * 订阅并关联生命周期管理
 *
 * @param owner Android 生命周期接口
 * @param observer 观察者对象
 */
fun <T> Observable<T>.subscribeWithOwner(owner: LifecycleOwner,
                                         observer: Observer<T>
) {
    this.subscribe(object : Observer<T> {
        override fun onComplete() {
            observer.onComplete()
        }

        override fun onSubscribe(d: Disposable) {
            observer.onSubscribe(d)
            getObserver(owner).addDisposable(d)
        }

        override fun onNext(t: T) {
            observer.onNext(t)
        }

        override fun onError(e: Throwable) {
            observer.onError(e)
        }

    })
}

/**
 * HashMap 对象，保存 Rx、Android 生命周期管理对象
 */
internal val rxLifecycles = hashMapOf<LifecycleOwner, RxLifecycleObserver>()

/**
 * 根据 Android 生命周期对象获取 Rx 生命周期管理对象
 *
 * @param owner Android 生命周期对象
 *
 * @return Rx 生命周期管理对象
 */
internal fun getObserver(owner: LifecycleOwner): RxLifecycleObserver {
    var observer: RxLifecycleObserver?
    if (rxLifecycles.containsKey(owner)) {
        // 集合中已包含观察者对象
        observer = rxLifecycles[owner]
        if (observer != null) {
            // 且不为空，返回已有的观察者对象
            return observer
        }
    }
    // 没有已存在的，新建观察者对象
    observer = RxLifecycleObserver(owner)
    // 添加到 Map 集合以复用
    rxLifecycles[owner] = observer
    return observer
}