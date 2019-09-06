@file:Suppress("unused")

package cn.wj.android.base.tools

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import cn.wj.android.base.utils.AppManager

/* ----------------------------------------------------------------------------------------- */
/* |                                        资源相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 根据资源 id 获取颜色值
 *
 * @return 颜色值
 */
@ColorInt
fun Int.getColor(): Int {
    return getColor(AppManager.getContext())
}

/**
 * 根据资源 id 获取颜色值
 *
 * @param context [Context] 对象
 *
 * @return 颜色值
 */
@ColorInt
fun Int.getColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

/**
 * 根据资源 id 获取颜色值
 *
 * @param resId 颜色资源 id
 *
 * @return 颜色值
 */
@ColorInt
fun color(@ColorRes resId: Int): Int {
    return resId.getColor()
}

/**
 * 根据资源 id 获取颜色值
 *
 * @param context [Context] 对象
 * @param resId 颜色资源 id
 *
 * @return 颜色值
 */
@ColorInt
fun color(context: Context, @ColorRes resId: Int): Int {
    return resId.getColor(context)
}

/**
 * 根据资源 id 获取字符串
 *
 * @return 字符串
 */
fun Int.getString(): String {
    return getString(AppManager.getContext())
}

/**
 * 根据资源 id 获取字符串
 *
 * @param context [Context] 对象
 *
 * @return 字符串
 */
fun Int.getString(context: Context): String {
    return context.getString(this)
}

/**
 * 根据资源 id 获取字符串
 *
 * @param resId 字符串资源 id
 *
 * @return 字符串
 */
fun string(@StringRes resId: Int): String {
    return resId.getString()
}

/**
 * 根据资源 id 获取字符串
 *
 * @param context [Context] 对象
 * @param resId 字符串资源 id
 *
 * @return 字符串
 */
fun string(context: Context, @StringRes resId: Int): String {
    return resId.getString(context)
}

/**
 * 根据资源 id 获取 Drawable
 *
 * @return [Drawable] 对象
 */
fun Int.getDrawable(): Drawable? {
    return getDrawable(AppManager.getContext())
}

/**
 * 根据资源 id 获取 Drawable
 *
 * @param context [Context] 对象
 *
 * @return [Drawable] 对象
 */
fun Int.getDrawable(context: Context): Drawable? {
    return ContextCompat.getDrawable(context, this)
}

/**
 * 根据资源 id 获取 Drawable
 *
 * @param resId Drawable 资源 id
 *
 * @return [Drawable] 对象
 */
fun drawable(@DrawableRes resId: Int): Drawable? {
    return resId.getDrawable()
}

/**
 * 根据资源 id 获取 Drawable
 *
 * @param context [Context] 对象
 * @param resId Drawable 资源 id
 *
 * @return [Drawable] 对象
 */
fun drawable(context: Context, @DrawableRes resId: Int): Drawable? {
    return resId.getDrawable(context)
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @return 长度 px
 */
fun Int.getDimension(): Float {
    return getDimension(AppManager.getContext())
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param context [Context] 对象
 *
 * @return 长度 px
 */
fun Int.getDimension(context: Context): Float {
    return context.resources.getDimension(this)
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param resId 尺寸资源 id
 *
 * @return 长度 px
 */
fun dimension(@DimenRes resId: Int): Float {
    return resId.getDimension()
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param context [Context] 对象
 * @param resId 尺寸资源 id
 *
 * @return 长度 px
 */
fun dimension(context: Context, @DimenRes resId: Int): Float {
    return resId.getDimension(context)
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @return 长度 px
 */
fun Int.getDimensionPixelOffset(): Int {
    return getDimensionPixelOffset(AppManager.getContext())
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param context [Context] 对象
 *
 * @return 长度 px
 */
fun Int.getDimensionPixelOffset(context: Context): Int {
    return context.resources.getDimensionPixelOffset(this)
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param resId 尺寸资源 id
 *
 * @return 长度 px
 */
fun dimensionPixelOffset(@DimenRes resId: Int): Int {
    return resId.getDimensionPixelOffset()
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param context [Context] 对象
 * @param resId 尺寸资源 id
 *
 * @return 长度 px
 */
fun dimensionPixelOffset(context: Context, @DimenRes resId: Int): Int {
    return resId.getDimensionPixelOffset(context)
}

/**
 * 根据资源 id 字符串，获取资源 id
 *
 * @param defType 资源类型：资源所在文件夹名称
 *
 * @return 资源 id
 */
fun String.getIdentifier(defType: String): Int {
    return this.getIdentifier(AppManager.getContext(), defType)
}

/**
 * 根据资源 id 字符串，获取资源 id
 *
 * @param context [Context] 对象
 * @param defType 资源类型：资源所在文件夹名称
 *
 * @return 资源 id
 */
fun String.getIdentifier(context: Context, defType: String): Int {
    return context.resources.getIdentifier(this, defType, context.packageName)
}

/**
 * 根据资源 id 字符串，获取资源 id
 *
 * @param idStr 资源 id 字符串
 * @param defType 资源类型：资源所在文件夹名称
 *
 * @return 资源 id
 */
fun id(idStr: String, defType: String): Int {
    return idStr.getIdentifier(defType)
}

/**
 * 根据资源 id 字符串，获取资源 id
 *
 * @param context [Context] 对象
 * @param idStr 资源 id 字符串
 * @param defType 资源类型：资源所在文件夹名称
 *
 * @return 资源 id
 */
fun id(context: Context, idStr: String, defType: String): Int {
    return idStr.getIdentifier(context, defType)
}

/**
 * 修正 Resources
 * - 使得应用文字大小不跟随系统
 *
 * @param resource Activity 的 Resources 对象
 *
 * @return 修正后的 Resources 对象
 *
 * <pre>
 * override fun getResources(): Resources {
 *     return fixResources(super.getResources())
 * }
 * </pre>
 */
fun fixResources(resource: Resources): Resources {
    val config = Configuration()
    config.setToDefaults()
    @Suppress("DEPRECATION")
    resource.updateConfiguration(config, resource.displayMetrics)
    return resource
}