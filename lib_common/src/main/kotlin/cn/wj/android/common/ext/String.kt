@file:Suppress("unused")
@file:JvmName("StringExt")

package cn.wj.android.common.ext

/**
 * 字符串非空类型转换
 * - 调用对象为 null，则从候选列表中获取
 * - 若调用对象为 null，且获选列表为空或都为 null，则返回空字符串
 *
 * @param strs 候选字符串列表
 */
fun String?.orEmpty(vararg strs: String?): String {
    if (null != this) {
        return this
    }
    return strs.firstOrNull {
        null != it
    } ?: ""
}