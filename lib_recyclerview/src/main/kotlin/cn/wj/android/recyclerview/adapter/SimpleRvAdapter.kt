package cn.wj.android.recyclerview.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import java.lang.reflect.ParameterizedType

/**
 * 简易 列表适配器
 * - 使用 Databinding
 * - 数据绑定对象名为 viewModel
 *
 * @author 王杰
 */
class SimpleRvAdapter<E>(override val layoutResID: Int)
    : BaseRvDBAdapter<
        SimpleRvAdapter.ViewHolder<E>,
        ViewDataBinding,
        Any,
        E>() {

    override fun getVHClass() = ((getActualTypeList()[0] as ParameterizedType).rawType) as Class<*>

    @Suppress("unused")
    class ViewHolder<E> : BaseRvDBViewHolder<ViewDataBinding, E> {

        constructor(view: View) : super(view)

        constructor(binding: ViewDataBinding) : super(binding)
    }
}