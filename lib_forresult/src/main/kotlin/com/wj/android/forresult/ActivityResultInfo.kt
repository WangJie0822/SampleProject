package com.wj.android.forresult

import android.content.Intent

/**
 * Activity 返回数据
 *
 * @param requestCode 请求码
 * @param resultCode 返回码
 * @param result 返回数据
 *
 * 创建时间：2019/10/12
 *
 * @author 王杰
 */
data class ActivityResultInfo(
        var requestCode: Int,
        var resultCode: Int,
        var result: Intent?
)