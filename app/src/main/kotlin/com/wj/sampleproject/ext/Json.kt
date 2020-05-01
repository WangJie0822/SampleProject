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
 * 将 String 转为 数据对象
 *
 * @param T 数据泛型
 *
 * @param gson Gson 对象
 *
 * @return 数据对象
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
 * 将对象转为 String
 *
 * @param gson Gson 对象
 *
 * @return String 字符串
 */
inline fun <reified T> T?.toJsonString(gson: Gson = Gson()): String {
    return try {
        gson.toJson(this, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        ""
    }
}

/**
 * 将 JSONObject 转换为 RequestBody
 */
fun JSONObject.toJsonRequestBody(): RequestBody {
    return toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}