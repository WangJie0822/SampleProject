package cn.wj.android.swipeback.constants

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

/**
 * 侧滑返回配置
 *
 * - 创建时间：2020/1/3
 *
 * @author 王杰
 */
object SwipeBackConfig {

    /** 最大移动距离 */
    var moveMax = 300f
    /** 滑动系数 */
    var slippageFactor = 0.5f

    /** 滑动感知距离 0~1 */
    var sensorPercent = 0.05

    /** 背景颜色 */
    @ColorInt
    var bgColor: Int = Color.parseColor("#000000")

    /** 圆的内部图片 */
    var content: Drawable? = null
    /** 圆的半径 */
    var circleRadius = 70f
    /** 圆的内部图片内边距 */
    var contentMargin = 10

    /** 可拖拽宽度 */
    var dragWidth = 120
    /** 目标高度 */
    var targetHeight = 600
    /** 角度变换 0-135 */
    var tangentAngle = 45

    /** 重心点最终高度，决定控制点的 X 坐标 */
    var targetGravityWidth = 20
}