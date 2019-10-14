package com.wj.android.startactivity4result

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 空 Fragment 处理 startActivityForResult
 */
@Suppress("unused")
internal class StartActivity4ResultFragment : Fragment() {

    /** 界面跳转集合 */
    private val map = hashMapOf<Int, MutableLiveData<ActivityResultInfo>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 不创建 View 并保存实例
        retainInstance = true
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
        map[code] = liveData
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
        map[code] = liveData
        start.invoke(this, code)
        return liveData
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (map.containsKey(requestCode)) {
            // 跳转返回，发送事件
            val liveData = map[requestCode] ?: return
            liveData.postValue(ActivityResultInfo(requestCode, resultCode, data))
        }
    }
}