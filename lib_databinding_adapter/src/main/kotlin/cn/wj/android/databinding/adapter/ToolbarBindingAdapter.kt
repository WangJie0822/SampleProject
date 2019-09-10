package cn.wj.android.databinding.adapter

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

/**
 * 设置 Menu 点击事件
 */
@BindingAdapter("android:bind_toolbar_itemClick")
fun setToolbarMenuItemClick(toolbar: Toolbar, itemClick: (MenuItem) -> Boolean) {
    toolbar.setOnMenuItemClickListener(itemClick)
}