@file:Suppress("unused")
@file:JvmName("ResourceTools")

package cn.wj.android.base.tools

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import cn.wj.android.base.log.InternalLog
import cn.wj.android.base.utils.AppManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                        资源相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 根据资源 id 获取颜色值
 *
 * @param colorResId 颜色资源 id
 * @param context [Context] 对象
 *
 * @return 颜色值
 */
@ColorInt
@JvmOverloads
fun getColorById(@ColorRes colorResId: Int, context: Context = AppManager.getContext()): Int {
    return ContextCompat.getColor(context, colorResId)
}

/**
 * 根据资源 id 获取字符串
 *
 * @param resId 字符串资源 id
 * @param context [Context] 对象
 *
 * @return 字符串
 */
@JvmOverloads
fun getStringById(@StringRes resId: Int, context: Context = AppManager.getContext()): String {
    return context.getString(resId)
}

/**
 * 根据资源 id 获取 Drawable
 *
 * @param resId Drawable 资源 id
 * @param context [Context] 对象
 *
 * @return [Drawable] 对象 可能为空
 */
@JvmOverloads
fun getDrawableById(@DrawableRes resId: Int, context: Context = AppManager.getContext()): Drawable? {
    return ContextCompat.getDrawable(context, resId)
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param resId 尺寸资源 id
 * @param context [Context] 对象
 *
 * @return 长度 px
 */
@JvmOverloads
fun getFloatDimensionById(@DimenRes resId: Int, context: Context = AppManager.getContext()): Float {
    return context.resources.getDimension(resId)
}

/**
 * 根据资源 id 获取 尺寸
 *
 * @param resId 尺寸资源 id
 * @param context [Context] 对象
 *
 * @return 长度 px
 */
@JvmOverloads
fun getIntDimensionById(@DimenRes resId: Int, context: Context = AppManager.getContext()): Int {
    return context.resources.getDimensionPixelOffset(resId)
}

/**
 * 根据资源 id 字符串，获取资源 id
 *
 * @param idStr 资源 id 字符串
 * @param defType 资源类型：资源所在文件夹名称
 * @param context [Context] 对象
 *
 * @return 资源 id
 */
@JvmOverloads
fun getIdByString(idStr: String, defType: String, context: Context = AppManager.getContext()): Int {
    return context.resources.getIdentifier(idStr, defType, context.packageName)
}

/** 默认字体规模 */
const val RESOURCE_DEFAULT_FONT_SCALE = 1f

/**
 * 修正 Resources
 * - 使得应用文字大小不跟随系统
 *
 * @param resource Activity 的 Resources 对象
 * @param context [Context] 对象
 *
 * @return 修正后的 Resources 对象
 *
 * ```
 * override fun getResources(): Resources {
 *     return fixFontScaleResources(super.getResources(), applicationContext)
 * }
 * ```
 */
@JvmOverloads
fun fixFontScaleResources(resource: Resources?, context: Context? = null): Resources? {
    return if (resource == null) {
        null
    } else {
        val config = resource.configuration
        if (config.fontScale != RESOURCE_DEFAULT_FONT_SCALE) {
            // 不是默认字体规模，修复
            config.fontScale = RESOURCE_DEFAULT_FONT_SCALE
            @Suppress("DEPRECATION")
            resource.updateConfiguration(config, resource.displayMetrics)
            context?.createConfigurationContext(config)
        }
        resource
    }
}

/**
 * 从 Assets 中打开文件
 *
 * @param fileName 文件名
 * @param context [Context] 对象
 *
 * @return 文件输入流
 */
@JvmOverloads
fun getAssetsStreamByName(fileName: String, context: Context = AppManager.getContext()): InputStream? {
    return try {
        context.assets.open(fileName)
    } catch (e: IOException) {
        InternalLog.e("Resource", "assetsFile", e)
        null
    }
}

/**
 * 从 Assets 读取字符串
 *
 * @param fileName 文件名
 * @param context [Context] 对象
 *
 * @return 文件内容文本
 */
@JvmOverloads
fun getAssetsStringByName(fileName: String, context: Context = AppManager.getContext()): String? {
    return try {
        val br = BufferedReader(InputStreamReader(getAssetsStreamByName(fileName, context)))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = br.readLine()
            if (line != null) {
                sb.append(line)
            } else {
                break
            }
        }
        sb.toString()
    } catch (e: IOException) {
        null
    }
}

/**
 * 读取 Raw 流
 *
 * @param rawResId Raw 资源 id
 * @param context [Context] 对象，默认[AppManager.getContext]
 *
 * @return 流对象
 */
@JvmOverloads
fun getRawStreamById(@RawRes rawResId: Int, context: Context = AppManager.getContext()): InputStream? {
    return try {
        context.resources.openRawResource(rawResId)
    } catch (e: Resources.NotFoundException) {
        InternalLog.e("Resource", "rawStream", e)
        null
    }
}

/**
 * 读取 Raw 流
 *
 * @param rawResId Raw 资源 id
 * @param key 关键字
 * @param context [Context] 对象，默认[AppManager.getContext]
 * @param defaultValue 默认值
 *
 * @return 流对象
 */
@JvmOverloads
fun getRawValue(@RawRes rawResId: Int, key: String, context: Context = AppManager.getContext(), defaultValue: String = ""): String {
    val rawStream = getRawStreamById(rawResId, context)
    var result = defaultValue
    if (null != rawStream) {
        try {
            val pro = Properties().apply { load(rawStream) }
            result = pro.getProperty(key, defaultValue)
        } catch (e: IOException) {
            InternalLog.e("Resource", "rawValue", e)
        } finally {
            try {
                rawStream.close()
            } catch (e: IOException) {
                InternalLog.e("Resource", "rawValue", e)
            }
        }
    }
    return result
}