package com.wj.sampleproject.ext

import com.wj.sampleproject.model.SnackbarModel

/** 根据 [String] 生成并返回 [SnackbarModel] */
fun String?.toSnackbarMsg(): SnackbarModel {
    return SnackbarModel(this.orEmpty())
}