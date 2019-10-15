@file:Suppress("unused")
@file:JvmName("DeviceTools")

package cn.wj.android.base.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import cn.wj.android.base.utils.AppManager
import java.io.File
import java.util.*

/* ----------------------------------------------------------------------------------------- */
/* |                                        设备相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/** 设备是否是手机 */
val IS_PHONE: Boolean
    get() = (AppManager.getApplication().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).phoneType != TelephonyManager.PHONE_TYPE_NONE

/** 设备是否 ROOT */
val IS_DEVICE_ROOTED: Boolean
    get() {
        val su = "su"
        val locations = arrayOf(
                "/system/bin/",
                "/system/xbin/",
                "/sbin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/data/local/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

/** 设备系统语言 */
val DEVICE_LANGUAGE: String
    get() = Locale.getDefault().language

/** 设备系统语言列表(Locale列表) */
val DEVICE_LANGUAGES: Array<Locale>
    get() = Locale.getAvailableLocales()

/** 设备系统版本名 */
val DEVICE_VERSION_NAME: String
    get() = Build.VERSION.RELEASE

/** 设备系统版本号 */
val DEVICE_VERSION_CODE: Int
    get() = Build.VERSION.SDK_INT

/** 设备型号 */
val DEVICE_MODEL: String
    get() {
        var model: String? = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }

/** 设备厂商 */
val DEVICE_BRAND: String
    get() = Build.BRAND

/** 设备 IMEI */
val DEVICE_IMEI: String
    @SuppressLint("HardwareIds")
    get() {
        var imei: String?
        val context = AppManager.getApplication()
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        @Suppress("DEPRECATION")
        imei = telephonyManager.deviceId
        if (null == imei) {
            imei = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if ("9774d56d682e549c" == imei) {
                // 某些主流设备 android_id是固定的
                imei = ""
            }
        }
        if (null == imei) {
            imei = ""
        }
        return imei
    }

/** 屏幕宽度 单位：px */
val DEVICE_SCREEN_WIDTH: Int
    get() = AppManager.getApplication().resources.displayMetrics.widthPixels

/** 屏幕高度 单位：px */
val DEVICE_SCREEN_HEIGHT: Int
    get() = AppManager.getApplication().resources.displayMetrics.heightPixels

/**
 * 获取设备 ABIs
 *
 * @return 设备 ABIs 列表
 */
@Suppress("DEPRECATION")
fun getABIs(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS
    } else {
        if (Build.CPU_ABI2.isNotEmpty()) {
            arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
        } else arrayOf(Build.CPU_ABI)
    }
}

/**
 * 是否有网络
 */
fun isNetworkReachable(): Boolean {
    val cm = AppManager.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    @Suppress("DEPRECATION")
    return cm.activeNetworkInfo?.isAvailable ?: false
}
