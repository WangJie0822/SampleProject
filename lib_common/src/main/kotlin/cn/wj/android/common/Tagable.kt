package cn.wj.android.common

import java.io.Closeable
import java.io.IOException
import java.util.*

/**
 * 可保存 Map 数据接口
 */
interface Tagable {

    /** Map 数据集合 */
    val mBagOfTags: HashMap<String, Any>

    /** 标记 - 当前接口是否关闭 */
    var mClosed: Boolean

    fun <T> getTag(key: String): T? {
        return synchronized(mBagOfTags) {
            @Suppress("UNCHECKED_CAST")
            mBagOfTags[key] as? T
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> setTagIfAbsent(key: String, newValue: T): T {
        val previous: T?
        synchronized(mBagOfTags) {
            previous = mBagOfTags[key] as T?
            if (previous == null) {
                mBagOfTags[key] = newValue as Any
            }
        }
        val result = previous ?: newValue
        if (mClosed) {
            // 如果接口已关闭，直接关闭 Closeable 对象
            closeWithRuntimeException(result)
        }
        return result
    }

    fun closeWithRuntimeException(obj: Any?) {
        if (obj != null && obj is Closeable) {
            try {
                obj.close()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }
    }
}