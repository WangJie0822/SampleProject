@file:Suppress("unused")
@file:JvmName("StartActivity4ResultExt")

package com.wj.android.startactivity4result

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer

/** 请求码 - 自增 */
internal var requestCode = 0

/**
 * 获取下一个请求码
 * * 达到最大值之后重新从零开始
 */
internal fun nextRequestCode(): Int {
    if (requestCode >= Int.MAX_VALUE) {
        requestCode = 0
    } else {
        requestCode++
    }
    return requestCode
}

/**
 * 跳转 Activity 并等待返回结果
 *
 * @param target 目标跳转 Activity 类对象
 * @param result 返回回调
 * @param requestCode 请求码
 */
@JvmOverloads
fun FragmentActivity.startActivity4Result(target: Class<out Activity>, result: (ActivityResultInfo) -> Unit, requestCode: Int = -1) {
    StartActivity4Result(this)
            .requestCode(requestCode)
            .target(target)
            .startForResult()
            .observe(this, Observer {
                result.invoke(it)
            })
}

/**
 * 跳转 Activity 并等待返回结果
 *
 * @param request 跳转代码块
 * @param result 返回回调
 * @param requestCode 请求码
 */
@JvmOverloads
fun FragmentActivity.startActivity4Result(request: (Fragment, Int) -> Unit, result: (ActivityResultInfo) -> Unit, requestCode: Int = -1) {
    StartActivity4Result(this)
            .requestCode(requestCode)
            .startForResult(request)
            .observe(this, Observer {
                result.invoke(it)
            })
}