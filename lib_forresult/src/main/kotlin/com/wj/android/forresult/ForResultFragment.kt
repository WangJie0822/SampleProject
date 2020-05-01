package com.wj.android.forresult

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 空 Fragment 处理 startActivityForResult
 */
@Suppress("unused")
internal class ForResultFragment : Fragment() {
    
    /** 界面跳转集合 */
    private val resultMap: HashMap<Int, MutableLiveData<ActivityResultInfo>> = hashMapOf()
    
    /** 权限申请数据 */
    private var permissionEachData: MutableLiveData<PermissionsResultEachInfo>? = null
    private var permissionAllData: MutableLiveData<PermissionsResultInfo>? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 不创建 View 并保存实例
        retainInstance = true
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 跳转返回，发送事件
        val liveData = resultMap[requestCode] ?: return
        liveData.postValue(ActivityResultInfo(requestCode, resultCode, data))
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // 请求权限返回，发送事件
        permissionAllData?.let { liveData ->
            // 统一处理
            val deniedList = arrayListOf<String>()
            for ((index, result) in grantResults.withIndex()) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[index])
                }
            }
            liveData.postValue(PermissionsResultInfo(deniedList.isEmpty(), permissions.size, deniedList))
        }
        permissionEachData?.let { liveData ->
            // 单个处理
            for ((index, result) in grantResults.withIndex()) {
                liveData.postValue(PermissionsResultEachInfo(result == PackageManager.PERMISSION_GRANTED, permissions[index]))
            }
        }
        
    }
    
    /**
     * 跳转 Activity 并返回 Observable
     *
     * @param intent [Intent] 对象
     *
     * @return [LiveData] 对象，用于处理返回数据
     */
    fun startForResult(intent: Intent, requestCode: Int = -1): LiveData<ActivityResultInfo> {
        val liveData = MutableLiveData<ActivityResultInfo>()
        val code = if (requestCode == -1) nextRequestCode() else requestCode
        resultMap[code] = liveData
        startActivityForResult(intent, code)
        return liveData
    }
    
    /**
     * 跳转 Activity 并返回 Observable
     *
     * @param start 跳转 Activity 方法块
     *
     * @return [LiveData] 对象，用于处理返回数据
     */
    fun startForResult(start: (Fragment, Int) -> Unit, requestCode: Int = -1): LiveData<ActivityResultInfo> {
        val liveData = MutableLiveData<ActivityResultInfo>()
        val code = if (requestCode == -1) nextRequestCode() else requestCode
        resultMap[code] = liveData
        start.invoke(this, code)
        return liveData
    }
    
    /**
     * 请求权限
     *
     * @param permissions 权限列表
     *
     * @return [LiveData] 对象，用于处理权限请求
     */
    fun requestPermissions(permissions: Array<out String>): LiveData<PermissionsResultInfo> {
        val liveData = MutableLiveData<PermissionsResultInfo>()
        val code = nextRequestCode()
        permissionAllData = liveData
        requestPermissions(permissions, code)
        return liveData
    }
    
    /**
     * 请求权限
     *
     * @param permissions 权限列表
     *
     * @return [LiveData] 对象，用于处理权限请求
     */
    fun requestEachPermissions(permissions: Array<out String>): LiveData<PermissionsResultEachInfo> {
        val liveData = MutableLiveData<PermissionsResultEachInfo>()
        val code = nextRequestCode()
        permissionEachData = liveData
        requestPermissions(permissions, code)
        return liveData
    }
    
}