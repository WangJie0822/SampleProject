package com.wj.sampleproject.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.model.UiCloseModel

/**
 * 生物认证弹窗 ViewModel
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
class BiometricViewModel
    : BaseViewModel() {

    /** 取消数据 */
    val cancelData = MutableLiveData<Int>()

    /** 弹窗标题 */
    val title = ObservableField<String>()

    /** 弹窗副标题 */
    val subTitle = ObservableField<String>()

    /** 默认提示文本 */
    val hint = ObservableField<String>()

    /** 取消按钮文本 */
    val negative = ObservableField<String>()

    /** 取消点击 */
    val onCancelClick: () -> Unit = {
        cancelData.value = 0
        uiCloseData.value = UiCloseModel()
    }
}