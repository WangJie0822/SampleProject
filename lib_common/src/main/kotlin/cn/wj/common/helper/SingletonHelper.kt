package cn.wj.common.helper

/**
 * 单例帮助类，带参数的单例模式
 * ```
 * class Singleton(val arg: String) {
 *
 *      companion object : SingletonHelper<Singleton, String>(::Singleton)
 * }
 * ```
 *
 * - 创建时间：2021/1/6
 *
 * @author 王杰
 */
abstract class SingletonHelper<out I, in P>(creator: (P) -> I) {

    private var creator: ((P) -> I)? = creator

    @Volatile
    private var instance: I? = null

    fun getInstance(arg: P): I {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}