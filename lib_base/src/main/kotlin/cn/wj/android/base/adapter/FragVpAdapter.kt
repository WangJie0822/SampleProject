@file:Suppress("unused")

package cn.wj.android.base.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * [Fragment] [ViewPager] 适配器类
 *
 * @param fm [FragmentManager] 对象
 * @param behavior [Fragment] 生命周期行为
 *
 * @author 王杰
 */
class FragVpAdapter private constructor(
        fm: FragmentManager,
        behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    : FragmentStatePagerAdapter(fm, behavior) {

    companion object {

        /**
         * 新建建造者对象
         *
         * @return [Builder] 对象
         */
        @JvmStatic
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    /** 保存 [Fragment] 的集合 */
    private val mFragsMap = hashMapOf<Int, Fragment>()

    /** 动态创建接口 */
    lateinit var mCreator: Creator

    /** 获取标题接口 */
    private var pageTitleListener: OnPageTitleListener? = null

    override fun getCount() = mCreator.count

    override fun getPageTitle(position: Int) = pageTitleListener?.invoke(getItem(position), position)

    override fun getItem(position: Int) = if (mFragsMap.containsKey(position)) {
        var frag = mFragsMap[position]
        if (frag == null) {
            frag = mCreator.createFragment(position)
            mFragsMap[position] = frag
        }
        frag
    } else {
        val frag = mCreator.createFragment(position)
        mFragsMap[position] = frag
        frag
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        mFragsMap[position] = fragment
        return fragment
    }

    /**
     * FragVpAdapter 建造者类
     */
    class Builder internal constructor() {

        /** Fragment 管理器 */
        private lateinit var fm: FragmentManager

        /** behavior */
        private var behavior = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT

        /** 获取标题 */
        private var pageTitleListener: OnPageTitleListener? = null

        /** 动态创建接口 */
        private var creator: Creator? = null

        /**
         * 设置动态创建接口
         *
         * @param creator 动态创建接口对象
         *
         * @return [Builder] 对象
         *
         * @see Creator
         */
        fun creator(creator: Creator): Builder {
            this.creator = creator
            return this
        }

        /**
         * 绑定 [Fragment] 管理器
         *
         * @param fm [Fragment] 管理器 [FragmentManager]
         *
         * @return [Builder] 对象
         *
         * @see FragmentManager
         */
        fun manager(fm: FragmentManager): Builder {
            this.fm = fm
            return this
        }

        /**
         * 设置 [Fragment] 生命周期行为
         *
         * @param behavior 行为参数
         *
         * @return [Builder] 对象
         *
         * @see FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
         * @see FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
         */
        fun behavior(behavior: Int): Builder {
            this.behavior = behavior
            return this
        }

        /**
         * 设置获取标题回调
         *
         * @param listener 获取标题回调
         *
         * @return [Builder] 对象
         *
         * @see OnPageTitleListener
         */
        fun pageTitle(listener: OnPageTitleListener): Builder {
            this.pageTitleListener = listener
            return this
        }

        /**
         * 生成 [FragVpAdapter] 对象
         *
         * @return [FragVpAdapter] 对象
         */
        fun build(): FragVpAdapter {
            val adapter = FragVpAdapter(fm, behavior)
            adapter.mCreator = creator
                    ?: throw RuntimeException("Fragment Creator must not be null!")
            adapter.pageTitleListener = this.pageTitleListener
            return adapter
        }
    }

    /**
     * 动态创建接口
     */
    interface Creator {

        /** [Fragment] 总数 */
        val count: Int

        /**
         * 创建 [Fragment] 对象
         *
         * @param position 下标
         *
         * @return [Fragment] 对象
         */
        fun createFragment(position: Int): Fragment
    }
}

/**
 * 一个便捷的简易 [FragVpAdapter.Creator] 实现类
 */
open class SimpleCreator : FragVpAdapter.Creator {

    /** 创建 [Fragment] 方法块 */
    private lateinit var createFragment: (Int) -> Fragment

    /** [Fragment] 数量 */
    private var mCount = 0

    override val count: Int
        get() = mCount

    override fun createFragment(position: Int): Fragment {
        return createFragment.invoke(position)
    }

    /**
     * 设置 [Fragment] 数量
     *
     * @param count [Fragment] 数量
     */
    fun count(count: Int) {
        mCount = count
    }

    /**
     * 设置创建 [Fragment] 方法块
     *
     * @param createFragment 创建 [Fragment] 方法块
     */
    fun createFragment(createFragment: (Int) -> Fragment) {
        this.createFragment = createFragment
    }
}

/**
 * 设置 [Fragment] 创建器
 *
 * @param init 方法块
 *
 * @see SimpleCreator
 */
inline fun FragVpAdapter.Builder.creator(init: SimpleCreator.() -> Unit): FragVpAdapter.Builder {
    val creator = SimpleCreator()
    creator.init()
    return this.creator(creator)
}

/**
 * 类型别名 - 获取 Fragment 标题
 * - （Fragment对象，Fragment位置）
 */
typealias OnPageTitleListener = (Fragment, Int) -> String?