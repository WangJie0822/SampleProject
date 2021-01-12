@file:Suppress("unused")
@file:JvmName("RegexTools")

package cn.wj.common.ext

import java.util.regex.Pattern

/* ----------------------------------------------------------------------------------------- */
/* |                                        正则相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 判断字符序列是否满足简单的密码格式
 * > 6-16位，大小写字符、数字、下划线、减号
 */
fun CharSequence?.isPwdSimple(): Boolean {
    return isMatch(regex = "^[\\w_-]{6,16}\$")
}

/**
 * 判断字符序列是否不满足简单的密码格式
 * > 6-16位，大小写字符、数字、下划线、减号
 */
fun CharSequence?.isNotPwdSimple(): Boolean {
    return !isPwdSimple()
}

/**
 * 判断字符序列是否满足简单的手机号码格式
 * > 首位数字为 1，共 11 位
 *
 * @return 满足简单的手机号码格式：true 不满足简单的手机号码格式：false
 */
fun CharSequence?.isMobileSimple(): Boolean {
    return isMatch(regex = "^[1]+\\d{10}")
}

/**
 * 判断字符序列是否不满足简单的手机号码格式
 * > 首位数字为 1，共 11 位
 */
fun CharSequence?.isNotMobileSimple(): Boolean {
    return !isMobileSimple()
}

/**
 * 判断字符序列是否满足精准的手机号码格式
 * > 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198
 * > 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186
 * > 133, 153, 173, 177, 180, 181, 189, 199
 * > 1349
 * > 170
 */
fun CharSequence?.isMobileExact(): Boolean {
    return isMatch(regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}\$")
}

/**
 * 判断字符序列是否不满足精准的手机号码格式
 * > 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198
 * > 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186
 * > 133, 153, 173, 177, 180, 181, 189, 199
 * > 1349
 * > 170
 */
fun CharSequence?.isNotMobileExact(): Boolean {
    return !isMobileExact()
}

/** 判断字符序列是否满足电子邮件格式 */
fun CharSequence?.isEmail(): Boolean {
    return isMatch(regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
}

/** 判断字符序列是否不满足电子邮件格式 */
fun CharSequence?.isNotEmail(): Boolean {
    return !isEmail()
}

/** 判断字符序列是否满足 Url 格式 */
fun CharSequence?.isUrl(): Boolean {
    return isMatch(regex = "[a-zA-z]+://[^\\s]*")
}

/** 判断字符序列是否不满足 Url 格式 */
fun CharSequence?.isNotUrl(): Boolean {
    return !isUrl()
}

/** 判断字符序列是否满足汉字格式 */
fun CharSequence?.isZH(): Boolean {
    return isMatch(regex = "^[\\u4e00-\\u9fa5]+$")
}

/** 判断字符序列是否不满足汉字格式 */
fun CharSequence?.isNotZH(): Boolean {
    return !isZH()
}

/** 判断字符序列是否满足银行卡号格式 */
fun CharSequence?.isBankCardNo(): Boolean {
    return isMatch(regex = "^([1-9]{1})(\\d{14}|\\d{15}|\\d{16}|\\d{17}|\\d{18})$")
}

/** 判断字符序列是否不满足银行卡号格式 */
fun CharSequence?.isNotBankCardNo(): Boolean {
    return !isBankCardNo()
}

/** 判断字符序列是否满足身份证号格式 */
fun CharSequence?.isIdCardNo(): Boolean {
    return isMatch(regex = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]\$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)")
}

/** 判断字符序列是否不满足身份证号格式 */
fun CharSequence?.isNotIdCardNo(): Boolean {
    return !isIdCardNo()
}

/** 判断字符序列是否匹配正则表达式[regex] */
fun CharSequence?.isMatch(regex: String): Boolean {
    return isNotNullAndEmpty() && Pattern.matches(regex, this)
}