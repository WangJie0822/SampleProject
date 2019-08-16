package com.wj.sampleproject.rxresult

import android.content.Intent

/**
 * Activity 返回数据实体类
 *
 * @param requestCode 请求码
 * @param resultCode 返回码
 * @param result 返回数据
 */
data class RxForActivityResultInfo(
        val requestCode: Int,
        val resultCode: Int,
        val result: Intent?
)