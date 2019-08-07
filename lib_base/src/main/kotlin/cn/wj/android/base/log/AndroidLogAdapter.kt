package cn.wj.android.base.log

/**
 * Andorid 用日志适配器
 *
 * @author 王杰
 */
@Suppress("unused")
open class AndroidLogAdapter : LogAdapter {

    /** 日志格式化策略 */
    private val mFormatStrategy: FormatStrategy

    constructor() {
        mFormatStrategy = PrettyFormatStrategy.newBuilder().build()
    }

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