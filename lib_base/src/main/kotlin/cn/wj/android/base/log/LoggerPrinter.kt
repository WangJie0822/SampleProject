package cn.wj.android.base.log

import cn.wj.android.base.expanding.getStackTraceString
import cn.wj.android.base.expanding.jsonFormat
import cn.wj.android.base.expanding.xmlFormat
import cn.wj.android.base.log.Logger.ASSERT
import cn.wj.android.base.log.Logger.DEBUG
import cn.wj.android.base.log.Logger.ERROR
import cn.wj.android.base.log.Logger.INFO
import cn.wj.android.base.log.Logger.VERBOSE
import cn.wj.android.base.log.Logger.WARN

/**
 * 日志打印实现类
 *
 * @author 王杰
 */
class LoggerPrinter : Printer {

    /** 日志打印适配器列表 */
    private val mLogAdapters = arrayListOf<LogAdapter>()
    /** 日志打印临时标签 */
    private val mLocalTag = ThreadLocal<String>()

    override fun addAdapter(adapter: LogAdapter) {
        mLogAdapters.add(adapter)
    }

    override fun t(tag: String?): Printer {
        if (tag != null) {
            mLocalTag.set(tag)
        }
        return this
    }

    override fun d(message: String, vararg args: Any) {
        log(DEBUG, null, message, args)
    }

    override fun d(any: Any?) {
        log(DEBUG, null, any.toString())
    }

    override fun e(message: String, vararg args: Any) {
        log(ERROR, null, message, args)
    }

    override fun e(throwable: Throwable?, message: String, vararg args: Any) {
        log(ERROR, throwable, message, args)
    }

    override fun w(message: String, vararg args: Any) {
        log(WARN, null, message, args)
    }

    override fun i(message: String, vararg args: Any) {
        log(INFO, null, message, args)
    }

    override fun v(message: String, vararg args: Any) {
        log(VERBOSE, null, message, args)
    }

    override fun a(message: String, vararg args: Any) {
        log(ASSERT, null, message, args)
    }

    override fun json(json: String?) {
        val msg = json.jsonFormat()
        when {
            json.isNullOrBlank() -> d("Empty/Null json content")
            msg.isBlank() -> e("Invalid Json")
            else -> d(msg)
        }
    }

    override fun xml(xml: String?) {
        val msg = xml.xmlFormat()
        when {
            xml.isNullOrBlank() -> d("Empty/Null xml content")
            msg.isBlank() -> e("Invalid Json")
            else -> d(msg)
        }
    }

    @Synchronized
    override fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?) {
        var msg = message.orEmpty()
        if (throwable != null && message != null) {
            msg += " : " + throwable.getStackTraceString()
        }
        if (throwable != null && message == null) {
            msg = throwable.getStackTraceString()
        }
        if (message.isNullOrBlank()) {
            msg = "Empty/NULL log message"
        }

        for (adapter in mLogAdapters) {
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, msg)
            }
        }
    }

    override fun clearLogAdapters() {
        mLogAdapters.clear()
    }

    /**
     * 打印日志
     *
     * @param priority 日志等级
     * @param throwable 异常信息 可空
     * @param msg 日志数据
     * @param args 格式化数据
     */
    @Synchronized
    private fun log(priority: Int, throwable: Throwable?, msg: String, vararg args: Any) {
        val tag = getTag()
        val message = createMessage(msg, *args)
        log(priority, tag, message, throwable)
    }

    /**
     * 获取临时标签
     */
    private fun getTag(): String? {
        val tag = mLocalTag.get()
        if (tag != null) {
            mLocalTag.remove()
            return tag
        }
        return null
    }

    /**
     * 根据数据生成打印的字符串
     *
     * @param message 消息数据
     * @param args 格式化参数
     *
     * @return 实际打印数据
     */
    private fun createMessage(message: String, vararg args: Any): String {
        return if (args.isEmpty()) message else String.format(message, *args)
    }
}