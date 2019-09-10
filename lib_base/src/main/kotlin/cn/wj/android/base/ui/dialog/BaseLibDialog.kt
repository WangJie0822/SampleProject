package cn.wj.android.base.ui.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import cn.wj.android.base.tools.DEVICE_SCREEN_HEIGHT
import cn.wj.android.base.tools.statusBarHeight
import cn.wj.android.base.ui.Tagable
import java.util.*

/**
 * Dialog 弹窗基类
 * - 维护 [mContext]，当前界面的 Context 对象
 * - 添加 [initBefore] 方法，在一切流程开始前预处理一些数据
 * - [onCreateView] 方法中对 [layoutResID] 对应的 [View] 进行加载，并在 [initView] 中进行初始化操作
 * - 维护了 [mRootView]、[mDialogWidth]、[mDialogHeight]、[mGravity] 等参数，在 [onActivityCreated] 方法中进行配置
 *
 * @author 王杰
 */
abstract class BaseLibDialog
    : DialogFragment(),
        Tagable {

    override val mBagOfTags: HashMap<String, Any> = hashMapOf()

    override var mClosed: Boolean = false

    /** Context 对象 */
    protected lateinit var mContext: AppCompatActivity

    /** 根布局对象 */
    protected lateinit var mRootView: View

    /** Dialog 宽度 单位：px  */
    protected var mDialogWidth: Int = WindowManager.LayoutParams.MATCH_PARENT
    /** Dialog 高度 单位：px */
    protected var mDialogHeight: Int = if (DEVICE_SCREEN_HEIGHT - statusBarHeight == 0) {
        WindowManager.LayoutParams.MATCH_PARENT
    } else {
        DEVICE_SCREEN_HEIGHT - statusBarHeight
    }
    /** Dialog 重心 [Gravity] */
    protected var mGravity: Int = Gravity.CENTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存 Context 对象
        mContext = activity as AppCompatActivity

        // 初始化数据
        initBefore()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // 取消标题栏
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))

        mRootView = inflater.inflate(layoutResID, container, false)

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
        mClosed = true
        synchronized(mBagOfTags) {
            for (value in mBagOfTags.values) {
                closeWithRuntimeException(value)
            }
        }
    }

    /**
     * 初始化数据，最先调用
     */
    protected open fun initBefore() {}

    /**
     * 初始化布局
     */
    protected open fun initView() {}

    /** 界面布局 id */
    abstract val layoutResID: Int
}