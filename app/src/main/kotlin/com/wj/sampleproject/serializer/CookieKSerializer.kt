package com.wj.sampleproject.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import okhttp3.Cookie

/**
 * [Cookie] 序列化器
 *
 * - 创建时间：2021/1/5
 *
 * @author 王杰
 */
object CookieKSerializer : KSerializer<Cookie> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("okhttp3.Cookie") {
            element<String>("name")
            element<String>("value")
            element<Long>("expiresAt")
            element<String>("domain")
            element<String>("path")
            element<Boolean>("secure")
            element<Boolean>("httpOnly")
            element<Boolean>("persistent")
            element<Boolean>("hostOnly")
        }

    override fun serialize(encoder: Encoder, value: Cookie) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeStringElement(descriptor, 1, value.value)
            encodeLongElement(descriptor, 2, value.expiresAt)
            encodeStringElement(descriptor, 3, value.domain)
            encodeStringElement(descriptor, 4, value.path)
            encodeBooleanElement(descriptor, 5, value.secure)
            encodeBooleanElement(descriptor, 6, value.httpOnly)
            encodeBooleanElement(descriptor, 7, value.persistent)
            encodeBooleanElement(descriptor, 8, value.hostOnly)
        }
    }

    override fun deserialize(decoder: Decoder): Cookie {
        return decoder.decodeStructure(descriptor) {
            val builder = Cookie.Builder()
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> builder.name(decodeStringElement(descriptor, index))
                    1 -> builder.value(decodeStringElement(descriptor, index))
                    2 -> builder.expiresAt(decodeLongElement(descriptor, index))
                    3 -> builder.domain(decodeStringElement(descriptor, index))
                    4 -> builder.path(decodeStringElement(descriptor, index))
                    5 -> if (decodeBooleanElement(descriptor, index)) builder.secure()
                    6 -> if (decodeBooleanElement(descriptor, index)) builder.httpOnly()
                    7 -> decodeBooleanElement(descriptor, index)
                    8 -> if (decodeBooleanElement(descriptor, index)) builder.httpOnly()
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            builder.build()
        }
    }
}