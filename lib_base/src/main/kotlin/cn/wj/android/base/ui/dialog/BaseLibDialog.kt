package cn.wj.android.base.ui.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import cn.wj.android.common.Tagable
import java.util.*

/**
 * Dialog 弹窗基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - [onCreateView] 方法中对 [layoutResId] 对应的 [View] 进行加载，并在 [initView] 中进行初始化操作
 * - 维护了 [mRootView]、[mDialogWidth]、[mDialogHeight]、[mGravity] 等参数，在 [onActivityCreated] 方法中进行配置
 *
 * @author 王杰
 */
abstract class BaseLibDialog
    : DialogFragment(),
        Tagable {

    override val mTagMaps: HashMap<String, Any> = hashMapOf()

    override var mClosed: Boolean = false

    /** Context 对象 */
    protected lateinit var mContext: FragmentActivity

    /** 根布局对象 */
    protected lateinit var mRootView: View

    /** Dialog 宽度 单位：px  */
    protected var mDialogWidth: Int = WindowManager.LayoutParams.MATCH_PARENT
    /** Dialog 高度 单位：px */
    protected var mDialogHeight: Int = WindowManager.LayoutParams.MATCH_PARENT
    /** Dialog 重心 [Gravity] */
    protected var mGravity: Int = Gravity.CENTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存 Context 对象
        mContext = activity as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // 取消标题栏
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))

        mRootView = inflater.inflate(layoutResId, container, false)

        // 初始化布局
        initView()

        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 配置 Dialog 宽高、重心
        val layoutParams = dialog?.window?.attributes
        layoutParams?.width = mDialogWidth
        layoutParams?.height = mDialogHeight
        layoutParams?.gravity = mGravity
        dialog?.window?.attributes = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        clearTags()
    }

    /** 界面布局 id */
    abstract val layoutResId: Int

    /**
     * 初始化布局
     */
    abstract fun initView()
}