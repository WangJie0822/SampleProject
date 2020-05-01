package com.wj.sampleproject.model

/**
 * 进度条弹窗控制 Model
 *
 * @param show 是否显示
 * @param cancelable 能否取消
 *
 * - 创建时间：2019/10/14
 *
 * @author 王杰
 */
data class ProgressModel(
        var show: Boolean = true,
        var cancelable: Boolean = true
)