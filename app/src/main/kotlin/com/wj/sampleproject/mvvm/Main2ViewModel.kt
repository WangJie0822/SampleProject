package com.wj.sampleproject.mvvm

import androidx.lifecycle.MutableLiveData
import cn.wj.android.logger.Logger
import cn.wj.android.rx.subscribeWithOwner
import com.wj.sampleproject.base.mvvm.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Main2 ViewModel
 */
class Main2ViewModel
    : BaseViewModel() {

    val clickLiveData = MutableLiveData<Int>()

    /** 点击事件 */
    val onClick: () -> Unit = {
        //        clickLiveData.postValue(0)
        Observable.create<String> {
            try {
                for (i in 0 until 100) {
                    it.onNext("++$i")
                    Thread.sleep(500L)
                }
            } catch (e: Exception) {
                it.onError(e)
            }
            it.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWithOwner(this, {
                    Logger.e("print ----> $it")
                }, {
                    Logger.e(it)
                })
    }
}