package cn.wj.android.views.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import kotlin.math.min

/**
 * 圆形 ImageView
 * - 所有图片都会显示为圆形
 *
 * @author 王杰
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class CircleImageView
    : AppCompatImageView {

    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBorderPaint = Paint()
    private val mFillPaint = Paint()
    private val mTextPaint = Paint()

    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH
    private var mFillColor = DEFAULT_FILL_COLOR
    var textString: String? = null
        private set
    private var mTextColor = DEFAULT_TEXT_COLOR
    private var mTextSize = DEFAULT_TEXT_SIZE
    private var mTextPadding = DEFAULT_TEXT_PADDING

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0

    private var mDrawableRadius: Float = 0.toFloat()
    private var mBorderRadius: Float = 0.toFloat()

    private var mColorFilter: ColorFilter? = null

    private var mReady: Boolean = false
    private var mSetupPending: Boolean = false
    private var mBorderOverlay: Boolean = false

    var textColor: Int
        get() = mTextColor
        set(@ColorInt mTextColor) {
            this.mTextColor = mTextColor
            mTextPaint.color = mTextColor
            invalidate()
        }

    var textSize: Int
        get() = mTextSize
        set(textSize) {
            this.mTextSize = textSize
            mTextPaint.textSize = textSize.toFloat()
            invalidate()
        }

    var textPadding: Int
        get() = mTextPadding
        set(mTextPadding) {
            this.mTextPadding = mTextPadding
            invalidate()
        }

    var borderColor: Int
        get() = mBorderColor
        set(@ColorInt borderColor) {
            if (borderColor == mBorderColor) {
                return
            }

            mBorderColor = borderColor
            mBorderPaint.color = mBorderColor
            invalidate()
        }

    var fillColor: Int
        get() = mFillColor
        set(@ColorInt fillColor) {
            if (fillColor == mFillColor) {
                return
            }

            mFillColor = fillColor
            mFillPaint.color = fillColor
            invalidate()
        }

    var borderWidth: Int
        get() = mBorderWidth
        set(borderWidth) {
            if (borderWidth == mBorderWidth) {
                return
            }

            mBorderWidth = borderWidth
            setup()
        }

    var isBorderOverlay: Boolean
        get() = mBorderOverlay
        set(borderOverlay) {
            if (borderOverlay == mBorderOverlay) {
                return
            }

            mBorderOverlay = borderOverlay
            setup()
        }

    constructor(context: Context) : super(context) {
        init()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_citv_border_width, DEFAULT_BORDER_WIDTH)
        mBorderColor = a.getColor(R.styleable.CircleImageView_citv_border_color, DEFAULT_BORDER_COLOR)
        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_citv_border_overlay, DEFAULT_BORDER_OVERLAY)
        mFillColor = a.getColor(R.styleable.CircleImageView_citv_fill_color, DEFAULT_FILL_COLOR)
        textString = a.getString(R.styleable.CircleImageView_citv_text_text)
        mTextColor = a.getColor(R.styleable.CircleImageView_citv_border_color, DEFAULT_TEXT_COLOR)
        mTextSize = a.getDimensionPixelSize(R.styleable.CircleImageView_citv_text_size, DEFAULT_TEXT_SIZE)
        mTextPadding = a.getDimensionPixelSize(R.styleable.CircleImageView_citv_text_padding, DEFAULT_TEXT_PADDING)
        a.recycle()

        init()
    }

    private fun init() {
        super.setScaleType(SCALE_TYPE)
        mReady = true

        if (mSetupPending) {
            setup()
            mSetupPending = false
        }
    }

    override fun getScaleType(): ScaleType {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ScaleType) {
        require(scaleType == SCALE_TYPE) { String.format("ScaleType %s not supported.", scaleType) }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap == null && TextUtils.isEmpty(textString)) {
            return
        }

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(width / 2.0f, height / 2.0f, mDrawableRadius, mFillPaint)
        }
        if (mBitmap != null) {
            canvas.drawCircle(width / 2.0f, height / 2.0f, mDrawableRadius, mBitmapPaint)
        }
        if (mBorderWidth != 0) {
            canvas.drawCircle(width / 2.0f, height / 2.0f, mBorderRadius, mBorderPaint)
        }

        if (!TextUtils.isEmpty(textString)) {
            val fm = mTextPaint.fontMetricsInt
            canvas.drawText(
                    textString!!,
                    width / 2 - mTextPaint.measureText(textString) / 2,
                    (height / 2 - fm.descent + (fm.bottom - fm.top) / 2).toFloat(), mTextPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }


    fun setText(@StringRes TextResId: Int) {
        setText(resources.getString(TextResId))
    }

    fun setText(textString: String) {
        this.textString = textString
        invalidate()

    }

    fun setTextColorResource(@ColorRes colorResource: Int) {
        textColor = ContextCompat.getColor(context, colorResource)
    }

    fun setBorderColorResource(@ColorRes borderColorRes: Int) {
        borderColor = ContextCompat.getColor(context, borderColorRes)
    }

    fun setFillColorResource(@ColorRes fillColorRes: Int) {
        fillColor = ContextCompat.getColor(context, fillColorRes)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mBitmap = if (uri != null) getBitmapFromDrawable(drawable) else null
        setup()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === mColorFilter) {
            return
        }

        mColorFilter = cf
        mBitmapPaint.colorFilter = mColorFilter
        invalidate()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }

        if (width == 0 && height == 0) {
            return
        }



        if (mBitmap == null && TextUtils.isEmpty(textString)) {
            invalidate()
            return
        }



        mTextPaint.isAntiAlias = true
        mTextPaint.color = mTextColor
        mTextPaint.textSize = mTextSize.toFloat()


        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()

        mFillPaint.style = Paint.Style.FILL
        mFillPaint.isAntiAlias = true
        mFillPaint.color = mFillColor



        mBorderRect.set(0f, 0f, width.toFloat(), height.toFloat())
        mBorderRadius =
                min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f)

        mDrawableRect.set(mBorderRect)
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth.toFloat(), mBorderWidth.toFloat())
        }
        mDrawableRadius = min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)

        if (mBitmap != null) {
            mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            mBitmapHeight = mBitmap!!.height
            mBitmapWidth = mBitmap!!.width
            mBitmapPaint.isAntiAlias = true
            mBitmapPaint.shader = mBitmapShader
            updateShaderMatrix()
        }
        invalidate()
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate((dx + 0.5f).toInt() + mDrawableRect.left, (dy + 0.5f).toInt() + mDrawableRect.top)

        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec)

        if (!TextUtils.isEmpty(textString)) {
            var textMeasuredSize = mTextPaint.measureText(textString).toInt()
            textMeasuredSize += 2 * mTextPadding
            if (widthMeasureSpecMode == MeasureSpec.AT_MOST && heightMeasureSpecMode == MeasureSpec.AT_MOST) {
                if (textMeasuredSize > measuredWidth || textMeasuredSize > measuredHeight) {
                    setMeasuredDimension(textMeasuredSize, textMeasuredSize)
                }
            }
        }

    }

    companion object {

        private val SCALE_TYPE = ScaleType.CENTER_CROP

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 2

        private const val DEFAULT_BORDER_WIDTH = 0
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_FILL_COLOR = Color.TRANSPARENT
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_SIZE = 22
        private const val DEFAULT_TEXT_PADDING = 4
        private const val DEFAULT_BORDER_OVERLAY = false
    }
}
