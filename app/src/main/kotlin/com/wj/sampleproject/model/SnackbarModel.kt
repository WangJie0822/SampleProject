package com.wj.sampleproject.model

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import cn.wj.android.base.ext.color
import cn.wj.android.base.ext.string
import com.google.android.material.snackbar.Snackbar
import com.wj.sampleproject.R

/**
 * [Snackbar] 显示数据实体类
 *
 * @param content 显示文本
 * @param contentBgColor 显示背景颜色
 * @param contentColor 显示文字颜色
 * @param duration 显示时长
 * @param actionText 按钮文本
 * @param actionColor 按钮文本颜色
 * @param onAction 按钮点击事件
 * @param onCallback Snackbar 回调
 */
class SnackbarModel(
        val content: String? = "",
        @ColorInt val contentBgColor: Int = R.color.app_colorPrimaryDark.color,
        @ColorInt val contentColor: Int = R.color.app_white.color,
        val duration: Int = Snackbar.LENGTH_SHORT,
        val actionText: String? = null,
        @ColorInt val actionColor: Int = R.color.app_colorAccent.color,
        val onAction: View.OnClickListener? = null,
        val onCallback: Snackbar.Callback? = null
) {

    /**
     * 次构造方法
     *
     * @param resId 显示文本资源 id
     */
    constructor(@StringRes resId: Int) : this(resId.string)
}