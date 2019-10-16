package com.wj.sampleproject.model

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import cn.wj.android.base.tools.getColor
import cn.wj.android.base.tools.getString
import com.google.android.material.snackbar.Snackbar
import com.wj.sampleproject.R

/**
 * [Snackbar] 显示数据实体类
 */
class SnackbarModel
/**
 * 主构造方法
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
constructor(
        var content: String? = "",
        @ColorInt var contentBgColor: Int = R.color.app_colorPrimaryDark.getColor(),
        @ColorInt var contentColor: Int = R.color.app_white.getColor(),
        var duration: Int = Snackbar.LENGTH_SHORT,
        var actionText: String? = null,
        @ColorInt var actionColor: Int = R.color.app_colorAccent.getColor(),
        var onAction: View.OnClickListener? = null,
        var onCallback: Snackbar.Callback? = null
) {

    /**
     * 次构造方法
     *
     * @param resId 显示文本资源 id
     */
    constructor(@StringRes resId: Int) : this(resId.getString())
}