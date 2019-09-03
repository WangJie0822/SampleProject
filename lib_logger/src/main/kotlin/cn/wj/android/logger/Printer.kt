package cn.wj.android.logger

/**
 * 日志打印接口
 *
 * @author 王杰
 */
interface Printer {

    /**
     * 添加日志适配器
     *
     * @param adapter 日志适配器
     */
    fun addAdapter(adapter: LogAdapter)

    /**
     * 添加临时标签
     *
     * @param tag 临时标签
     *
     * @return 打印接口对象
     */
    fun t(tag: String?): Printer

    /**
     * 打印 DEBUG 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun d(message: String, vararg args: Any)

    /**
     * 打印 DEBUG 信息
     *
     * @param any 日志信息
     */
    fun d(any: Any?)

    /**
     * 打印 ERROR 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun e(message: String, vararg args: Any)

    /**
     * 打印 ERROR 信息
     *
     * @param throwable 异常对象
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun e(throwable: Throwable?, message: String, vararg args: Any)

    /**
     * 打印 WARN 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun w(message: String, vararg args: Any)

    /**
     * 打印 INFO 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun i(message: String, vararg args: Any)

    /**
     * 打印 VERBOSE 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun v(message: String, vararg args: Any)

    /**
     * 打印 ASSERT 信息
     *
     * @param message 日志信息
     * @param args 格式化信息
     */
    fun a(message: String, vararg args: Any)

    /**
     * 打印 json 数据
     * - 对 json 进行格式化输出
     *
     * @param json json 字符串
     */
    fun json(json: String?)

    /**
     * 打印 xml 数据
     * - 对 xml 进行格式化输出
     *
     * @param xml xml 字符串
     */
    fun xml(xml: String?)

    /**
     * 打印日志
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param message 日志数据
     * @param throwable 异常信息 可空
     */
    fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?)

    /**
     * 清空日志打印适配器
     */
    fun clearLogAdapters()
}