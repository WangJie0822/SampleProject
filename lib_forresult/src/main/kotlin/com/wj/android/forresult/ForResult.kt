@file:Suppress("unused")

package com.wj.android.forresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData

/**
 * 处理 startActivityForResult、requestPermission
 *
 * @param fragmentManager [FragmentManager] 对象
 *
 * ```
 * // Activity、Fragment startActivityForResult
 * ForResult(activity or fragment)
 *     .requestCode(REQUEST_CODE) // 指定请求码
 *     .target(TargetActivity::class.java) // 指定目标 Activity
 *     .params(params) // 配置请求参数
 *     .startForResult() // 跳转
 *     .observe(lifecyclerOwner, Observer { // 回调
 *         // do something
 *     })
 *
 * // Activity、Fragment startActivityForResult
 * ForResult(activity or fragment)
 *     .startForResult{ fragment, requestCode -> // 自动生成 requestCode，回调中进行跳转
 *         TargetActivity.actionStartForResult(fragment, requestCode)
 *     }
 *     .observe(lifecyclerOwner, Observer { // 回调
 *         // do something
 *     })
 *
 * // 请求权限，单个权限处理，每个权限都会进行回调
 * ForResult(activity or fragment)
 *     .requestEachPermissions(permissions) // 权限列表
 *     .observe(lifecyclerOwner, Observer { // 回调
 *         // do something
 *     })
 *
 * // 请求权限，统一处理
 * ForResult(activity or fragment)
 *     .requestPermissions(permissions) // 权限列表
 *     .observe(lifecyclerOwner, Observer { // 回调
 *         // do something
 *     })
 * ```
 *
 * @author 王杰
 */
class ForResult(fragmentManager: FragmentManager) {
    
    /** 空 Fragment，用于处理 onActivityResult */
    private val forResultFragment: ForResultFragment
    
    init {
        // 初始化 Fragment
        forResultFragment = get4ResultFragment(fragmentManager)
    }
    
    /**
     * 子构造方法
     *
     * @param fragment [Fragment] 对象
     */
    constructor(fragment: Fragment) : this(fragment.childFragmentManager)
    
    /**
     * 子构造方法
     *
     * @param activity [FragmentActivity] 对象
     */
    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager)
    
    /** 目标界面 */
    private var target: Class<out Activity>? = null
    
    /** 请求码 */
    private var requestCode = -1
    
    /** 携带数据 */
    private val bundle = Bundle()
    
    /**
     * 获取 Fragment 对象
     *
     * @param fragmentManager [FragmentManager] 对象
     *
     * @return 与当前关联的 Fragment
     */
    private fun get4ResultFragment(fragmentManager: FragmentManager): ForResultFragment {
        // 从 Activity 查找 Fragment 对象
        var forResultFragment: ForResultFragment? = find4ResultFragment(fragmentManager)
        if (forResultFragment == null) {
            // 没有找到 Fragment 对象，新建
            forResultFragment = ForResultFragment()
            fragmentManager
                    .beginTransaction()
                    .add(forResultFragment, "wj-ForResultFragment")
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return forResultFragment
    }
    
    /**
     * 从当前 Activity 获取 Fragment 对象
     *
     * @param fragmentManager [FragmentManager] 对象
     *
     * @return 与当前关联的 Fragment，若没有返回 null
     */
    private fun find4ResultFragment(fragmentManager: FragmentManager): ForResultFragment? {
        return fragmentManager.findFragmentByTag("wj-ForResultFragment") as? ForResultFragment
    }
    
    /**
     * 设置请求码
     *
     * @param code 请求码
     */
    fun requestCode(code: Int): ForResult {
        this.requestCode = code
        return this
    }
    
    /**
     * 设置目标 Activity
     *
     * @param clazz 目标 Activity 类对象
     */
    fun target(clazz: Class<out Activity>): ForResult {
        this.target = clazz
        return this
    }
    
    /**
     * 设置跳转参数
     *
     * @param params 跳转参数
     */
    fun params(params: Bundle): ForResult {
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
        val intent = Intent(forResultFragment.activity, target).apply {
            putExtras(bundle)
        }
        return forResultFragment.startForResult(intent, requestCode)
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
        return forResultFragment.startForResult(start, requestCode)
    }
    
    /**
     * 申请权限
     * > 全部校验，统一返回结果
     *
     * @param permissions 权限列表
     */
    fun requestPermissions(vararg permissions: String): LiveData<PermissionsResultInfo> {
        return forResultFragment.requestPermissions(permissions)
    }
    
    /**
     * 申请权限
     * > 单个校验，每次校验回调
     *
     * @param permissions 权限列表
     */
    fun requestEachPermissions(vararg permissions: String): LiveData<PermissionsResultEachInfo> {
        return forResultFragment.requestEachPermissions(permissions)
    }
}

