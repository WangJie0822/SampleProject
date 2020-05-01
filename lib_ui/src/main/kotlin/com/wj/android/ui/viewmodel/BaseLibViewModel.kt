@file:Suppress("unused")

package com.wj.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import cn.wj.android.base.log.InternalLog

/**
 * MVVM ViewModel 基类
 *
 * - 继承 [ViewModel]
 *
 * @author 王杰
 */
abstract class BaseLibViewModel
    : ViewModel() {
    
    override fun onCleared() {
        InternalLog.i("BaseLibViewModel", "View onCleared ----> ViewModel: $this")
    }
}