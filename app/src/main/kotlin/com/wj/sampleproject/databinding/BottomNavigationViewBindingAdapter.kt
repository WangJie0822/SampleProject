@file:Suppress("unused")

package com.wj.sampleproject.databinding

import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import cn.wj.android.common.ext.orTrue
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * BottomNavigationView DataBinding 适配器
 */

/**
 * 设置条目选中监听
 *
 * @param bnv [BottomNavigationView] 对象
 * @param itemSelected 条目选中回调
 */
@BindingAdapter("android:bind_bnv_onItemSelected", "android:bind_bnv_selectedIdAttrChanged", requireAll = false)
fun setOnNavigationItemSelectedListener(bnv: BottomNavigationView,
                                        itemSelected: ((Int) -> Boolean)?,
                                        listener: InverseBindingListener?) {
    bnv.setOnNavigationItemSelectedListener {
        if (bnv.selectedItemId != it.itemId) {
            listener?.onChange()
        }
        itemSelected?.invoke(it.itemId).orTrue()
    }
}

/**
 * 设置选中状态
 *
 * @param bnv [BottomNavigationView] 对象
 * @param selectedId 选中 item 的 id
 */
@BindingAdapter("android:bind_bnv_selectedId")
fun setNavigationSelectedId(bnv: BottomNavigationView, @IdRes selectedId: Int) {
    bnv.selectedItemId = selectedId
}

/**
 * 获取选中 id
 *
 * @param bnv [BottomNavigationView] 对象
 *
 * @return 选中 item 的 id
 */
@InverseBindingAdapter(attribute = "android:bind_bnv_selectedId")
fun getNavigationSelectedId(bnv: BottomNavigationView): Int {
    return bnv.selectedItemId
}