package cn.wj.android.base.log

/**
 * Log 格式化策略接口
 *
 * @author 王杰
 */
interface FormatStrategy {

    /**
     * 打印日志
     *
     * @param priority 日志等级
     * @param tag 日志标签
     * @param message 日志数据
     */
    fun log(priority: Int, tag: String?, message: String)
}