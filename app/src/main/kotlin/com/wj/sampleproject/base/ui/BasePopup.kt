package com.wj.sampleproject.base.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.wj.android.ui.popup.BaseLibPopupWindow

/**
 * PopupWindow 基类，封装了一些兼容性处理
 *
 * @author 王杰
 */
abstract class BasePopup<DB : ViewDataBinding>(context: AppCompatActivity) : BaseLibPopupWindow<DB>(context)