@file:Suppress("unused")

package com.wj.sampleproject.tools

import android.os.Parcelable
import cn.wj.android.base.utils.AppManager
import com.tencent.mmkv.MMKV
import com.wj.sampleproject.constants.MMKV_ID_ENCRYPTION

/**
 * 数据缓存相关
 *
 * - 创建时间：2021/1/13
 *
 * @author 王杰
 */

/** 默认 [MMKV] 对象 */
private val defaultMMKV: MMKV by lazy {
    MMKV.initialize(AppManager.getContext())
    MMKV.defaultMMKV()
}

/** 单独用于安全数据的 [MMKV] */
val safeMMKV: MMKV by lazy {
    defaultMMKV.mmapID()
    MMKV.mmkvWithID(MMKV_ID_ENCRYPTION, MMKV.SINGLE_PROCESS_MODE, MMKV_ID_ENCRYPTION)
}

/** 用户定制的 [MMKV] 对象 */
private val localMMKV = ThreadLocal<MMKV>()

/** 使用用户定制 [mmkv] 进行数据操作，[block] 方法块内，使用的都是用户定制的 [mmkv] */
fun <T> withMMKV(mmkv: MMKV, block: () -> T): T {
    localMMKV.remove()
    localMMKV.set(mmkv)
    val result = block.invoke()
    localMMKV.remove()
    return result
}

/** 获取 [MMKV] 对象，优先 [localMMKV] */
fun getMMKV(): MMKV {
    return localMMKV.get() ?: defaultMMKV
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Int) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Long) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Float) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Double) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Boolean) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: String) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: ByteArray) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Parcelable) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 编码保存 [value] */
fun String.encode(value: Set<String>) {
    getMMKV().encode(this, value)
}

/** 使用 key [String] 解码获取 [Int] 值，没有默认 [defaultValue] */
fun String.decodeInt(defaultValue: Int = 0): Int {
    return getMMKV().decodeInt(this, defaultValue)
}

/** 使用 key [String] 解码获取 [Long] 值，没有默认 [defaultValue] */
fun String.decodeLong(defaultValue: Long = 0L): Long {
    return getMMKV().decodeLong(this, defaultValue)
}

/** 使用 key [String] 解码获取 [Float] 值，没有默认 [defaultValue] */
fun String.decodeFloat(defaultValue: Float = 0f): Float {
    return getMMKV().decodeFloat(this, defaultValue)
}

/** 使用 key [String] 解码获取 [Double] 值，没有默认 [defaultValue] */
fun String.decodeDouble(defaultValue: Double = 0.0): Double {
    return getMMKV().decodeDouble(this, defaultValue)
}

/** 使用 key [String] 解码获取 [Boolean] 值，没有默认 [defaultValue] */
fun String.decodeBoolean(defaultValue: Boolean = false): Boolean {
    return getMMKV().decodeBool(this, defaultValue)
}

/** 使用 key [String] 解码获取 [String] 值，没有默认 [defaultValue] */
fun String.decodeString(defaultValue: String = ""): String {
    return getMMKV().decodeString(this, defaultValue)
}

/** 使用 key [String] 解码获取 [ByteArray] 值，没有默认 [defaultValue] */
fun String.decodeByteArray(defaultValue: ByteArray? = null): ByteArray? {
    return getMMKV().decodeBytes(this, defaultValue)
}

/** 使用 key [String] 解码获取 [T] 值，没有默认 [defaultValue] */
inline fun <reified T : Parcelable> String.decodeParcelable(defaultValue: T? = null): T? {
    return getMMKV().decodeParcelable(this, T::class.java, defaultValue)
}

/** 使用 key [String] 解码获取 [Set]<[String]> 值，没有默认 [defaultValue] */
fun String.decodeStringSet(defaultValue: Set<String>? = null): Set<String>? {
    return getMMKV().decodeStringSet(this, defaultValue)
}