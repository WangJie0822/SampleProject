package com.wj.sampleproject.model

import androidx.annotation.DrawableRes
import cn.wj.android.base.tools.getString
import com.wj.sampleproject.R

/**
 * 占位显示数据 Model
 *
 * - 创建时间：2019/10/8
 *
 * @author 王杰
 */
class PlaceholderModel
/**
 * @param show 是否显示
 * @param imgResId 显示图片资源
 * @param tips 显示文本资源
 * @param onClick 点击回调
 */
constructor(
        var show: Boolean = true,
        @DrawableRes
        var imgResId: Int = R.drawable.app_vector_no_data_gray,
        var tips: String = R.string.app_no_data_try_refresh.getString(),
        var onClick: PlaceholderClick? = null
)

/** 点击接口 */
typealias PlaceholderClick = () -> Unit
