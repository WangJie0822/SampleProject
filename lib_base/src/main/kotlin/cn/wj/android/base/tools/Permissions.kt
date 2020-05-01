@file:JvmName("PermissionsTools")
@file:Suppress("unused")

package cn.wj.android.base.tools

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import cn.wj.android.base.utils.AppManager

/**
 * 权限相关
 *
 * - 创建时间：2019/11/22
 *
 * @author 王杰
 */

/**
 * 通知是否打开
 *
 * @param context [Context] 对象
 *
 * @return 是否开启通知权限
 */
fun isNotificationEnable(context: Context = AppManager.getContext()): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}