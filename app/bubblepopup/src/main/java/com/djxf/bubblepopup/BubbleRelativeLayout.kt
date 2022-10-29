package com.djxf.bubblepopup

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class BubbleRelativeLayout (mContext: Context, attributeSet: AttributeSet?, style: Int) : RelativeLayout(mContext, attributeSet, style) {


    constructor(mContext: Context): this(mContext, null)
    constructor(mContext: Context, attributeSet: AttributeSet?) : this(mContext, attributeSet, 0)


    var PADDING = 30
    var STROKE_WIDTH = 2.0f
    var CORNER_RADIUS = 8.0f
    var SHADOW_COLOR = Color.BLUE

    private val mPath = Path()
    private val mBubbleLegPrototype = Path()

    private val mPaint = Paint(Paint.DITHER_FLAG)
    private var mBubbleLegOffset = 0.25f
    private var mBubbleOrientation = Gravity.TOP

    init {
        initView(attributeSet)
    }

    private fun initView(attributeSet: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.bubble)
        try {
            PADDING = typedArray.getDimensionPixelSize(R.styleable.bubble_padding, PADDING)
            SHADOW_COLOR = typedArray.getInt(R.styleable.bubble_shadowColor, SHADOW_COLOR)
            STROKE_WIDTH = typedArray.getFloat(R.styleable.bubble_strokeWidthBu, STROKE_WIDTH)
            CORNER_RADIUS = typedArray.getFloat(R.styleable.bubble_cornerRadiusBu, CORNER_RADIUS)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
        mPaint.color = SHADOW_COLOR
        mPaint.style = Paint.Style.FILL
        mPaint.strokeCap = Cap.BUTT
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = STROKE_WIDTH
        mPaint.strokeJoin = Paint.Join.MITER
        mPaint.pathEffect = CornerPathEffect(CORNER_RADIUS)
        setPadding(PADDING, PADDING, PADDING, PADDING)
        generateRenderBubbleLegPrototype()
        layoutParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    private fun generateRenderBubbleLegPrototype() {
        mBubbleLegPrototype.moveTo(0f, 0f)
        mBubbleLegPrototype.lineTo((PADDING * 1.5).toFloat(), (-PADDING/1.5).toFloat())
        mBubbleLegPrototype.lineTo((PADDING * 1.5).toFloat(), (PADDING/1.5).toFloat())
        mBubbleLegPrototype.close()
    }

    fun getOffsetWidthDistance() : Float {
        //必须手动执行measure() 负责无法得到宽度
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        return mBubbleLegOffset * measuredWidth
    }

    fun getOffsetHeightDistance() : Float {
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        return mBubbleLegOffset * measuredHeight
    }

    fun setBubbleLegOffset(offset: Float) = apply {
        this.mBubbleLegOffset = offset
    }

    fun getMeasureWidth(): Int {
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        return measuredWidth
    }

    fun setPaintColor(color: Int) = apply {
        this.mPaint.color = color
        mPaint.setShadowLayer(2f, 2f, 5f, color)
    }

    fun setBubbleOrientation(orientation: Int) = apply {
        this.mBubbleOrientation = orientation
    }


    private fun renderBubbleLegMatrix(width: Int, height: Int): Matrix {
        val offset = mBubbleLegOffset
        var dstX = 0f
        var dstY = 0f
        val matrix = Matrix()
        when (mBubbleOrientation) {
            Gravity.TOP -> {
                dstX = width * offset
                dstY = height.toFloat()
                matrix.postRotate(270f)
            }
            Gravity.RIGHT -> {
                dstX = 0f
                dstY = height * offset
            }
            Gravity.BOTTOM -> {
                dstX = width * offset
                dstY = 0f
                matrix.postRotate(90f)
            }
            Gravity.LEFT -> {
                dstX = width.toFloat()
                dstY = height * offset
                matrix.postRotate(180f)
            }
            else -> {}
        }
        matrix.postTranslate(dstX, dstY)
        return matrix
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.rewind()
        mPath.addRoundRect(RectF(PADDING.toFloat(), PADDING.toFloat(),
            (width - PADDING).toFloat(),
            (height - PADDING).toFloat()), CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW)
        if (canvas != null) {
            mPath.addPath(mBubbleLegPrototype, renderBubbleLegMatrix(width, height))
        }
        canvas?.drawPath(mPath, mPaint)
    }
}