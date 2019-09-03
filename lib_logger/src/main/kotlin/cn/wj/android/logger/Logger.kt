package cn.wj.android.logger

/**
 * 日志打印
 *
 * @author 王杰
 */
@Suppress("unused")
object Logger {

    /** 日志等级 - 详细 */
    const val VERBOSE = 2
    /** 日志等级 - 调试 */
    const val DEBUG = 3
    /** 日志等级 - 信息 */
    const val INFO = 4
    /** 日志等级 - 警告 */
    const val WARN = 5
    /** 日志等级 - 错误 */
    const val ERROR = 6
    /** 日志等级 - 断言 */
    const val ASSERT = 7

    /** 日志打印对象 */
    private var mPrinter: Printer = LoggerPrinter()

    /**
     * 设置日志打印对象
     *
     * @param printer 日志打印对象
     */
    @JvmStatic
    fun printer(printer: Printer) {
        mPrinter = printer
    }

    /**
     * 添加日志打印适配器
     *
     * @param adapter 日志打印适配器对象
     */
    @JvmStatic
    fun addLogAdapter(adapter: LogAdapter) {
        mPrinter.addAdapter(adapter)
    }

    /**
     * 清空日志打印适配器
     */
    @JvmStatic
    fun clearLogAdapters() {
        mPrinter.clearLogAdapters()
    }

    /**
     * 添加临时标签
     *
     * @param tag 临时标签
     *
     * @return 日志打印对象
     */
    @JvmStatic
    fun t(tag: String?): Printer {
        return mPrinter.t(tag)
    }

    /**
     * 打印日志
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param message 日志数据
     * @param throwable 异常信息 可空
     */
    @JvmStatic
    fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?) {
        mPrinter.log(priority, tag, message, throwable)
    }

    /**
     * 打印 DEBUG 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun d(message: String, vararg args: Any) {
        mPrinter.d(message, args)
    }

    /**
     * 打印 DEBUG 信息
     *
     * @param any 日志信息
     */
    @JvmStatic
    fun d(any: Any?) {
        mPrinter.d(any)
    }

    /**
     * 打印 ERROR 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun e(message: String, vararg args: Any) {
        mPrinter.e(message, args)
    }

    /**
     * 打印 ERROR 信息
     *
     * @param throwable 异常对象
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun e(throwable: Throwable?, message: String, vararg args: Any) {
        mPrinter.e(throwable, message, args)
    }

    /**
     * 打印 ERROR 信息
     *
     * @param throwable 异常对象
     */
    @JvmStatic
    fun e(throwable: Throwable) {
        mPrinter.e(throwable, "Throwable")
    }

    /**
     * 打印 INFO 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun i(message: String, vararg args: Any) {
        mPrinter.i(message, args)
    }

    /**
     * 打印 VERBOSE 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun v(message: String, vararg args: Any) {
        mPrinter.v(message, args)
    }

    /**
     * 打印 WARN 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun w(message: String, vararg args: Any) {
        mPrinter.w(message, args)
    }

    /**
     * 打印 ASSERT 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    @JvmStatic
    fun a(message: String, vararg args: Any) {
        mPrinter.a(message, args)
    }

    /**
     * 打印 json 数据
     * - 对 json 进行格式化输出
     *
     * @param json json 字符串
     */
    @JvmStatic
    fun json(json: String?) {
        mPrinter.json(json)
    }

    /**
     * 打印 xml 数据
     * - 对 xml 进行格式化输出
     *
     * @param xml xml 字符串
     */
    @JvmStatic
    fun xml(xml: String?) {
        mPrinter.xml(xml)
    }
}