@file:Suppress("unused")
@file:JvmName("ImageTools")

package cn.wj.android.base.tools

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.view.View
import cn.wj.android.base.utils.AppManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/* ----------------------------------------------------------------------------------------- */
/* |                                        图片相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 将 Bitmap 转换成 ByteArray
 *
 * @param format Bitmap 格式
 *
 * @return ByteArray
 */
fun Bitmap.toBytes(format: Bitmap.CompressFormat): ByteArray {
    val baos = ByteArrayOutputStream()
    compress(format, 100, baos)
    return baos.toByteArray()
}

/**
 * 将 ByteArray 转换成 Bitmap
 *
 * @return Bitmap
 */
fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

/**
 * 将 Drawable 转换成 Bitmap
 *
 * @return Bitmap
 */
@Suppress("DEPRECATION")
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        if (bitmap != null) {
            return bitmap
        }
    }
    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(
                1, 1,
                if (opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
        )
    } else {
        Bitmap.createBitmap(
                intrinsicWidth, intrinsicHeight,
                if (opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
        )
    }
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

/**
 * 将 Bitmap 转换成 Drawable
 *
 * @return Drawable
 */
fun Bitmap.toDrawable(): Drawable {
    return BitmapDrawable(AppManager.getApplication().resources, this)
}

/**
 * 从 View 中获取 Bitmap 对象
 *
 * @param w 截取宽度 为 -1 时使用 View 宽度
 * @param h 截取高度 为 -1 时使用 View 高度
 *
 * @return Bitmap
 */
fun View.getBitmap(w: Int = -1, h: Int = -1): Bitmap {
    val cWidth = if (w != -1) {
        w
    } else {
        width
    }
    val cHeight = if (h != -1) {
        h
    } else {
        height
    }
    val ret = Bitmap.createBitmap(cWidth, cHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(ret)
    val bgDrawable = background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    draw(canvas)
    return ret
}

/**
 * 通过 File 对象获取 Bitmap 对象
 *
 * @return Bitmap
 */
fun File.getBitmap(): Bitmap {
    return absolutePath.getBitmap()
}

/**
 * 通过文件路径获取 Bitmap 对象
 *
 * @return Bitmap
 */
fun String.getBitmap(): Bitmap {
    return BitmapFactory.decodeFile(this)
}

/**
 * 将图片保存到手机
 *
 * @param dirName 文件夹名称
 *
 * @return 是否保存成功
 */
@Suppress("DEPRECATION")
fun Bitmap.saveToGallery(dirName: String): Boolean {
    // 首先保存图片
    val storePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + dirName
    val appDir = File(storePath)
    if (!appDir.exists()) {
        appDir.mkdir()
    }
    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val file = File(appDir, fileName)
    try {
        val fos = FileOutputStream(file)
        // 通过io流的方式来压缩保存图片
        val success = compress(Bitmap.CompressFormat.JPEG, 60, fos)
        fos.flush()
        fos.close()

        //把文件插入到系统图库
        //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

        //保存图片后发送广播通知更新数据库
        val uri = Uri.fromFile(file)
        AppManager.getApplication().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        return success
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}
