@file:Suppress("unused")
@file:JvmName("AppTools")

package cn.wj.android.base.tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

/**
 * 应用相关
 *
 * - 创建时间：2019/10/31
 *
 * @author 王杰
 */

/** 算法 - MD5 */
const val ALGORITHM_MD5 = "MD5"

/** 算法 - SHA1 */
const val ALGORITHM_SHA1 = "SHA1"

/** 算法 - SHA256 */
const val ALGORITHM_SHA256 = "SHA256"

/**
 * 获取应用的签名数据
 *
 * @param context [Context] 对象
 * @param algorithm 签名算法
 *
 *  @see ALGORITHM_MD5
 *  @see ALGORITHM_SHA1
 *  @see ALGORITHM_SHA256
 */
@JvmOverloads
fun getSignature(context: Context, algorithm: String, packageName: String? = null): String {
    // 包管理器
    val pm = context.packageManager
    // 获取已安装应用列表
    val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pm.getInstalledPackages(PackageManager.GET_SIGNING_CERTIFICATES)
    } else {
        @Suppress("DEPRECATION")
        pm.getInstalledPackages(PackageManager.GET_SIGNATURES)
    }
    
    // 获取要获取的应用的包名
    val pName = if (packageName.isNullOrBlank()) {
        context.packageName
    } else {
        packageName
    }
    
    // 获取应用信息对象
    val app = apps.firstOrNull { it.packageName == pName } ?: return ""
    
    // 获取签名信息
    val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        app.signingInfo.apkContentsSigners
    } else {
        @Suppress("DEPRECATION")
        app.signatures
    }
    if (signatures.isNullOrEmpty()) {
        // 没有签名信息
        return ""
    }
    val signature = signatures[0]
    
    // 获取签名数据
    val info = MessageDigest.getInstance(algorithm)
    info.update(signature.toByteArray())
    return bytesToHexString(info.digest())
}
