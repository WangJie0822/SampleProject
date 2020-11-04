@file:Suppress("unused")
@file:JvmName("CharSequenceExt")

package cn.wj.android.common.ext

/* ----------------------------------------------------------------------------------------- */
/* |                                      字符序列相关                                      | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 判断字符序列是否不为 null 且不是空格
 *
 * @return 是否不为 null 且不是空格
 */
fun CharSequence?.isNotNullAndBlank(): Boolean {
    return !isNullOrBlank()
}

/**
 * 判断字符序列是否不为 null 且不是空串
 *
 * @return 是否不为 null 且不是空串
 */
fun CharSequence?.isNotNullAndEmpty(): Boolean {
    return !isNullOrEmpty()
}

/**
 * 判断字符串中是否包含 Emoji
 *
 * @return 是否包含 Emoji
 */
fun CharSequence.containsEmoji(): Boolean {
    return (this.indices)
            .map { this[it].toInt() }
            .none {
                (it == 0x0) || (it == 0x9) || (it == 0xA) || (it == 0xD)
                        || (it in 0x20..0xD7FF)
                        || (it in 0xE000..0xFFFD)
                        || (it in 0x10000..0x10FFFF)
            }
}