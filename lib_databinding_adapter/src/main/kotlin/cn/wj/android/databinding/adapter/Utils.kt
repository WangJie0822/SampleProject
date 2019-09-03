package cn.wj.android.databinding.adapter

import android.content.Context

/**
 * 根据资源 id 字符串，获取资源 id
 *
 * @param context [Context] 对象
 * @param defType 资源类型：资源所在文件夹名称
 *
 * @return 资源 id
 */
internal fun String.getIdentifier(context: Context, defType: String): Int {
    return context.resources.getIdentifier(this, defType, context.packageName)
}