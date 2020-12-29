package cn.wj.json

import cn.wj.json.internal.Util
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class JSON {

    val factory = object : JsonAdapter.Factory {
        override fun create(type: KClass<*>, annotations: Set<Annotation>, json: JSON): JsonAdapter<*>? {

            return null
        }
    }
}