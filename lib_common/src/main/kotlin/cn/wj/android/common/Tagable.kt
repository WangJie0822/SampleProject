package cn.wj.android.common

import java.io.Closeable
import java.io.IOException
import java.util.*

/**
 * 可保存 Map 数据接口
 */
interface Tagable {

    /** Map 数据集合 */
    val mTagMaps: HashMap<String, Any>

    /** 标记 - 当前接口是否关闭 */
    var mClosed: Boolean

    fun <T> getTag(key: String): T? {
        return synchronized(mTagMaps) {
            @Suppress("UNCHECKED_CAST")
            mTagMaps[key] as? T
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> setTagIfAbsent(key: String, newValue: T): T {
        val previous: T?
        synchronized(mTagMaps) {
            previous = mTagMaps[key] as T?
            if (previous == null) {
                mTagMaps[key] = newValue as Any
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

    /**
     * 清空 Tag，此方法必须在回收资源是调用
     */
    fun clearTags() {
        mClosed = true
        synchronized(mTagMaps) {
            for (value in mTagMaps.values) {
                closeWithRuntimeException(value)
            }
        }
    }
}