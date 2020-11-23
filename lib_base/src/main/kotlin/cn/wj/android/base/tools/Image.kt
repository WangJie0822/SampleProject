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
 * 将位图[bitmap] 根据格式[format] 转换成 [ByteArray]
 * > [format] 参见 [Bitmap.CompressFormat]
 */
fun bitmapToBytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(format, 100, baos)
    return baos.toByteArray()
}

/** 将Byte数组[bytes] 转换成 [Bitmap] */
fun bytesToBitmap(bytes: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

/** 将 [Drawable] 转换成 [Bitmap] */
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

/** 将位图[bitmap] 转换成 [Drawable] */
@JvmOverloads
fun bitmapToDrawable(bitmap: Bitmap, context: Context = AppManager.getContext()): Drawable {
    return BitmapDrawable(context.resources, bitmap)
}

/**
 * 从控件[view] 中根据宽度[w]、高度[h] 获取 [Bitmap]
 * > [w] 截取宽度 为 -1 时使用 View 宽度 & [h] 截取高度 为 -1 时使用 View 高度
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

/** 通过文件[file] 对象获取 [Bitmap] 对象 */
fun getBitmapFromFile(file: File): Bitmap {
    return getBitmapFromPath(file.absolutePath)
}

/** 通过文件路径[path] 获取 [Bitmap] 对象 */
fun getBitmapFromPath(path: String): Bitmap {
    return BitmapFactory.decodeFile(path)
}