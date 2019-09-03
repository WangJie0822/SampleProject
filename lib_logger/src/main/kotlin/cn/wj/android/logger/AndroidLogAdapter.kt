package cn.wj.android.logger

/**
 * Andorid 用日志适配器
 *
 * @author 王杰
 */
@Suppress("unused")
open class AndroidLogAdapter : LogAdapter {

    /** 日志格式化策略 */
    private val mFormatStrategy: FormatStrategy

    /**
     * 默认构造方法
     * * 默认使用 [PrettyFormatStrategy]
     */
    constructor() {
        mFormatStrategy = PrettyFormatStrategy.newBuilder().build()
    }

    /**
     * 构造方法
     *
     * @param formatStrategy 指定日志格式化策略
     */
    constructor(formatStrategy: FormatStrategy) {
        mFormatStrategy = formatStrategy
    }


    override fun log(priority: Int, tag: String?, message: String) {
        mFormatStrategy.log(priority, tag, message)
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return true
    }
}