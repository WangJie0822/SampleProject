package cn.wj.android.logger

import android.util.Log

/**
 * Android 日志打印策略
 *
 * @author 王杰
 */
class LogcatLogStrategy : LogStrategy {

    /**
     * 默认标签文本
     */
    private val defaultTag = "NO_TAG"

    override fun log(priority: Int, tag: String?, message: String) {
        var tempTag = tag

        if (tag.isNullOrBlank()) {
            tempTag = defaultTag
        }

        Log.println(priority, tempTag, message)
    }
}