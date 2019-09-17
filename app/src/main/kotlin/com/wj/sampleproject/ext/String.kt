package com.wj.sampleproject.ext

import com.wj.sampleproject.base.SnackbarEntity

fun String?.toSnackbarMsg(): SnackbarEntity {
    return SnackbarEntity(this.orEmpty())
}