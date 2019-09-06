@file:Suppress("unused")

package cn.wj.android.base.databinding

import androidx.databinding.ObservableField

/**
 * Databinding 数据绑定对象封装类
 * - 添加了数据变化监听
 */
open class BindingField<T>
/**
 * 构造方法
 *
 * @param value 默认值
 * @param onChange 数据变化监听
 */
constructor(value: T? = null, private var onChange: OnFieldChangeListener<T>? = null)
    : ObservableField<T>(value) {

    override fun set(value: T?) {
        super.set(value)
        onChange?.invoke(value)
    }
}

/**
 * 数据变化监听回调
 */
typealias OnFieldChangeListener<T> = (T?) -> Unit