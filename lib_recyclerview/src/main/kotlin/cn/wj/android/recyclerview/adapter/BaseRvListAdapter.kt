@file:Suppress("unused")

package cn.wj.android.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
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
abstract class BaseRvListAdapter<VH : BaseRvViewHolder<E>, E>
    : RecyclerView.Adapter<BaseRvViewHolder<E>> {

    /** Adapter 绑定的 RecyclerView 对象 */
    var mRecyclerView: RecyclerView? = null

    /** 数据集合 */
    val mDiffer: AsyncListDiffer<E>

    private val mListener = AsyncListDiffer.ListListener<E> { previousList, currentList -> this.onCurrentListChanged(previousList, currentList) }

    /**
     * 构造方法
     *
     * @param diffCallback 数据是否相同回调
     * @param anim 是否使用动画
     */
    constructor(diffCallback: DiffUtil.ItemCallback<E>, anim: Boolean = false) {
        val listUpdateCallback = if (anim) {
            AdapterListUpdateCallback(this)
        } else {
            AdapterListUpdateNoAnimCallback(this)
        }
        mDiffer = AsyncListDiffer<E>(listUpdateCallback,
                AsyncDifferConfig.Builder<E>(diffCallback).build())
        mDiffer.addListListener(mListener)
    }

    /**
     * 构造方法
     *
     * @param config Differ config
     * @param anim 是否使用动画
     */
    constructor(config: AsyncDifferConfig<E>, anim: Boolean = false) {
        val listUpdateCallback = if (anim) {
            AdapterListUpdateCallback(this)
        } else {
            AdapterListUpdateNoAnimCallback(this)
        }
        mDiffer = AsyncListDiffer<E>(listUpdateCallback, config)
        mDiffer.addListListener(mListener)
    }

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
                mDiffer.currentList.size + 2
            } else {
                // 无脚布局
                mDiffer.currentList.size + 1
            }
        } else {
            // 无头布局
            if (haveFooter()) {
                // 有脚布局
                mDiffer.currentList.size + 1
            } else {
                // 无脚布局
                mDiffer.currentList.size
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder<E> {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                // 头布局
                mHeaderLayout.removeParent()
                createViewHolder(mHeaderLayout!!)
            }
            VIEW_TYPE_FOOTER -> {
                // 脚布局
                mFooterLayout.removeParent()
                createViewHolder(mFooterLayout!!)
            }
            VIEW_TYPE_EMPTY -> {
                // 空布局
                mEmptyLayout.removeParent()
                createViewHolder(mEmptyLayout!!)
            }
            else -> {
                customCreateViewHolder(parent, viewType)
            }
        }
    }

    /**
     * 移除父布局
     */
    internal fun View?.removeParent() {
        (this?.parent as? ViewGroup)?.removeView(this)
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
        if (position >= mDiffer.currentList.size) {
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
    protected fun isShowEmpty() = mEmptyLayout != null && mEmptyLayout!!.childCount > 0 && mDiffer.currentList.size <= 0

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
            val empty = LayoutInflater.from(parent.context).inflate(layoutResID, parent, false)
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
            mEmptyLayout = createEmptyLayout(emptyView.context)
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

    protected fun createEmptyLayout(context: Context): FrameLayout {
        return FrameLayout(context).apply {
            setOnClickListener {
                mEmptyClickListener?.invoke()
            }
            // 设置布局参数
            val tempLayoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT
            )
            layoutParams = tempLayoutParams
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
            mHeaderLayout = createHeaderOrFooterLayout(header.context, orientation)
        }
        var contains = false
        for (i in 0 until mHeaderLayout!!.childCount) {
            val childAt = mHeaderLayout!!.getChildAt(i)
            if (header.like(childAt)) {
                contains = true
                break
            }
        }
        if (contains) {
            return -1
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

    fun View.like(v: View): Boolean {
        return this.javaClass == v.javaClass
                && this.id == v.id
                && (this as? ViewGroup)?.childCount == (v as? ViewGroup)?.childCount
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
            mFooterLayout = createHeaderOrFooterLayout(footer.context, orientation)
        }
        var contains = false
        for (i in 0 until mFooterLayout!!.childCount) {
            val childAt = mFooterLayout!!.getChildAt(i)
            if (footer.like(childAt)) {
                contains = true
                break
            }
        }
        if (contains) {
            return -1
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

    /**
     * 创建头布局、脚布局对象
     *
     * @param context [Context] 对象
     * @param orientation 排列方式 [LinearLayout.VERTICAL]、[LinearLayout.HORIZONTAL] 默认竖排
     *
     * @return 头布局对象
     */
    protected fun createHeaderOrFooterLayout(context: Context, orientation: Int = LinearLayout.VERTICAL): LinearLayout {
        return LinearLayout(context).apply {
            if (orientation == LinearLayout.VERTICAL) {
                this.orientation = LinearLayout.VERTICAL
                this.layoutParams =
                        RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                this.orientation = LinearLayout.HORIZONTAL
                this.layoutParams =
                        RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
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
    protected fun getItem(position: Int) = mDiffer.currentList[position]

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

    open fun onCurrentListChanged(previousList: List<E>, currentList: List<E>) {}

    internal fun fixPosition(position: Int): Int {
        return if (haveHeader()) {
            position + 1
        } else {
            position
        }
    }

    fun submitList(list: List<E>?) {
        mDiffer.submitList(list)
    }

    fun submitList(list: List<E>?, commitCallback: Runnable?) {
        mDiffer.submitList(list, commitCallback)
    }
}