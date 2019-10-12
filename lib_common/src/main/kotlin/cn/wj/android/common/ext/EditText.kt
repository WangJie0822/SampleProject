@file:Suppress("unused")
@file:JvmName("EditTextExt")

package cn.wj.android.common.ext

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 显示软键盘
 */
fun EditText.showSoftKeyboard() {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.requestFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            this,
            InputMethodManager.SHOW_FORCED
    )
}

/**
 * 设置获取焦点是否弹起软键盘
 *
 * @param show 是否弹起
 */
fun EditText.showSoftInputOnFocus(show: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.showSoftInputOnFocus = show
    } else {
        try {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
            setShowSoftInputOnFocus.isAccessible = false
            setShowSoftInputOnFocus.invoke(this, show)
        } catch (e: Exception) {
            Log.e("Common_EditText ---->>", e.localizedMessage.orEmpty())
        }
    }
}