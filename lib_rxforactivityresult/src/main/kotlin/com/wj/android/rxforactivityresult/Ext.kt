@file:Suppress("unused")

package com.wj.android.rxforactivityresult

import android.app.Activity
import android.content.Intent
import io.reactivex.Observable

/**
 * 处理返回数据，筛选返回成功结果
 */
fun Observable<RxForActivityResultInfo>.withSuccess(): Observable<RxForActivityResultInfo> {
    return this.filter { result -> result.resultCode == Activity.RESULT_OK }
}

/**
 * 处理返回数据，筛选返回成功结果，并返回返回数据
 */
fun Observable<RxForActivityResultInfo>.withData(): Observable<Intent> {
    return this.filter { result -> result.resultCode == Activity.RESULT_OK && result.result != null }
            .map { it.result!! }
}