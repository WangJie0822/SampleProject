@file:Suppress("unused")

package cn.wj.android.base.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import cn.wj.android.base.widget.SideBarView
import cn.wj.android.base.widget.SwitchButton

/**
 * 自定义 View DataBinding 适配器
 * - 一些自定义控件的 DataBinding 适配器
 */

/**
 * 选择 [SwitchButton] 状态监听
 *
 * ```
 * .kt
 * class ViewModel {
 *     val onCheck: (Boolean) -> Unit = {
 *         // do something
 *     }
 * }
 *
 * .xml
 * <layout>
 *     <data>
 *         <variable name="viewModel" type="xxx.xxx.ViewModel"
 *     </data>
 *
 *     <cn.wj.android.base.widget.SwitchButton
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         **android:bind_sb_onCheck="@{viewModel.onCheck}"** />
 * </layout>
 *
 * ```
 *
 * @param sb [SwitchButton] 对象
 * @param check 选择回调
 */
@BindingAdapter("android:bind_sb_onCheck")
fun setSwitchButtonOnCheckChange(sb: SwitchButton, check: ((Boolean) -> Unit)) {
    sb.setOnCheckedChangeListener { _, isChecked -> check.invoke(isChecked) }
}

/**
 * 设置是否选中
 *
 * @param sb [SwitchButton] 对象
 * @param check 是否选中
 */
@BindingAdapter("android:bind_sb_check")
fun setSwitchButtonCheck(sb: SwitchButton, check: Boolean) {
    sb.isChecked = check
}

/**
 * 获取是否选中
 *
 * @param sb [SwitchButton] 对象
 *
 * @return 是否选中
 */
@InverseBindingAdapter(attribute = "android:bind_sb_check")
fun getSwitchButtonCheck(sb: SwitchButton): Boolean {
    return sb.isChecked
}

/**
 * 设置是否选中
 *
 * @param sb [SwitchButton] 对象
 * @param check 是否选中
 */
@BindingAdapter("android:bind_sb_checkAttrChanged")
fun setSwitchButtonCheckChange(sb: SwitchButton, check: InverseBindingListener) {
    sb.setOnCheckedChangeListener { _, _ -> check.onChange() }
}

/**
 * 设置侧边栏选中状态监听
 *
 * @param sbv [SideBarView] 对象
 * @param selected 选中回调
 * @param changed 变更回调
 * @param released 释放回调
 */
@BindingAdapter(
        "android:bind_sbv_selected",
        "android:bind_sbv_changed",
        "android:bind_sbv_released",
        requireAll = false
)
fun setSideBarSelectListener(
        sbv: SideBarView, selected: ((SideBarView, String) -> Unit)?,
        changed: ((SideBarView, String) -> Unit)?, released: ((SideBarView, String) -> Unit)?
) {
    sbv.setOnLetterSelectListener(object : SideBarView.LetterSelectListener {
        override fun onLetterSelected(letter: String) {
            selected?.invoke(sbv, letter)
        }

        override fun onLetterChanged(letter: String) {
            changed?.invoke(sbv, letter)
        }

        override fun onLetterReleased(letter: String) {
            released?.invoke(sbv, letter)
        }
    })
}