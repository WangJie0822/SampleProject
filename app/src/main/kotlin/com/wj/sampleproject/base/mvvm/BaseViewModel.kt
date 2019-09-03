package com.wj.sampleproject.base.mvvm

import cn.wj.android.base.mvvm.BaseMvvmViewModel
import cn.wj.android.rx.RxLifecycleOwner
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

/**
 * MVVM ViewModel 基类
 */
abstract class BaseViewModel
    : BaseMvvmViewModel(),
        KoinComponent,
        RxLifecycleOwner {

    /** Rx 生命周期管理 */
    override var disposables: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        // 处理 Rx 事件
        disposeAll()
    }
}