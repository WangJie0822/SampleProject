@file:Suppress("unused")
@file:JvmName("ImageTools")

package cn.wj.android.base.tools

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import cn.wj.android.base.utils.AppManager
import java.io.ByteArrayOutputStream
import java.io.File

/* ----------------------------------------------------------------------------------------- */
/* |                                        图片相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 将 Bitmap 转换成 ByteArray
 *
 * @param bitmap [Bitmap] 对象
 * @param format [Bitmap] 格式
 *
 * @return [ByteArray]
 *
 * @see Bitmap.CompressFormat.JPEG
 * @see Bitmap.CompressFormat.PNG
 * @see Bitmap.CompressFormat.WEBP
 */
fun bitmapToBytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(format, 100, baos)
    return baos.toByteArray()
}

/**
 * 将 [ByteArray] 转换成 [Bitmap]
 *
 * @param bytes [Byte] 数组
 *
 * @return [Bitmap]
 */
fun bytesToBitmap(bytes: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

/**
 * 将 [Drawable] 转换成 [Bitmap]
 *
 * @param drawable [Drawable] 对象
 *
 * @return [Bitmap]
 */
fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        if (drawable.bitmap != null) {
            return drawable.bitmap
        }
    }
    @Suppress("DEPRECATION")
    val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
                1, 1,
                if (drawable.opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
        )
    } else {
        Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
        )
    }
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

/**
 * 将 [Bitmap] 转换成 [Drawable]
 *
 * @param bitmap [Bitmap] 对象
 * @param context [Context] 对象
 *
 * @return [Drawable]
 */
@JvmOverloads
fun bitmapToDrawable(bitmap: Bitmap, context: Context = AppManager.getContext()): Drawable {
    return BitmapDrawable(context.resources, bitmap)
}

/**
 * 从 [View] 中获取 [Bitmap] 对象
 *
 * @param view [View] 对象
 * @param w 截取宽度 为 -1 时使用 View 宽度
 * @param h 截取高度 为 -1 时使用 View 高度
 *
 * @return [Bitmap]
 */
@JvmOverloads
fun getBitmapFromView(view: View, w: Int = -1, h: Int = -1): Bitmap {
    val cWidth = if (w != -1) {
        w
    } else {
        view.width
    }
    val cHeight = if (h != -1) {
        h
    } else {
        view.height
    }
    val ret = Bitmap.createBitmap(cWidth, cHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(ret)
    val bgDrawable = view.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    view.draw(canvas)
    return ret
}

/**
 * 通过 [File] 对象获取 [Bitmap] 对象
 *
 * @param file [File] 对象
 *
 * @return [Bitmap]
 */
fun getBitmapFromFile(file: File): Bitmap {
    return getBitmapFromPath(file.absolutePath)
}

/**
 * 通过文件路径获取 [Bitmap] 对象
 *
 * @param path 文件路径
 *
 * @return [Bitmap]
 */
fun getBitmapFromPath(path: String): Bitmap {
    return BitmapFactory.decodeFile(path)
}