package com.wj.sampleproject.ext

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

fun <T> String.toEntity(clazz: Class<T>): T? {
    return try {
        Gson().fromJson(this, clazz)
    } catch (e: JsonSyntaxException) {
        null
    }
}

fun <T> String.toEntity(): T? {
    return try {
        Gson().fromJson(this, object : TypeToken<T>() {}.type)
    } catch (e: JsonSyntaxException) {
        null
    }
}

fun <T> T?.toJson(): String {
    return if (this == null) {
        ""
    } else {
        Gson().toJson(this)
    }
}