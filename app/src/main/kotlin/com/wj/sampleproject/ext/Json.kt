@file:Suppress("unused")

package com.wj.sampleproject.ext

import cn.wj.android.logger.Logger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * 将 [String] 转换为 [T] 类型数据对象并返回，可传参数 [gson]
 * > 转换失败返回 `null`
 */
inline fun <reified T> String?.toTypeEntity(gson: Gson = Gson()): T? {
    return try {
        gson.fromJson(this, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        Logger.t("JSON").e(e, "toBean")
        null
    }
}

/**
 * 将 [T] 对象转换为 [String] 并返回，可传参数 [gson]
 * > 转换失败返回 `""`
 */
inline fun <reified T> T?.toJsonString(gson: Gson = Gson()): String {
    return try {
        gson.toJson(this, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        ""
    }
}

/** 将 [JSONObject] 转换为 [RequestBody] 并返回 */
fun JSONObject.toJsonRequestBody(): RequestBody {
    return toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}