package cn.wj.android.base.utils

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Toast 管理工具
 */
object ToastUtil {

    /** Toast 单例对象 */
    private val mToast: Toast by lazy {
        Toast.makeText(AppManager.getApplication(), "", Toast.LENGTH_SHORT)
    }

    /**
     * 弹 Toast
     *
     * @param str 提示字符串
     * @param duration 显示时长
     * @param single 是否使用单独的 Toast 对象
     */
    @SuppressLint("ShowToast")
    fun show(str: String, duration: Int = Toast.LENGTH_SHORT, single: Boolean = false) {
        val toast = if (single) {
            // 单独的 Toast
            Toast.makeText(AppManager.getApplication(), str, duration)
        } else {
            // 通用的 Toast
            mToast.setText(str)
            mToast.duration = duration
            mToast
        }
        toast.show()
    }

    /**
     * 弹 Toast
     *
     * @param strResId 提示字符串资源 id
     * @param duration 显示时长
     * @param single 是否使用单独的 Toast 对象
     */
    @SuppressLint("ShowToast")
    fun show(@StringRes strResId: Int, duration: Int = Toast.LENGTH_SHORT, single: Boolean = false) {
        val toast = if (single) {
            // 单独的 Toast
            Toast.makeText(AppManager.getApplication(), strResId, duration)
        } else {
            // 通用的 Toast
            mToast.setText(strResId)
            mToast.duration = duration
            mToast
        }
        toast.show()
    }
}