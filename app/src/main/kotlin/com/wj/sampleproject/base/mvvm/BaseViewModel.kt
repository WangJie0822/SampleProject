package com.wj.sampleproject.base.mvvm

import cn.wj.android.base.mvvm.BaseMvvmViewModel
import com.wj.sampleproject.tools.RxLifeCircle
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

/**
 * MVVM ViewModel 基类
 */
abstract class BaseViewModel
    : BaseMvvmViewModel(),
        KoinComponent,
        RxLifeCircle {

    /** Rx 生命周期管理 */
    override var disposables: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        // 处理 Rx 事件
        disposeAll()
    }
}