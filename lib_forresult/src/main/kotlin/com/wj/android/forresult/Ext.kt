@file:Suppress("unused")
@file:JvmName("ForResultExt")

package com.wj.android.forresult

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
    ForResult(this)
            .requestCode(requestCode)
            .target(target)
            .startForResult()
            .observe(this, Observer(result))
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
    ForResult(this)
            .requestCode(requestCode)
            .startForResult(request)
            .observe(this, Observer(result))
}

/**
 * 跳转 Activity 并等待返回结果
 *
 * @param target 目标跳转 Activity 类对象
 * @param result 返回回调
 * @param requestCode 请求码
 */
@JvmOverloads
fun Fragment.startActivity4Result(target: Class<out Activity>, result: (ActivityResultInfo) -> Unit, requestCode: Int = -1) {
    ForResult(this)
            .requestCode(requestCode)
            .target(target)
            .startForResult()
            .observe(this, Observer(result))
}

/**
 * 跳转 Activity 并等待返回结果
 *
 * @param request 跳转代码块
 * @param result 返回回调
 * @param requestCode 请求码
 */
@JvmOverloads
fun Fragment.startActivity4Result(request: (Fragment, Int) -> Unit, result: (ActivityResultInfo) -> Unit, requestCode: Int = -1) {
    ForResult(this)
            .requestCode(requestCode)
            .startForResult(request)
            .observe(this, Observer(result))
}

/**
 * 申请权限
 * > 全部处理回调
 *
 * @param permissions 权限列表
 * @param result 请求回调
 */
@JvmOverloads
fun FragmentActivity.requestPermissions(vararg permissions: String, result: (PermissionsResultInfo) -> Unit = {}) {
    ForResult(this)
            .requestPermissions(*permissions)
            .observe(this, Observer(result))
}

/**
 * 申请权限
 * > 全部处理回调
 *
 * @param permissions 权限列表
 * @param result 请求回调
 */
@JvmOverloads
fun Fragment.requestPermissions(vararg permissions: String, result: (PermissionsResultInfo) -> Unit = {}) {
    ForResult(this)
            .requestPermissions(*permissions)
            .observe(this, Observer(result))
}

/**
 * 申请权限
 * > 单个权限回调
 *
 * @param permissions 权限列表
 * @param result 请求回调
 */
fun FragmentActivity.requestEachPermissions(vararg permissions: String, result: (PermissionsResultEachInfo) -> Unit) {
    ForResult(this)
            .requestEachPermissions(*permissions)
            .observe(this, Observer(result))
}

/**
 * 申请权限
 * > 单个权限回调
 *
 * @param permissions 权限列表
 * @param result 请求回调
 */
fun Fragment.requestEachPermissions(vararg permissions: String, result: (PermissionsResultEachInfo) -> Unit) {
    ForResult(this)
            .requestEachPermissions(*permissions)
            .observe(this, Observer(result))
}

/**
 * 请求权限并执行
 * > 只有在所有权限都通过才会执行
 *
 * @param permissions 权限列表
 * @param result 请求回调
 */
fun FragmentActivity.runWithPermissions(vararg permissions: String, result: () -> Unit) {
    ForResult(this)
            .requestPermissions(*permissions)
            .observe(this, Observer {
                if (it.allGranted) {
                    result.invoke()
                }
            })
}

/**
 * 请求权限并执行
 * > 只有在所有权限都通过才会执行
 *
 * @param permissions 权限列表
 * @param result 请求回调
 */
fun Fragment.runWithPermissions(vararg permissions: String, result: () -> Unit) {
    ForResult(this)
            .requestPermissions(*permissions)
            .observe(this, Observer {
                if (it.allGranted) {
                    result.invoke()
                }
            })
}



