@file:Suppress("unused")

package com.wj.android.rxforactivityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.Observable

/**
 * 处理 startActivityForResult
 *
 * @param activity 当前 Activity 对象
 *
 * ```
 * RxForActivityResult(activity)
 *     .requestCode(REQUEST_CODE)
 *     .target(TargetActivity::class.java)
 *     .params(params)
 *     .startForResult()
 *     .subscribe({
 *         // on success
 *     }, {
 *         // on error
 *     })
 *
 * RxForActivityResult(activity)
 *     .requestCode(REQUEST_CODE)
 *     .startForResult{ fragment ->
 *         TargetActivity.actionStartForResult(fragment, REQUEST_CODE)
 *     }
 *     .subscribe({
 *         // on success
 *     }, {
 *         // on error
 *     })
 * ```
 *
 * @author 王杰
 */
class RxForActivityResult(activity: AppCompatActivity) {

    /** 空 Fragment，用于处理 onActivityResult */
    private val rxForActivityResultFragment: RxForActivityResultFragment

    init {
        // 初始化 Fragment
        rxForActivityResultFragment = getRxResultFragment(activity)
    }

    /** 目标界面 */
    private var target: Class<out Activity>? = null
    /** 请求码 */
    private var requestCode = -1
    /** 携带数据 */
    private val bundle = Bundle()

    /**
     * 获取 Fragment 对象
     *
     * @param activity 当前 Activity 对象
     *
     * @return 与当前 Activity 关联的 Fragment
     */
    private fun getRxResultFragment(activity: AppCompatActivity): RxForActivityResultFragment {
        // 从 Activity 查找 Fragment 对象
        var rxForActivityResultFragment: RxForActivityResultFragment? = findRxResultFragment(activity)
        if (rxForActivityResultFragment == null) {
            // 没有找到 Fragment 对象，新建
            rxForActivityResultFragment = RxForActivityResultFragment()
            val fragmentManager = activity.supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .add(rxForActivityResultFragment, "RxForActivityResult")
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return rxForActivityResultFragment
    }

    /**
     * 从当前 Activity 获取 Fragment 对象
     *
     * @param activity 当前 Activity
     *
     * @return 与当前 Activity 关联的 Fragment，若没有返回 null
     */
    private fun findRxResultFragment(activity: AppCompatActivity): RxForActivityResultFragment? {
        return activity.supportFragmentManager.findFragmentByTag("RxForActivityResult") as? RxForActivityResultFragment
    }

    /**
     * 设置请求码
     *
     * @param code 请求码
     */
    fun requestCode(code: Int): RxForActivityResult {
        this.requestCode = code
        return this
    }

    /**
     * 设置目标 Activity
     *
     * @param clazz 目标 Activity 类对象
     */
    fun target(clazz: Class<out Activity>): RxForActivityResult {
        this.target = clazz
        return this
    }

    /**
     * 设置跳转参数
     *
     * @param params 跳转参数
     */
    fun params(params: Bundle): RxForActivityResult {
        this.bundle.putAll(params)
        return this
    }

    /**
     * 跳转 Activity 并返回 Observable
     * ```
     * RxForActivityResult(activity)
     *     .requestCode(REQUEST_CODE)
     *     .target(TargetActivity::class.java)
     *     .params(params)
     *     .startForResult()
     *     .subscribe({
     *         // on success
     *     }, {
     *         // on error
     *     })
     * ```
     *
     * @return [Observable] 对象，用于处理返回数据
     */
    fun startForResult(): Observable<RxForActivityResultInfo> {
        require(requestCode != -1) { "Please set requestCode first! requestCode could not be -1!" }
        if (target == null) {
            throw NullPointerException("Target Activity could not be null!")
        }
        val intent = Intent(rxForActivityResultFragment.activity, target).apply {
            putExtras(bundle)
        }
        return rxForActivityResultFragment.startForResult(requestCode, intent)
    }

    /**
     * 跳转 Activity 并返回 Observable
     * ```
     * RxForActivityResult(activity)
     *     .requestCode(REQUEST_CODE)
     *     .startForResult{ fragment ->
     *         TargetActivity.actionStartForResult(fragment, REQUEST_CODE)
     *     }
     *     .subscribe({
     *         // on success
     *     }, {
     *         // on error
     *     })
     * ```
     *
     * @param start 跳转 Activity 方法块
     *
     * @return [Observable] 对象，用于处理返回数据
     */
    fun startForResult(start: (Fragment) -> Unit): Observable<RxForActivityResultInfo> {
        require(requestCode != -1) { "Please set requestCode first!" }
        return rxForActivityResultFragment.startForResult(requestCode, start)
    }
}

