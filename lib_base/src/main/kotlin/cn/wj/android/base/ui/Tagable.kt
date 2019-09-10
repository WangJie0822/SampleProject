package cn.wj.android.base.ui

import java.io.Closeable
import java.io.IOException
import java.util.*

/**
 * 可保存 Map 数据接口
 */
interface Tagable {

    /** Map 数据集合 */
    val mBagOfTags: HashMap<String, Any>

    /** 标记 - 当前界面是否关闭 */
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
            // It is possible that we'll call close() multiple times on the same object, but
            // Closeable interface requires close method to be idempotent:
            // "if the stream is already closed then invoking this method has no effect." (c)
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