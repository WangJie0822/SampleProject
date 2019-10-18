package cn.wj.android.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import cn.wj.android.common.log.InternalLog
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess

/**
 * 应用程序 Activity 管理类
 * - 用于 Activity 管理和应用程序退出
 * - 强烈建议在 Application$onCreate 方法中调用 register 方法注册，否则会导致 Activity 管理不完整
 *
 * @author 王杰
 */
@Suppress("unused")
object AppManager {

    /** Application 对象 */
    private var mApplication: Application? = null

    /** 保存 Activity 对象的堆栈 */
    private val activityStack: Stack<WeakReference<Activity>> = Stack()

    /** 忽略列表 */
    private val ignoreActivitys = arrayListOf<Class<out Activity>>()

    /** 前台界面个数 */
    private var foregroundCount = 0

    /** Activity 生命周期回调接口*/
    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            onCreate(activity)
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivityCreated")
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivitySaveInstanceState")
        }

        override fun onActivityStarted(activity: Activity?) {
            foregroundCount++
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivityStarted")
        }

        override fun onActivityResumed(activity: Activity?) {
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivityResumed")
        }

        override fun onActivityPaused(activity: Activity?) {
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivityPaused")
        }

        override fun onActivityStopped(activity: Activity?) {
            foregroundCount--
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivityStopped")
        }

        override fun onActivityDestroyed(activity: Activity?) {
            onDestroy(activity)
            InternalLog.i("AppManager", "Activity: ${activity?.javaClass?.simpleName} ----> onActivityDestroyed")
        }
    }

    /**
     * 获取前台界面数量
     *
     * @return 前台 Activity 数量
     */
    @JvmStatic
    fun getForegroundCount(): Int {
        return foregroundCount
    }

    /**
     * 应用是否在前台
     *
     * @return 应用是否在前台
     */
    @JvmStatic
    fun isForeground(): Boolean {
        val activityManager = getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = getContext().packageName
        // 获取Android设备中所有正在运行的App
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    /**
     * 注册 Application
     *
     * @param application 应用 [Application] 对象
     */
    @JvmStatic
    fun register(application: Application) {
        mApplication = application
        application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    /**
     * 获取可用 Application 对象
     */
    @JvmStatic
    fun getApplication(): Application {
        if (mApplication == null) {
            register(getApplicationByReflect())
            if (mApplication == null) {
                throw NullPointerException("Application must not be null! Please register AppManager in your Application start！")
            }
        }
        return mApplication!!
    }

    /**
     * 获取 [Context] 对象
     * - 优先获取栈顶的 [Activity] 对象，若没有，则返回 [Application] 对象
     *
     * @return [Context] 对象
     */
    @JvmStatic
    fun getContext(): Context {
        return peekActivity() ?: getApplication()
    }

    /**
     * 通过反射获取当前 Application 对象
     *
     * @return 当前 Application 对象
     */
    private fun getApplicationByReflect(): Application {
        try {
            @SuppressLint("PrivateApi")
            val activityThread = Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread)
                    ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: Exception) {
            InternalLog.e("AppManager", "getApplicationByReflect", e)
        }
        throw NullPointerException("u should init first")
    }

    /**
     * 将 Activity 类对象添加到忽略列表
     */
    @JvmStatic
    fun addToIgnore(vararg clazzs: Class<out Activity>) {
        ignoreActivitys.addAll(clazzs)
    }

    /**
     * 添加 Activity 到堆栈
     *
     * @param activity Activity 对象
     */
    @JvmStatic
    fun onCreate(activity: Activity?) {
        if (activity != null && ignoreActivitys.contains(activity.javaClass)) {
            // 不添加在忽略列表中的 Activity
            return
        }
        add(activity)
    }

    /**
     * 将 Activity 从堆栈移除
     *
     * @param activity Activity 对象
     */
    @JvmStatic
    fun onDestroy(activity: Activity?) {
        if (activity != null && ignoreActivitys.contains(activity.javaClass)) {
            // 不移除在忽略列表中的 Activity
            return
        }
        remove(activity)
    }

    /**
     * 判断当前堆栈中是否存在对应 Activity
     *
     * @param clazz Activity 类对象
     */
    @JvmStatic
    fun contains(clazz: Class<out Activity>): Boolean {
        return activityStack.count { it.get()?.javaClass == clazz } > 0
    }

    /**
     * 将 Activity 加入堆栈
     */
    @JvmStatic
    fun add(activity: Activity?) {
        if (activity == null) {
            return
        }
        activityStack.add(WeakReference(activity))
    }

    /**
     * 将 Activity 从堆栈移除
     *
     * @param activity Activity 对象
     */
    @JvmStatic
    fun remove(activity: Activity?) {
        if (activity == null) {
            return
        }
        var remove: WeakReference<Activity>? = null
        activityStack.forEach { weak ->
            if (weak.get() == activity) {
                remove = weak
            }
        }
        activityStack.remove(remove)
    }

    /**
     * 结束指定 Activity 之外的其他 Activity
     *
     * @param activitys 指定不关闭的 Activity
     */
    @JvmStatic
    fun finishAllWithout(vararg activitys: Activity) {
        for (activity in activitys) {
            remove(activity)
        }
        finishAllActivity()
        for (activity in activitys) {
            add(activity)
        }
    }

    /**
     * 结束指定 Activity 之外的其他 Activity
     *
     * @param activitys 指定不关闭的 Activity 类对象
     */
    @JvmStatic
    fun finishAllWithout(vararg activitys: Class<out Activity>) {
        for (activity in activitys) {
            remove(getActivity(activity))
        }
        finishAllActivity()
        for (activity in activitys) {
            add(getActivity(activity))
        }
    }


    /**
     * 结束指定 Activity
     *
     * @param clazz Activity 类对象
     */
    @JvmStatic
    fun finishActivity(clazz: Class<out Activity>) {
        val del: Activity? = activityStack.lastOrNull { it.get()?.javaClass == clazz }?.get()
        del?.finish()
    }

    /**
     * 结束指定 Activity
     *
     * @param clazzs Activity 类对象
     */
    @JvmStatic
    fun finishActivities(vararg clazzs: Class<out Activity>) {
        for (clazz in clazzs) {
            finishActivity(clazz)
        }
    }

    /**
     * 获取栈顶的 Activity
     *
     * @return 栈顶的 Activity 对象
     */
    @JvmStatic
    fun peekActivity(): Activity? {
        return if (activityStack.isEmpty()) {
            null
        } else {
            activityStack.peek().get()
        }
    }

    /**
     * 根据类，获取 Activity 对象
     *
     * @param clazz  Activity 类
     * @param A      Activity 类型
     *
     * @return       Activity 对象
     */
    @JvmStatic
    fun <A : Activity> getActivity(clazz: Class<A>): A? {
        @Suppress("UNCHECKED_CAST")
        return activityStack.firstOrNull { it.get()?.javaClass == clazz }?.get() as A?
    }

    /**
     * 根据下标，获取 Activity 对象
     *
     * @param index  Activity 下标
     *
     * @return       Activity 对象
     */
    @JvmStatic
    fun getActivity(index: Int): Activity? {
        return activityStack[index]?.get()
    }

    /**
     * 获取堆栈中 Activity 数量
     *
     * @return Activity 数量
     */
    @JvmStatic
    fun getStackSize(): Int {
        return activityStack.size
    }

    /**
     * 结束所有 Activity
     */
    @JvmStatic
    private fun finishAllActivity() {
        for (weak in activityStack) {
            weak.get()?.finish()
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     */
    @JvmStatic
    fun appExit() {
        try {
            val activityMgr = getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.killBackgroundProcesses(getContext().packageName)
            finishAllActivity()
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
