@file:Suppress("unused", "DEPRECATION")

package cn.wj.android.base.simple

import android.inputmethodservice.KeyboardView

/**
 * 设置软键盘监听
 *
 * @see SimpleKeyboardActionListener
 *
 * @author 王杰
 */
fun KeyboardView.setOnKeyboardActionListener(init: SimpleKeyboardActionListener.() -> Unit) {
    val listener = SimpleKeyboardActionListener()
    listener.init()
    this.setOnKeyboardActionListener(listener)
}

/**
 * 简易实现的软键盘监听类
 */
open class SimpleKeyboardActionListener : KeyboardView.OnKeyboardActionListener {

    private var onPress: ((Int) -> Unit)? = null

    fun onPress(onPress: (Int) -> Unit) {
        this.onPress = onPress
    }

    override fun onPress(primaryCode: Int) {
        onPress?.invoke(primaryCode)
    }

    private var onRelease: ((Int) -> Unit)? = null

    fun onRelease(onRelease: (Int) -> Unit) {
        this.onRelease = onRelease
    }

    override fun onRelease(primaryCode: Int) {
        onRelease?.invoke(primaryCode)
    }

    private var onKey: ((Int, IntArray?) -> Unit)? = null

    fun onKey(onKey: (Int, IntArray?) -> Unit) {
        this.onKey = onKey
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        onKey?.invoke(primaryCode, keyCodes)
    }

    private var onText: ((CharSequence?) -> Unit)? = null

    fun onText(onText: (CharSequence?) -> Unit) {
        this.onText = onText
    }

    override fun onText(text: CharSequence?) {
        onText?.invoke(text)
    }

    private var swipeLeft: (() -> Unit)? = null

    fun swipeLeft(swipeLeft: () -> Unit) {
        this.swipeLeft = swipeLeft
    }

    override fun swipeLeft() {
        swipeLeft?.invoke()
    }

    private var swipeRight: (() -> Unit)? = null

    fun swipeRight(swipeRight: () -> Unit) {
        this.swipeRight = swipeRight
    }

    override fun swipeRight() {
        swipeRight?.invoke()
    }

    private var swipeDown: (() -> Unit)? = null

    fun swipeDown(swipeDown: () -> Unit) {
        this.swipeDown = swipeDown
    }

    override fun swipeDown() {
        swipeDown?.invoke()
    }

    private var swipeUp: (() -> Unit)? = null

    fun swipeUp(swipeUp: () -> Unit) {
        this.swipeUp = swipeUp
    }

    override fun swipeUp() {
        swipeUp?.invoke()
    }
}