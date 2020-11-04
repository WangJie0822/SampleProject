@file:Suppress("unused")

package cn.wj.android.base.tools

import org.json.JSONObject

/**
 * 新建 [JSONObject]
 */
fun jsonObjectOf(vararg pairs: Pair<String, Any?>) = JSONObject().apply {
    for ((key, value) in pairs) {
        when (value) {
            is Boolean -> put(key, value)
            is Double -> put(key, value)
            is Int -> put(key, value)
            is Long -> put(key, value)
            else -> put(key, value)
        }
    }
}