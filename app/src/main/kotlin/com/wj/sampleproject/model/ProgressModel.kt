package com.wj.sampleproject.model

/**
 * 进度条弹窗控制 Model
 * <p/>
 * 创建时间：2019/10/14
 *
 * @author 王杰
 */
data class ProgressModel
/**
 * 主构造函数
 *
 * @param show 是否显示
 * @param cancelable 能否取消
 */
constructor(
        var show: Boolean = true,
        var cancelable: Boolean = true
)