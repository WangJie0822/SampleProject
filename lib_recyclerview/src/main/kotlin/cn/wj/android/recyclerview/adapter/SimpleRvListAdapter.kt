package cn.wj.android.recyclerview.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import java.lang.reflect.ParameterizedType

/**
 * 简易 列表适配器
 * - 使用 Databinding
 * - 数据绑定对象名为 viewModel
 *
 * @author 王杰
 */
class SimpleRvListAdapter<E>
/**
 * @param layoutResID 布局 id
 * @param areContentsTheSame 内容是否相同
 * @param anim 是否开启动画
 */
constructor(
        override val layoutResID: Int,
        areContentsTheSame: AreContentsTheSame<E> = { old, new -> old.toString() == new.toString() },
        anim: Boolean = false
) : BaseRvListDBAdapter<
        SimpleRvListAdapter.ViewHolder<E>,
        ViewDataBinding,
        Any,
        E>(
        object : DiffUtil.ItemCallback<E>() {
            override fun areItemsTheSame(oldItem: E, newItem: E): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: E, newItem: E): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }
        }, anim
) {

    override fun getVHClass() = ((getActualTypeList()[0] as ParameterizedType).rawType) as Class<*>

    @Suppress("unused")
    class ViewHolder<E> : BaseRvDBViewHolder<ViewDataBinding, E> {

        constructor(view: View) : super(view)

        constructor(binding: ViewDataBinding) : super(binding)
    }
}

typealias AreContentsTheSame<E> = (E, E) -> Boolean