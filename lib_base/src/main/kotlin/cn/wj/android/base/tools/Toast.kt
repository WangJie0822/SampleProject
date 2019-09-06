@file:Suppress("unused")

package cn.wj.android.base.tools

import android.widget.Toast
import androidx.annotation.StringRes
import cn.wj.android.base.utils.ToastUtil

/**
 * 弹 Toast
 *
 * @param str 提示字符串
 * @param duration 显示时长
 * @param single 是否使用单独的 Toast 对象
 */
fun toast(str: String, duration: Int = Toast.LENGTH_SHORT, single: Boolean = false) {
    ToastUtil.show(str, duration, single)
}

/**
 * 弹 Toast
 *
 * @param strResId 提示字符串资源 id
 * @param duration 显示时长
 * @param single 是否使用单独的 Toast 对象
 */
fun toast(@StringRes strResId: Int, duration: Int = Toast.LENGTH_SHORT, single: Boolean = false) {
    ToastUtil.show(strResId, duration, single)
}