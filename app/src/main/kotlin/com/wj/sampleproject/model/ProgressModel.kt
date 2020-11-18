package com.wj.sampleproject.model

/**
 * 进度条弹窗控制 Model
 *
 * @param cancelable 能否取消
 * @param hint 提示文本
 *
 * - 创建时间：2019/10/14
 *
 * @author 王杰
 */
data class ProgressModel(
        val cancelable: Boolean = true,
        val hint: String = ""
)