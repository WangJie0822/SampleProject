@file:Suppress("unused")

package cn.wj.android.base.tools

import android.os.Environment
import cn.wj.android.base.utils.AppManager
import java.io.File

/* ----------------------------------------------------------------------------------------- */
/* |                                        清除相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 清除内部缓存
 * - directory: /data/data/package/cache
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanInternalCache(): Boolean {
    return deleteFilesInDir(AppManager.getApplication().cacheDir)
}

/**
 * 清除内部文件
 * - directory: /data/data/package/files
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanInternalFiles(): Boolean {
    return deleteFilesInDir(AppManager.getApplication().filesDir)
}

/**
 * 清除内部数据库
 * - directory: /data/data/package/databases
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanInternalDbs(): Boolean {
    return deleteFilesInDir(File(AppManager.getApplication().filesDir.parent, "databases"))
}

/**
 * 根据数据库名称清除内部数据库
 * - directory: /data/data/package/databases/dbName
 *
 * @param dbName 数据库名称
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanInternalDbByName(dbName: String): Boolean {
    return AppManager.getApplication().deleteDatabase(dbName)
}

/**
 * 清除内部 SP 文件
 * - directory: /data/data/package/shared_prefs
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanInternalSp(): Boolean {
    return deleteFilesInDir(File(AppManager.getApplication().filesDir.parent, "shared_prefs"))
}

/**
 * 清除外部缓存
 * - directory: /storage/emulated/0/android/data/package/cache
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanExternalCache(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && deleteFilesInDir(AppManager.getApplication().externalCacheDir)
}

/**
 * 清除自定义目录
 *
 * @param dirPath 自定义目录路径
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanCustomDir(dirPath: String): Boolean {
    return deleteFilesInDir(dirPath)
}

/**
 * 清除自定义目录
 *
 * @param dir 自定义目录文件对象
 *
 * @return `true`: 成功  `false`: 失败
 */
fun cleanCustomDir(dir: File): Boolean {
    return deleteFilesInDir(dir)
}

fun deleteFilesInDir(dirPath: String): Boolean {
    return deleteFilesInDir(getFileByPath(dirPath))
}

fun deleteFilesInDir(dir: File?): Boolean {
    if (dir == null) return false
    // dir doesn't exist then return true
    if (!dir.exists()) return true
    // dir isn't a directory then return false
    if (!dir.isDirectory) return false
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (file.isFile) {
                if (!file.delete()) return false
            } else if (file.isDirectory) {
                if (!deleteDir(file)) return false
            }
        }
    }
    return true
}

fun deleteDir(dir: File?): Boolean {
    if (dir == null) return false
    // dir doesn't exist then return true
    if (!dir.exists()) return true
    // dir isn't a directory then return false
    if (!dir.isDirectory) return false
    val files = dir.listFiles()
    if (files != null && files.isNotEmpty()) {
        for (file in files) {
            if (file.isFile) {
                if (!file.delete()) return false
            } else if (file.isDirectory) {
                if (!deleteDir(file)) return false
            }
        }
    }
    return dir.delete()
}

fun getFileByPath(filePath: String): File? {
    return if (isSpace(filePath)) null else File(filePath)
}

fun isSpace(s: String?): Boolean {
    if (s == null) return true
    var i = 0
    val len = s.length
    while (i < len) {
        if (!Character.isWhitespace(s[i])) {
            return false
        }
        ++i
    }
    return true
}