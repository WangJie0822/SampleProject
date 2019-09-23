package cn.wj.android.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.wj.android.common.Tagable
import java.util.*

/**
 * Fragment基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - 添加 [initBefore] 方法，在一切流程开始前预处理一些数据
 * - [onCreateView] 方法中对 [layoutResID] 对应的 [View] 进行加载，并在 [initView] 中进行初始化操作
 * - 维护了 [rootView]等参数
 *
 * @author 王杰
 */
abstract class BaseLibFragment
    : Fragment(),
        Tagable {

    override val mTagMaps: HashMap<String, Any> = hashMapOf()

    override var mClosed: Boolean = false

    /** 当前界面 Context 对象*/
    protected lateinit var mContext: AppCompatActivity

    /** 跟布局对象 */
    protected lateinit var rootView: View

    /** 页面标题 */
    open val pageTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存当前 Context 对象
        mContext = activity as AppCompatActivity

        // 初始化数据
        initBefore()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(layoutResID, container, false)

        // 初始化布局
        initView()

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTags()
    }

    /**
     * 初始化数据，最先调用
     */
    protected open fun initBefore() {}

    /**
     * 初始化布局
     */
    abstract fun initView()

    /** 界面布局 id */
    abstract val layoutResID: Int
}