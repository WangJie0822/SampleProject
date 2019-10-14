@file:Suppress("unused")

package com.wj.android.startactivity4result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData

/**
 * 处理 startActivityForResult
 *
 * @param activity 当前 Activity 对象
 *
 * ```
 * StartActivity4Result(activity)
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
 * StartActivity4Result(activity)
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
class StartActivity4Result(activity: FragmentActivity) {

    /** 空 Fragment，用于处理 onActivityResult */
    private val startActivity4ResultFragment: StartActivity4ResultFragment

    init {
        // 初始化 Fragment
        startActivity4ResultFragment = get4ResultFragment(activity)
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
    private fun get4ResultFragment(activity: FragmentActivity): StartActivity4ResultFragment {
        // 从 Activity 查找 Fragment 对象
        var startActivity4ResultFragment: StartActivity4ResultFragment? = find4ResultFragment(activity)
        if (startActivity4ResultFragment == null) {
            // 没有找到 Fragment 对象，新建
            startActivity4ResultFragment = StartActivity4ResultFragment()
            val fragmentManager = activity.supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .add(startActivity4ResultFragment, "StartActivity4Result")
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return startActivity4ResultFragment
    }

    /**
     * 从当前 Activity 获取 Fragment 对象
     *
     * @param activity 当前 Activity
     *
     * @return 与当前 Activity 关联的 Fragment，若没有返回 null
     */
    private fun find4ResultFragment(activity: FragmentActivity): StartActivity4ResultFragment? {
        return activity.supportFragmentManager.findFragmentByTag("StartActivity4Result") as? StartActivity4ResultFragment
    }

    /**
     * 设置请求码
     *
     * @param code 请求码
     */
    fun requestCode(code: Int): StartActivity4Result {
        this.requestCode = code
        return this
    }

    /**
     * 设置目标 Activity
     *
     * @param clazz 目标 Activity 类对象
     */
    fun target(clazz: Class<out Activity>): StartActivity4Result {
        this.target = clazz
        return this
    }

    /**
     * 设置跳转参数
     *
     * @param params 跳转参数
     */
    fun params(params: Bundle): StartActivity4Result {
        this.bundle.putAll(params)
        return this
    }

    /**
     * 跳转 Activity 并返回 Observable
     * ```
     * StartActivity4Result(activity)
     *     .target(TargetActivity::class.java)
     *     .params(params)
     *     .startForResult()
     *     .observe(lifeCycleOwner, {
     *         // on result
     *     })
     * ```
     *
     * @return [LiveData] 对象，用于处理返回数据
     */
    fun startForResult(): LiveData<ActivityResultInfo> {
        if (target == null) {
            throw NullPointerException("Target Activity could not be null!")
        }
        val intent = Intent(startActivity4ResultFragment.activity, target).apply {
            putExtras(bundle)
        }
        return startActivity4ResultFragment.startForResult(intent, requestCode)
    }

    /**
     * 跳转 Activity 并返回 Observable
     * ```
     * StartActivity4Result(activity)
     *     .startForResult{ fragment, requestCode ->
     *         TargetActivity.actionStartForResult(fragment, requestCode)
     *     }
     *     .observe(lifeCycleOwner, {
     *         // on result
     *     })
     * ```
     *
     * @param start 跳转 Activity 方法块
     *
     * @return [LiveData] 对象，用于处理返回数据
     */
    fun startForResult(start: (Fragment, Int) -> Unit): LiveData<ActivityResultInfo> {
        return startActivity4ResultFragment.startForResult(start, requestCode)
    }
}

