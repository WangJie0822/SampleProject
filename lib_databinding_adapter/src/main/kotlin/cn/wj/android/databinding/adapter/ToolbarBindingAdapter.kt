@file:Suppress("unused")

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

/**
 * 设置 Menu 点击事件
 */
@BindingAdapter("android:bind_toolbar_navigationClick")
fun setToolbarNavigationClick(toolbar: Toolbar, click: () -> Unit) {
    toolbar.setNavigationOnClickListener { click.invoke() }
}

/**
 * 设置 Toolbar 标题
 */
@BindingAdapter("android:bind_toolbar_title")
fun setToolbarTitle(toolbar: Toolbar, title: String) {
    toolbar.setTitle(title)
}