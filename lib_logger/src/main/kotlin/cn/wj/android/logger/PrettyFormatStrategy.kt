package cn.wj.android.logger

import kotlin.math.min

/**
 * 漂亮的日志格式化策略
 *
 * @author 王杰
 */
class PrettyFormatStrategy private constructor(builder: Builder) : FormatStrategy {

    companion object {

        /**
         * 新建建造器
         */
        @JvmStatic
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private val chunkSize = 4000

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private val minStackOffset = 5

    /**
     * Drawing toolbox
     */
    private val topLeftCorner = '┌'
    private val bottomLeftCorner = '└'
    private val middleCorner = '├'
    private val horizontalLine = '│'
    private val doubleDivider = "────────────────────────────────────────────────────────"
    private val singleDivider = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    private val topBorder = topLeftCorner + doubleDivider + doubleDivider
    private val bottomBorder = bottomLeftCorner + doubleDivider + doubleDivider
    private val middleBorder = middleCorner + singleDivider + singleDivider

    private val methodCount: Int
    private val methodOffset: Int
    private val showThreadInfo: Boolean
    private val logStrategy: LogStrategy
    private val tag: String?

    init {
        methodCount = builder.methodCount
        methodOffset = builder.methodOffset
        showThreadInfo = builder.showThreadInfo
        logStrategy = builder.logStrategy!!
        tag = builder.tag
    }

    override fun log(priority: Int, tag: String?, message: String) {
        val tempTag = formatTag(tag)

        logTopBorder(priority, tempTag)
        logHeaderContent(priority, tempTag, methodCount)

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        val bytes = message.toByteArray()
        val length = bytes.size
        if (length <= chunkSize) {
            if (methodCount > 0) {
                logDivider(priority, tempTag)
            }
            logContent(priority, tempTag, message)
            logBottomBorder(priority, tempTag)
            return
        }
        if (methodCount > 0) {
            logDivider(priority, tempTag)
        }
        var i = 0
        while (i < length) {
            val count = min(length - i, chunkSize)
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(priority, tempTag, String(bytes, i, count))
            i += chunkSize
        }
        logBottomBorder(priority, tempTag)
    }

    /**
     * 打印日志上边界
     *
     * @param priority 日志等级
     * @param tag 日志标签
     */
    private fun logTopBorder(priority: Int, tag: String?) {
        logChunk(priority, tag, topBorder)
    }

    /**
     * 打印日志标题内容
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param methodCount 消息行数
     */
    private fun logHeaderContent(priority: Int, tag: String?, methodCount: Int) {
        var tempMethodCount = methodCount
        val trace = Thread.currentThread().stackTrace
        if (showThreadInfo) {
            logChunk(priority, tag, horizontalLine + " Thread: " + Thread.currentThread().name)
            logDivider(priority, tag)
        }
        var level = ""

        val stackOffset = getStackOffset(trace) + methodOffset

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (tempMethodCount + stackOffset > trace.size) {
            tempMethodCount = trace.size - stackOffset - 1
        }

        for (i in tempMethodCount downTo 1) {
            val stackIndex = i + stackOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append(horizontalLine)
                    .append(' ')
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].className))
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            logChunk(priority, tag, builder.toString())
        }
    }

    /**
     * 打印日志下边界
     *
     * @param priority 日志等级
     * @param tag 日志标签
     */
    private fun logBottomBorder(priority: Int, tag: String?) {
        logChunk(priority, tag, bottomBorder)
    }

    /**
     * 打印日志分隔线
     *
     * @param priority 日志等级
     * @param tag 日志标签
     */
    private fun logDivider(priority: Int, tag: String?) {
        logChunk(priority, tag, middleBorder)
    }

    /**
     * 打印日志内容
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param chunk 块数据
     */
    private fun logContent(priority: Int, tag: String?, chunk: String) {
        val lines = chunk.split(System.getProperty("line.separator").orEmpty().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            logChunk(priority, tag, "$horizontalLine $line")
        }
    }

    /**
     * 打印日志块
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param chunk 块数据
     */
    private fun logChunk(priority: Int, tag: String?, chunk: String) {
        logStrategy.log(priority, tag, chunk)
    }

    /**
     * 获取简易类名
     */
    private fun getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    /**
     * 获取堆栈跟踪起始索引
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = minStackOffset
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LoggerPrinter::class.java.name && name != Logger::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    /**
     * 格式化标签
     * - 拼接临时标签
     *
     * @param tag 临时标签
     *
     * @return 实际打印的标签
     */
    private fun formatTag(tag: String?): String? {
        return if (tag != null && tag.isNotBlank() && this.tag != tag) {
            this.tag + "-" + tag
        } else this.tag
    }

    /**
     * 建造器
     */
    @Suppress("unused")
    class Builder internal constructor() {

        internal var methodCount = 2
        internal var methodOffset = 0
        internal var showThreadInfo = true
        internal var logStrategy: LogStrategy? = null
        internal var tag: String? = "PRETTY_LOGGER"

        /**
         * 方法信息行数
         */
        fun methodCount(count: Int): Builder {
            methodCount = count
            return this
        }

        fun methodOffset(offset: Int): Builder {
            methodOffset = offset
            return this
        }

        /**
         * 是否显示线程信息
         */
        fun showThreadInfo(show: Boolean): Builder {
            showThreadInfo = show
            return this
        }

        /**
         * 日志打印策略
         */
        fun logStrategy(strategy: LogStrategy): Builder {
            logStrategy = strategy
            return this
        }

        /**
         * 全局标签
         */
        fun tag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun build(): PrettyFormatStrategy {
            if (logStrategy == null) {
                logStrategy = LogcatLogStrategy()
            }
            return PrettyFormatStrategy(this)
        }
    }
}