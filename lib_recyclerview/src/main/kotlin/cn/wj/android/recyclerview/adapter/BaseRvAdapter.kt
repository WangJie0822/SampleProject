@file:Suppress("unused")

package cn.wj.android.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.wj.android.recyclerview.OnEmptyClickListener
import java.lang.reflect.ParameterizedType

/**
 * RecyclerView 适配器基类
 * - 若要使 头布局、脚布局 等必须在设置适配器之前设置布局管理器
 *
 * @param VH ViewHolder 类型，继承 [BaseRvViewHolder]
 * @param E  数据类型
 *
 * @author 王杰
 */
abstract class BaseRvAdapter<out VH : BaseRvViewHolder<E>, E>
    : RecyclerView.Adapter<BaseRvViewHolder<E>>() {

    companion object {
        /** 布局类型 - 头布局 */
        const val VIEW_TYPE_HEADER = 0x0101551
        /** 布局类型 - 正常 */
        const val VIEW_TYPE_NORMAL = 0x1011552
        /** 布局类型 - 脚布局 */
        const val VIEW_TYPE_FOOTER = 0x0101553
        /** 布局类型 - 空布局 */
        const val VIEW_TYPE_EMPTY = 0x0101554
    }

    /** Adapter 绑定的 RecyclerView 对象 */
    var mRecyclerView: RecyclerView? = null

    /** 数据集合  */
    val mData = arrayListOf<E>()

    /** 空布局 */
    protected var mEmptyLayout: FrameLayout? = null
    /** 头布局 */
    protected var mHeaderLayout: LinearLayout? = null
    /** 脚布局 */
    protected var mFooterLayout: LinearLayout? = null

    /** 空布局布局 id */
    protected var emptyViewLayoutResID: Int? = null

    /** 标记 - 显示空布局是是否显示头布局 默认不显示 */
    var showHeaderWhenEmpty = false
    /** 标记 - 显示空布局时是否显示脚布局 默认不显示 */
    var showFooterWhenEmpty = false

    /** 空布局点击事件监听 */
    private var mEmptyClickListener: OnEmptyClickListener? = null

    /**
     * 根据 View 下标获取当前 View 布局类型
     *
     * @param position View 下标
     * @return View 类型 [VIEW_TYPE_HEADER]、[VIEW_TYPE_NORMAL]、[VIEW_TYPE_FOOTER]
     */
    override fun getItemViewType(position: Int) = when {
        isEmpty(position) -> VIEW_TYPE_EMPTY     // 空布局
        isHeader(position) -> VIEW_TYPE_HEADER   // 头布局
        isFooter(position) -> VIEW_TYPE_FOOTER   // 脚布局
        else -> customViewType(if (haveHeader()) position - 1 else position) // 其他布局
    }

    /**
     * 除特殊布局外的其他布局，一种布局无需变化，多种布局重写
     */
    open fun customViewType(position: Int): Int {
        return VIEW_TYPE_NORMAL
    }

    override fun getItemCount() = if (isShowEmpty()) {
        // 显示空布局
        if (haveHeader() && showHeaderWhenEmpty) {
            // 有头布局，且显示
            if (haveFooter() && showFooterWhenEmpty) {
                // 有脚布局，且显示
                3
            } else {
                // 无脚布局或不显示
                2
            }
        } else {
            // 无头布局或不显示
            if (haveFooter() && showFooterWhenEmpty) {
                // 有脚布局，且显示
                2
            } else {
                // 无脚布局或不显示
                1
            }
        }
    } else {
        // 不显示空布局
        if (haveHeader()) {
            // 有头布局
            if (haveFooter()) {
                // 有脚布局
                mData.size + 2
            } else {
                // 无脚布局
                mData.size + 1
            }
        } else {
            // 无头布局
            if (haveFooter()) {
                // 有脚布局
                mData.size + 1
            } else {
                // 无脚布局
                mData.size
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder<E> {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                // 头布局
                createViewHolder(mHeaderLayout!!)
            }
            VIEW_TYPE_FOOTER -> {
                // 脚布局
                createViewHolder(mFooterLayout!!)
            }
            VIEW_TYPE_EMPTY -> {
                // 空布局
                createViewHolder(mEmptyLayout!!)
            }
            else -> {
                customCreateViewHolder(parent, viewType)
            }
        }
    }

    /**
     * 除特殊布局外的其他布局，一种布局无需变化，多种布局重写
     */
    abstract fun customCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder<E>

    /**
     * 除特殊布局外的其他布局，一种布局无需变化，多种布局重写
     */
    override fun onBindViewHolder(holder: BaseRvViewHolder<E>, position: Int) {
        if (needFix(position)) {
            // 头布局、脚布局、空布局，返回不做操作
            return
        }
        customBindViewHolder(holder, if (haveHeader()) position - 1 else position)
    }

    protected open fun customBindViewHolder(holder: BaseRvViewHolder<E>, position: Int) {
        // 普通布局，绑定数据
        if (position >= mData.size) {
            return
        }
        convert(holder, getItem(position))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        if (emptyViewLayoutResID != null) {
            setEmptyView(emptyViewLayoutResID!!)
        }
        val manager = mRecyclerView!!.layoutManager
        if (manager is GridLayoutManager) {
            // 如果是 Grid 布局
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                        if (needFix(position)) {
                            manager.spanCount
                        } else {
                            // 如果是头布局或者脚布局，独占一行
                            1
                        }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseRvViewHolder<E>) {
        val lp = holder.itemView.layoutParams
        lp?.let {
            if (it is StaggeredGridLayoutManager.LayoutParams) {
                // 是瀑布流式布局
                if (needFix(holder.layoutPosition)) {
                    // 是头布局或脚布局，独占一行
                    it.isFullSpan = true
                }
            }
        }
    }

    /**
     * 判断是否需要配置独占一行
     *
     * @param position View 位置
     *
     * @return 是否需要配置独占一行
     */
    private fun needFix(position: Int) =
            getItemViewType(position) in arrayOf(VIEW_TYPE_HEADER, VIEW_TYPE_FOOTER, VIEW_TYPE_EMPTY)

    /**
     * 判断是否显示空布局
     *
     * @return 是否显示空布局
     */
    protected fun isShowEmpty() = mEmptyLayout != null && mEmptyLayout!!.childCount > 0 && mData.size <= 0

    /**
     * 判断是否是空布局
     *
     * @param position View 位置
     *
     * @return 是否是空布局
     */
    protected fun isEmpty(position: Int) = if (isShowEmpty()) {
        // 显示空布局
        if (haveHeader() && showHeaderWhenEmpty) {
            // 有头布局且显示
            position == 1
        } else {
            position == 0
        }
    } else {
        // 不显示头布局
        false
    }

    /**
     * 设置空布局
     *
     * @param layoutResID 布局资源 id
     * @param parent [ViewGroup] 父控件，用于加载布局 **若使用默认 mRecyclerView 务必在 [RecyclerView]#setAdapter() 之后调用**
     */
    fun setEmptyView(@LayoutRes layoutResID: Int, parent: ViewGroup? = mRecyclerView) {
        emptyViewLayoutResID = if (parent != null) {
            // 已绑定 RecyclerView
            val empty = LayoutInflater.from(parent.context).inflate(layoutResID, null, false)
            setEmptyView(empty)
            null
        } else {
            layoutResID
        }
    }

    /**
     * 设置空布局
     *
     * @param emptyView 空布局
     */
    fun setEmptyView(emptyView: View) {
        // 标记，是否第一次创建
        var insert = false
        if (mEmptyLayout == null) {
            // 未初始化，初始化空布局
            mEmptyLayout = FrameLayout(emptyView.context)
            mEmptyLayout!!.setOnClickListener {
                mEmptyClickListener?.invoke()
            }
            // 设置布局参数
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT
            )
            val lp = emptyView.layoutParams
            if (lp != null) {
                layoutParams.width = lp.width
                layoutParams.height = lp.height
            }
            mEmptyLayout!!.layoutParams = layoutParams
            // 第一次创建
            insert = true
        }
        // 添加空布局
        mEmptyLayout!!.removeAllViews()
        mEmptyLayout!!.addView(emptyView)
        if (insert) {
            // 第一次创建
            if (isShowEmpty()) {
                // 显示空布局
                var position = 0
                if (haveHeader() && showHeaderWhenEmpty) {
                    // 有头布局并且显示
                    position++
                }
                notifyItemInserted(position)
            }
        }
    }

    /**
     * 添加头布局
     *
     * @param header 头布局
     * @param index 添加位置 默认从上往下
     * @param orientation 排列方式 [LinearLayout.VERTICAL]、[LinearLayout.HORIZONTAL] 默认竖排
     *
     * @return 插入的实际位置
     */
    fun addHeaderView(header: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (mHeaderLayout == null) {
            mHeaderLayout = LinearLayout(header.context)
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout!!.orientation = LinearLayout.VERTICAL
                mHeaderLayout!!.layoutParams =
                        RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                mHeaderLayout!!.orientation = LinearLayout.HORIZONTAL
                mHeaderLayout!!.layoutParams =
                        RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
        var i = index
        val childCount = mHeaderLayout!!.childCount
        if (index < 0 || index > childCount) {
            i = childCount
        }
        mHeaderLayout!!.addView(header, i)
        val position = getHeaderViewPosition()
        if (position != -1) {
            notifyItemInserted(position)
        }
        return i
    }

    /**
     * 添加头布局
     *
     * @param footer 脚布局
     * @param index 添加位置 默认从上往下
     * @param orientation 排列方式 [LinearLayout.VERTICAL]、[LinearLayout.HORIZONTAL] 默认竖排
     *
     * @return 插入的实际位置
     */
    fun addFooterView(footer: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (mFooterLayout == null) {
            mFooterLayout = LinearLayout(footer.context)
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout!!.orientation = LinearLayout.VERTICAL
                mFooterLayout!!.layoutParams =
                        RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                mFooterLayout!!.orientation = LinearLayout.HORIZONTAL
                mFooterLayout!!.layoutParams =
                        RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
        var i = index
        val childCount = mFooterLayout!!.childCount
        if (index < 0 || index > childCount) {
            i = childCount
        }
        mFooterLayout!!.addView(footer, i)
        val position = getFooterViewPosition()
        if (position != -1) {
            notifyItemInserted(position)
        }
        return i
    }

    fun clearFooterView() {
        mFooterLayout?.removeAllViews()
    }

    /**
     * 判断是否有头布局
     *
     * @return 是否有头布局
     */
    protected fun haveHeader() = mHeaderLayout != null && mHeaderLayout!!.childCount > 0

    /**
     * 根据下标判断是否是头布局
     *
     * @param position View 下标
     * @return 是否是头布局
     */
    protected fun isHeader(position: Int) = if (isShowEmpty()) {
        // 显示空布局
        if (haveHeader() && showHeaderWhenEmpty) {
            // 有头布局且显示
            position == 0
        } else {
            // 不显示头布局
            false
        }
    } else {
        // 不显示空布局
        haveHeader() && position == 0
    }

    /**
     * 获取头布局位置
     *
     * @return 头布局位置
     */
    protected fun getHeaderViewPosition() = if (haveHeader()) 0 else -1

    /**
     * 判断是否有脚布局
     *
     * @return 是否有脚布局
     */
    protected fun haveFooter() = mFooterLayout != null && mFooterLayout!!.childCount > 0

    /**
     * 根据下标判断是否是脚布局
     *
     * @param position View 下标
     * @return 是否是脚布局
     */
    protected fun isFooter(position: Int) = if (!haveFooter()) {
        // 没有脚布局或不显示
        false
    } else {
        // 有脚布局且显示
        if (isShowEmpty()) {
            // 显示空布局
            if (showFooterWhenEmpty) {
                // 显示脚布局
                position == itemCount - 1
            } else {
                false
            }
        } else {
            position == itemCount - 1
        }
    }

    /**
     * 获取脚布局位置
     *
     * @return 脚布局位置
     */
    protected fun getFooterViewPosition() = if (!haveFooter() || (isShowEmpty() && !showFooterWhenEmpty)) {
        // 不显示脚布局
        -1
    } else {
        // 显示脚布局
        itemCount - 1
    }

    /**
     * 根据下标获取当前布局对应的数据对象
     *
     * @param position View 下标
     * @return View 对应的数据对象
     */
    protected fun getItem(position: Int) = mData[position]

    /**
     * 绑定数据
     *
     * @param holder ViewHolder
     * @param entity   数据对象
     */
    protected open fun convert(holder: BaseRvViewHolder<E>, entity: E) {
        holder.bindData(entity)
    }

    /**
     * 创建ViewHolder 使用头布局时必须重写
     *
     * @param view View对象
     *
     * @return ViewHolder
     */
    protected open fun createViewHolder(view: View): BaseRvViewHolder<E> {
        @Suppress("UNCHECKED_CAST")
        val clazz = getVHClass() as Class<BaseRvViewHolder<E>>
        val constructor = clazz.getConstructor(View::class.java)
        return constructor.newInstance(view)
    }

    /**
     * 获取 ViewHolder 的类
     *
     * @return ViewHolder 实际类型
     */
    protected open fun getVHClass() = getActualTypeList()[0] as Class<*>


    /**
     * 获取泛型实际类型列表
     */
    protected fun getActualTypeList() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments

    /**
     * 设置空布局点击事件
     *
     * @param lis 空布局点击事件监听
     */
    fun setOnEmptyClickListener(lis: OnEmptyClickListener) {
        this.mEmptyClickListener = lis
    }

    /**
     * 获取数组中最大值
     *
     * @param numbers Int 数组
     *
     * @return 数组中最大值
     */
    private fun getTheBiggestNumber(numbers: IntArray): Int {
        var tmp = -1
        if (numbers.isEmpty()) {
            return tmp
        }
        for (num in numbers) {
            if (num > tmp) {
                tmp = num
            }
        }
        return tmp
    }

    /**
     * 刷新
     */
    fun refresh(ls: List<E>?) {
        if (ls.isNullOrEmpty()) {
            return
        }
        mData.clear()
        mData.addAll(ls)
        notifyDataSetChanged()
    }

    /**
     * 清空数据
     */
    fun clear() {
        mData.clear()
        notifyDataSetChanged()
    }

    /**
     * 加载数据
     *
     * @param ls 数据列表
     * @param refresh 是否刷新
     */
    fun loadData(ls: List<E>?, refresh: Boolean? = false) {
        if (ls.isNullOrEmpty()) {
            return
        }
        if (refresh == true) {
            mData.clear()
        }
        mData.addAll(ls)
        notifyDataSetChanged()
    }
}