package com.wj.sampleproject.viewmodel

import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.model.UiCloseModel

/**
 * 学习相关界面 ViewModel
 *
 * - 创建时间：2020/12/8
 *
 * @author 王杰
 */
class StudyViewModel : BaseViewModel() {

    /** 返回点击 */
    val onBackClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }
}