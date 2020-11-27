@file:Suppress("unused")

package com.wj.sampleproject.simple

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

/**
 * 设置 ViewPager2 Fragment 适配器
 *
 * @author 王杰
 */

fun ViewPager2.setFragmentAdapter(fragment: Fragment, init: SimpleFragmentStateAdapter.() -> Unit) {
    val adapter = SimpleFragmentStateAdapter(fragment)
    adapter.init()
    this.adapter = adapter
}

fun ViewPager2.setFragmentAdapter(activity: FragmentActivity, init: SimpleFragmentStateAdapter.() -> Unit) {
    val adapter = SimpleFragmentStateAdapter(activity)
    adapter.init()
    this.adapter = adapter
}

fun ViewPager2.setFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, init: SimpleFragmentStateAdapter.() -> Unit) {
    val adapter = SimpleFragmentStateAdapter(fragmentManager, lifecycle)
    adapter.init()
    this.adapter = adapter
}

/**
 * EditText 文本变化监听简易实现
 */
open class SimpleFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)

    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager, activity.lifecycle)

    private var itemCount = 0

    private lateinit var createFragment: (Int) -> Fragment

    override fun getItemCount(): Int {
        return itemCount
    }

    fun count(count: Int) {
        itemCount = count
    }

    override fun createFragment(position: Int): Fragment {
        return createFragment.invoke(position)
    }

    fun createFragment(createFragment: (Int) -> Fragment) {
        this.createFragment = createFragment
    }

}