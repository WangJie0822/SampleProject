@file:Suppress("unused")

package cn.wj.android.databinding.adapter

import android.text.method.TransformationMethod
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * EditText DataBinding 适配器
 */

/**
 * 设置 EditText 文本光标位置
 *
 * @param et [EditText] 对象
 * @param selection 光标位置
 * - 如果文本长度超过 10 会出异常，所以只设置10以及10一下
 */
@BindingAdapter("android:bind_selection")
fun setEditTextSelection(et: EditText, selection: Int?) {
    if (null == selection) {
        return
    }
    if (selection in 0..et.text.length) {
        et.setSelection(selection)
    }
}

/**
 * 设置 EditText 输入类型
 *
 * @param et [EditText] 对象
 * @param inputType 输入类型
 */
@BindingAdapter("android:bind_inputType")
fun setEditTextInputType(et: EditText, inputType: TransformationMethod?) {
    if (null == inputType) {
        return
    }
    et.transformationMethod = inputType
}

/**
 * 设置 EditText 软键盘事件监听
 */
@BindingAdapter("android:bind_et_onEditorAction")
fun setOnEditorAction(et: EditText, action: ((TextView, Int, KeyEvent?) -> Boolean)?) {
    et.setOnEditorActionListener { v, actionId, event ->
        action?.invoke(v, actionId, event) ?: false
    }
}