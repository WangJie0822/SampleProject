@file:Suppress("unused")

package cn.wj.android.base.tools

import cn.wj.android.base.ext.isNotNullAndEmpty
import java.util.regex.Pattern

/* ----------------------------------------------------------------------------------------- */
/* |                                        正则相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 判断字符序列是否满足简单的密码格式
 * - 6-16位，大小写字符、数字、下划线、减号
 */
fun CharSequence?.isPwdSimple(): Boolean {
    return isMatch(regex = "^[\\w_-]{6,16}\$")
}

/**
 * 判断字符序列是否不满足简单的密码格式
 * - 6-16位，大小写字符、数字、下划线、减号
 */
fun CharSequence?.isNotPwdSimple(): Boolean {
    return !isPwdSimple()
}

/**
 * 判断字符序列是否满足简单的手机号码格式
 * - 首位数字为 1，共 11 位
 *
 * @return 满足简单的手机号码格式：true 不满足简单的手机号码格式：false
 */
fun CharSequence?.isMobileSimple(): Boolean {
    return isMatch(regex = "^[1]+\\d{10}")
}

/**
 * 判断字符序列是否不满足简单的手机号码格式
 * - 首位数字为 1，共 11 位
 *
 * @return 不满足简单的手机号码格式：true 满足简单的手机号码格式：false
 */
fun CharSequence?.isNotMobileSimple(): Boolean {
    return !isMobileSimple()
}

/**
 * 判断字符序列是否满足精准的手机号码格式
 * - 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198
 * - 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186
 * - 133, 153, 173, 177, 180, 181, 189, 199
 * - 1349
 * - 170
 *
 * @return 满足手机号码格式：true 不满足手机号码格式：false
 */
fun CharSequence?.isMobileExact(): Boolean {
    return isMatch(regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}\$")
}

/**
 * 判断字符序列是否不满足精准的手机号码格式
 * - 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198
 * - 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186
 * - 133, 153, 173, 177, 180, 181, 189, 199
 * - 1349
 * - 170
 *
 * @return 不满足手机号码格式：true 满足手机号码格式：false
 */
fun CharSequence?.isNotMobileExact(): Boolean {
    return !isMobileExact()
}

/**
 * 判断字符序列是否满足电子邮件格式
 *
 * @return 满足电子邮件格式：true 不满足电子邮件格式：false
 */
fun CharSequence?.isEmail(): Boolean {
    return isMatch(regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
}

/**
 * 判断字符序列是否不满足电子邮件格式
 *
 * @return 不满足电子邮件格式：true 满足电子邮件格式：false
 */
fun CharSequence?.isNotEmail(): Boolean {
    return !isEmail()
}

/**
 * 判断字符序列是否满足 Url 格式
 *
 * @return 满足 Url 格式：true 不满足 Url 格式：false
 */
fun CharSequence?.isUrl(): Boolean {
    return isMatch(regex = "[a-zA-z]+://[^\\s]*")
}

/**
 * 判断字符序列是否不满足 Url 格式
 *
 * @return 不满足 Url 格式：true 满足 Url 格式：false
 */
fun CharSequence?.isNotUrl(): Boolean {
    return !isUrl()
}

/**
 * 判断字符序列是否满足汉字格式
 *
 * @return 满足汉字格式：true 不满足汉字格式：false
 */
fun CharSequence?.isZH(): Boolean {
    return isMatch(regex = "^[\\u4e00-\\u9fa5]+$")
}

/**
 * 判断字符序列是否不满足汉字格式
 *
 * @return 不满足汉字格式：true 满足汉字格式：false
 */
fun CharSequence?.isNotZH(): Boolean {
    return !isZH()
}

/**
 * 判断字符序列是否匹配正则表达式
 *
 * @param regex 正则表达式
 *
 * @return 匹配正则表达式：true 不匹配正则表达式：false
 */
fun CharSequence?.isMatch(regex: String): Boolean {
    return isNotNullAndEmpty() && Pattern.matches(regex, this)
}