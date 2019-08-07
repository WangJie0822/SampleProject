@file:Suppress("unused")

package cn.wj.android.base.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import cn.wj.android.base.log.Logger
import cn.wj.android.base.utils.AppManager

/* ----------------------------------------------------------------------------------------- */
/* |                                   状态栏 导航栏相关                                     | */
/* ----------------------------------------------------------------------------------------- */

/** 状态栏高度 - 单位：px */
val statusBarHeight: Int
    get() {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

/**
 * 判断是否显示状态栏
 *
 * @return 是否显示状态栏
 */
fun Activity.isStatusBarVisible(): Boolean {
    return this.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
}

/**
 * 设置状态栏是否为浅色模式
 * - API 23 及其以上有效
 *
 * @param isLightMode 是否是浅色模式
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarLightMode(isLightMode: Boolean) {
    window.setStatusBarLightMode(isLightMode)
}

/**
 * 设置状态栏是否为浅色模式
 * - API 23 及其以上有效
 *
 * @param isLightMode 是否是浅色模式
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Window.setStatusBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var vis = decorView.systemUiVisibility
        vis = if (isLightMode) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    }
}

/**
 * 设置状态栏是否为浅色模式
 * - 小米手机使用，需要 MIUI6 以上
 *
 * @param isLightMode 是否是浅色模式
 */
@SuppressLint("PrivateApi")
fun Activity.setMIUIStatusBarLightMode(isLightMode: Boolean) {
    window.setMIUIStatusBarLightMode(isLightMode)
}

/**
 * 设置状态栏是否为浅色模式
 * - 小米手机使用，需要 MIUI6 以上
 *
 * @param isLightMode 是否是浅色模式
 */
@SuppressLint("PrivateApi")
fun Window.setMIUIStatusBarLightMode(isLightMode: Boolean) {
    try {
        val darkModeFlag: Int
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField =
                javaClass.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        if (isLightMode) {
            extraFlagField.invoke(this, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
        } else {
            extraFlagField.invoke(this, 0, darkModeFlag)//清除黑色字体
        }
    } catch (e: Exception) {
        Logger.t("Bar").e("setMIUIStatusBarLightMode", e)
    }
}

/**
 * 设置状态栏图标为深色和魅族特定的文字风格
 * - 魅族手机使用
 *
 * @param isLightMode 是否是浅色模式
 */
fun Activity.setFlymeStatusBarLightMode(isLightMode: Boolean) {
    window.setFlymeStatusBarLightMode(isLightMode)
}

/**
 * 设置状态栏图标为深色和魅族特定的文字风格
 * - 魅族手机使用
 *
 * @param isLightMode 是否是浅色模式
 */
fun Window.setFlymeStatusBarLightMode(isLightMode: Boolean) {
    try {
        val lp = attributes
        val darkFlag = WindowManager.LayoutParams::class.java
                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags = WindowManager.LayoutParams::class.java
                .getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(lp)
        value = if (isLightMode) {
            value or bit
        } else {
            value and bit.inv()
        }
        meizuFlags.setInt(lp, value)
        attributes = lp
    } catch (e: Exception) {
        Logger.t("Bar").e("setFlymeStatusBarLightMode", e)
    }
}

/** 导航栏高度 - 单位：px */
val navBarHeight: Int
    get() {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

/** 是否支持导航栏 */
val isSupportNavBar: Boolean
    @SuppressLint("ObsoleteSdkInt")
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val wm = AppManager.getApplication().getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val display = wm.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y != size.y || realSize.x != size.x
        }
        val menu = ViewConfiguration.get(AppManager.getApplication()).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !menu && !back
    }

/**
 * 判断导航栏是否显示
 *
 * @return 导航栏是否显示
 */
fun Activity.isNavBarVisible(): Boolean {
    return window.isNavBarVisible()
}

/**
 * 判断导航栏是否显示
 *
 * @return 导航栏是否显示
 */
fun Window.isNavBarVisible(): Boolean {
    val visibility = decorView.systemUiVisibility
    return visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
}

/**
 * 设置导航栏颜色
 *
 * @param color 颜色值
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.setNavBarColor(@ColorInt color: Int) {
    window.setNavBarColor(color)
}

/**
 * 设置导航栏颜色
 *
 * @param color 颜色值
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.setNavBarColor(@ColorInt color: Int) {
    navigationBarColor = color
}

/**
 * 获取导航栏颜色
 *
 * @return 导航栏颜色
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.getNavBarColor(): Int {
    return window.getNavBarColor()
}

/**
 * 获取导航栏颜色
 *
 * @return 导航栏颜色
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.getNavBarColor(): Int {
    return navigationBarColor
}

/**
 * 隐藏导航栏
 */
fun Window.hideNavBar() {
    if (Build.VERSION.SDK_INT in Build.VERSION_CODES.HONEYCOMB_MR1..Build.VERSION_CODES.JELLY_BEAN_MR2) {
        // API 12 .. 18
        decorView.systemUiVisibility = View.GONE
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // API 19 ..
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}

/**
 * 隐藏导航栏
 */
fun Activity.hideNavBar() {
    window.hideNavBar()
}