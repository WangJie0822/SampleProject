package com.wj.sampleproject.ext

import com.wj.sampleproject.model.SnackbarModel

fun String?.toSnackbarMsg(): SnackbarModel {
    return SnackbarModel(this.orEmpty())
}