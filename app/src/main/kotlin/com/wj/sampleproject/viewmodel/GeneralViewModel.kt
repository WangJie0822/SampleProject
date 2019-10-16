package com.wj.sampleproject.viewmodel

import android.text.TextUtils
import android.view.Gravity
import androidx.lifecycle.MutableLiveData
import cn.wj.android.base.databinding.BindingField
import cn.wj.android.base.tools.getString
import com.wj.sampleproject.R
import com.wj.sampleproject.base.mvvm.BaseViewModel
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

    /** 标记 - 是否显示标题  */
    val showTitle = BindingField(false)

    /** 标题文本  */
    // 文本为空时不显示标题
    val titleStr = BindingField("", { _, value -> showTitle.set(!TextUtils.isEmpty(value)) })

    /** 内容文本  */
    val contentStr = BindingField("")

    /** 内容文本重心  */
    val contentGravity = BindingField(Gravity.START or Gravity.CENTER_VERTICAL)

    /** 标记 - 是否显示选择器  */
    val showSelect = BindingField(false)

    /** 标记 - 选择器是否选中  */
    val selected = BindingField(false)

    /** 选择器文本 - 默认：不再提示  */
    val selectStr = BindingField(R.string.app_no_longer_tips.getString())

    /** 标记 - 是否显示消极按钮  */
    val showNegativeButton = BindingField(true)

    /** 消极按钮文本 - 默认：取消  */
    val negativeButtonStr = BindingField(R.string.app_cancel.getString())

    /** 标记 - 是否显示积极按钮  */
    val showPositiveButton = BindingField(true)

    /** 积极按钮文本 - 默认：确认  */
    val positiveButtonStr = BindingField(R.string.app_confirm.getString())

    /** 关闭按钮点击  */
    val onCloseClick = { uiCloseData.postValue(UiCloseModel()) }

    /** 选择器点击  */
    val onSelectClick = fun() {
        // 选择状态置反
        val oldSelected = selected.get()
        if (null == oldSelected) {
            selected.set(true)
            return
        }
        selected.set(!oldSelected)
    }

    /** 消极按钮点击  */
    val onNegativeClick = { negativeClickData.postValue(System.currentTimeMillis()) }

    /** 积极按钮点击  */
    val onPositiveClick = { positiveClickData.postValue(System.currentTimeMillis()) }
}
