@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package cn.wj.android.base.sample

import android.inputmethodservice.KeyboardView
import cn.wj.android.base.simple.setOnKeyboardActionListener

/**
 * [cn.wj.android.base.simple.setOnKeyboardActionListener] 示例
 *
 * @author 王杰
 */
internal class KeyboardActionListener {

    /**
     * 设置软键盘监听示例方法
     *
     * @param keyboard [KeyboardView] 对象
     */
    fun setOnKeyboardActionListener(keyboard: KeyboardView) {
        // 设置监听
        keyboard.setOnKeyboardActionListener {
            onText {
                // do something
            }

            onPress {
                // do something
            }

            onKey { primaryCode, keyCodes ->
                // do something
            }

            onRelease {
                // do something
            }

            swipeUp {
                // do something
            }

            swipeDown {
                // do something
            }

            swipeLeft {
                // do something
            }

            swipeRight {
                // do something
            }
        }
    }
}