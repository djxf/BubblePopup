package com.djxf.bubblepopup

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout

class BubbleRelativeLayout (mContext: Context, attributeSet: AttributeSet?, style: Int) : RelativeLayout(mContext, attributeSet, style) {


    constructor(mContext: Context): this(mContext, null)
    constructor(mContext: Context, attributeSet: AttributeSet?) : this(mContext, attributeSet, 0)


    var PADDING = 30
    var LEG_HALF_BASE = 30
    var STROKE_WIDTH = 2.0f
    var CORNER_RADIUS = 8.0f
    var SHADOW_COLOR = Color.BLUE
    var MIN_LEG_DISTANCE = (PADDING + LEG_HALF_BASE).toFloat()

    private val mPath = Path()
    private val mBubbleLegPrototype = Path()

    private val mPaint = Paint(Paint.DITHER_FLAG)
    private val mBubbleLegOffset = 0.75f
    private val mBubbleOrientation: BubbleLegOrientation = BubbleLegOrientation.TOP

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
            MIN_LEG_DISTANCE = typedArray.getFloat(R.styleable.bubble_halfBaseOfLeg, MIN_LEG_DISTANCE)
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
        mPaint.setShadowLayer(2f, 2f, 5f, SHADOW_COLOR)
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

    private fun renderBubbleLegMatrix(width: Int, height: Int): Matrix {
        val offset =
            Math.max(mBubbleLegOffset, MIN_LEG_DISTANCE)
        var dstX = 0f
        var dstY =
            Math.min(offset, height - MIN_LEG_DISTANCE)
        val matrix = Matrix()
        when (mBubbleOrientation) {
            BubbleLegOrientation.TOP -> {
                dstX = Math.min(
                    offset,
                    width - MIN_LEG_DISTANCE
                )
                dstY = 0f
                matrix.postRotate(90f)
            }
            BubbleLegOrientation.RIGHT -> {
                dstX = width.toFloat()
                dstY = Math.min(
                    offset,
                    height - MIN_LEG_DISTANCE
                )
                matrix.postRotate(180f)
            }
            BubbleLegOrientation.BOTTOM -> {
                dstX = Math.min(
                    offset,
                    width - MIN_LEG_DISTANCE
                )
                dstY = height.toFloat()
                matrix.postRotate(270f)
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

    enum class BubbleLegOrientation {
        TOP, LEFT, RIGHT, BOTTOM, NONE
    }
}