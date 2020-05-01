@file:Suppress("unused")
@file:JvmName("StringExt")

package cn.wj.android.common.ext

/**
 * 字符串非空类型转换
 * - 调用对象为 null，则从候选列表中获取
 * - 若调用对象为 null，且获选列表为空或都为 null，则返回空字符串
 *
 * @param str 候选字符串列表
 */
fun String?.orEmpty(vararg str: String?): String {
    if (str.isEmpty()) {
        // 没有参数
        return this ?: ""
    } else {
        // 有参数
        return if (this.isNullOrEmpty()) {
            var ret: String? = null
            for (s in str) {
                if (!s.isNullOrEmpty()) {
                    ret = s
                    break
                }
            }
            ret ?: ""
        } else {
            this
        }
    }
}