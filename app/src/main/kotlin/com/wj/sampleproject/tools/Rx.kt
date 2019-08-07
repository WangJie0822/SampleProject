@file:Suppress("unused")

package com.wj.sampleproject.tools

import cn.wj.android.base.log.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/* ----------------------------------------------------------------------------------------- */
/* |                                         Rx 相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 异步线程
 * - 请求在 io 线程
 * - 回调在 主线程
 */
fun <T> Observable<T>.asyncSchedulers(): Observable<T> =
        subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

/**
 * 在主线程运行
 *
 * @param run 运行内容
 * @param error 异常回调
 */
fun runOnMainThread(run: () -> Unit, error: ((Throwable) -> Unit)? = null): Disposable =
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    run.invoke()
                }, { throwable ->
                    error?.invoke(throwable)
                    Logger.t("Rx").e("runOnMainThread", throwable)
                })

/**
 * 在 IO 线程运行
 *
 * @param run 运行内容
 * @param error 异常回调
 */
fun runOnIOThread(run: () -> Unit, error: ((Throwable) -> Unit)? = null): Disposable =
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe({
                    run.invoke()
                }, { throwable ->
                    error?.invoke(throwable)
                    Logger.t("Rx").e("runOnMainThread", throwable)
                })

/**
 * 异步任务
 *
 * @param subscribe IO 线程运行内容
 * @param consumer 主线程回调运行内容
 * @param error 异常回调 可为空
 */
fun <T> runAsync(subscribe: () -> T, consumer: (T) -> Unit, error: ((Throwable) -> Unit)? = null) =
        Observable.create<T> { oe ->
            oe.onNext(subscribe.invoke())
            oe.onComplete()
        }.asyncSchedulers()
                .subscribe({ result ->
                    consumer.invoke(result)
                }, { throwable ->
                    error?.invoke(throwable)
                    Logger.t("Rx").e("runOnMainThread", throwable)
                })!!

/**
 * 延时操作
 *
 * @param time 延时时间
 * @param unit 延时单位
 * @param sub 延时后事件
 * @param error 异常回调 可为空
 */
fun delay(time: Long, unit: TimeUnit, sub: () -> Unit, error: ((Throwable) -> Unit)? = null): Disposable =
        Observable.timer(time, unit)
                .asyncSchedulers()
                .subscribe({
                    sub.invoke()
                }, { throwable ->
                    error?.invoke(throwable)
                    Logger.t("Rx").e("runOnMainThread", throwable)
                })


/**
 * 延时操作
 *
 * @param time 延时时间 单位：毫秒
 * @param sub 延时后事件
 * @param error 异常回调 可为空
 */
fun millisecondsTimeDelay(time: Long, sub: () -> Unit, error: ((Throwable) -> Unit)? = null) =
        delay(time, TimeUnit.MILLISECONDS, sub, error)

/**
 * 延时操作
 *
 * @param time 延时时间 单位：秒
 * @param sub 延时后事件
 * @param error 异常回调 可为空
 */
fun secondsTimeDelay(time: Long, sub: () -> Unit, error: ((Throwable) -> Unit)? = null) =
        delay(time, TimeUnit.SECONDS, sub, error)


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
fun <T> Observable<T>.subscribeWithOwner(owner: RxLifeCircle,
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
fun <T> Observable<T>.subscribeWithOwner(owner: RxLifeCircle,
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
fun <T> Observable<T>.subscribeWithOwner(owner: RxLifeCircle,
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
 * Rx 生命周期管理接口
 */
interface RxLifeCircle {

    /** RxJ 生命周期管理  */
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