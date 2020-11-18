package com.wj.sampleproject.viewmodel

import android.view.Gravity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.ext.string
import cn.wj.android.common.ext.isNotNullAndBlank
import com.wj.sampleproject.R
import com.wj.sampleproject.base.viewmodel.BaseViewModel
import com.wj.sampleproject.model.UiCloseModel

/**
 * 通用弹窗 ViewModel
 *
 * * 创建时间：2019/9/28
 *
 * @author 王杰
 */
class GeneralViewModel : BaseViewModel() {

    /** 消极按钮点击  */
    val negativeClickData = MutableLiveData<Long>()

    /** 积极按钮点击  */
    val positiveClickData = MutableLiveData<Long>()

    /** 标题文本  */
    val titleStr: ObservableField<String> = ObservableField("")

    /** 标记 - 是否显示标题  */
    val showTitle: ObservableBoolean = object : ObservableBoolean(titleStr) {
        override fun get(): Boolean {
            return titleStr.get().isNotNullAndBlank()
        }
    }

    /** 内容文本  */
    val contentStr: ObservableField<String> = ObservableField("")

    /** 内容文本重心  */
    val contentGravity: ObservableInt = ObservableInt(Gravity.START or Gravity.CENTER_VERTICAL)

    /** 标记 - 是否显示选择器  */
    val showSelect: ObservableBoolean = ObservableBoolean(false)

    /** 标记 - 选择器是否选中  */
    val selected: ObservableBoolean = ObservableBoolean(false)

    /** 选择器文本 - 默认：不再提示  */
    val selectStr: ObservableField<String> = ObservableField(R.string.app_no_longer_tips.string)

    /** 标记 - 是否显示消极按钮  */
    val showNegativeButton: ObservableBoolean = ObservableBoolean(true)

    /** 消极按钮文本 - 默认：取消  */
    val negativeButtonStr: ObservableField<String> = ObservableField(R.string.app_cancel.string)

    /** 标记 - 是否显示积极按钮  */
    val showPositiveButton: ObservableBoolean = ObservableBoolean(true)

    /** 积极按钮文本 - 默认：确认  */
    val positiveButtonStr: ObservableField<String> = ObservableField(R.string.app_confirm.string)

    /** 关闭按钮点击  */
    val onCloseClick: () -> Unit = {
        uiCloseData.value = UiCloseModel()
    }

    /** 选择器点击  */
    val onSelectClick: () -> Unit = fun() {
        // 选择状态置反
        val oldSelected = selected.get()
        selected.set(!oldSelected)
    }

    /** 消极按钮点击  */
    val onNegativeClick: () -> Unit = {
        negativeClickData.value = System.currentTimeMillis()
    }

    /** 积极按钮点击  */
    val onPositiveClick: () -> Unit = {
        positiveClickData.value = System.currentTimeMillis()
    }
}
