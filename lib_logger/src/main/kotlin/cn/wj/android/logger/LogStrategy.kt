package cn.wj.android.logger

/**
 * 日志打印策略接口
 *
 * @author 王杰
 */
interface LogStrategy {

    /**
     * 日志打印
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param message 日志信息
     */
    fun log(priority: Int, tag: String?, message: String)
}