package cn.wj.android.base.log

/**
 * 日志打印适配器
 *
 * @author 王杰
 */
interface LogAdapter {

    /**
     * 打印日志
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param message 日志数据
     */
    fun log(priority: Int, tag: String?, message: String)

    /**
     * 是否允许打印日志
     *
     * @param priority 日志等级
     * @param tag 日志标签
     */
    fun isLoggable(priority: Int, tag: String?): Boolean
}