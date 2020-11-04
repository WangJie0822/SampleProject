@file:Suppress("unused")

package cn.wj.android.base.simple

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * 设置文本变化监听
 *
 * @see SimpleTextWatcher
 *
 * @author 王杰
 */
fun TextView.addTextChangedListener(init: SimpleTextWatcher.() -> Unit) {
    val watcher = SimpleTextWatcher()
    watcher.init()
    this.addTextChangedListener(watcher)
}

/**
 * EditText 文本变化监听简易实现
 */
open class SimpleTextWatcher : TextWatcher {

    private var beforeTextChanged: ((CharSequence, Int, Int, Int) -> Unit)? = null

    fun beforeTextChanged(beforeTextChanged: (CharSequence, Int, Int, Int) -> Unit) {
        this.beforeTextChanged = beforeTextChanged
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    private var onTextChanged: ((CharSequence, Int, Int, Int) -> Unit)? = null

    fun onTextChanged(onTextChanged: (CharSequence, Int, Int, Int) -> Unit) {
        this.onTextChanged = onTextChanged
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

    private var afterTextChanged: ((Editable) -> Unit)? = null

    fun afterTextChanged(afterTextChanged: (Editable) -> Unit) {
        this.afterTextChanged = afterTextChanged
    }

    override fun afterTextChanged(s: Editable) {
        afterTextChanged?.invoke(s)
    }
}