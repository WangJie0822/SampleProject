package cn.wj.android.base.log

import android.util.Log

/**
 * Android 日志打印策略
 *
 * @author 王杰
 */
class LogcatLogStrategy : LogStrategy {

    private val DEFAULT_TAG = "NO_TAG"

    override fun log(priority: Int, tag: String?, message: String) {
        var tempTag = tag

        if (tag.isNullOrBlank()) {
            tempTag = DEFAULT_TAG
        }

        Log.println(priority, tempTag, message)
    }
}