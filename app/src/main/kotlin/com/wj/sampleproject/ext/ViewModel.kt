package com.wj.sampleproject.ext

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.net.NetResult

/**
 * ViewModel 相关
 *
 * - 创建时间：2020/12/31
 *
 * @author 王杰
 */

/** 默认提示异常的方法块 */
val BaseViewModel.defaultFaildBlock: NetResult<*>.() -> Unit
    get() = {
        snackbarData.value = toSnackbarModel()
    }