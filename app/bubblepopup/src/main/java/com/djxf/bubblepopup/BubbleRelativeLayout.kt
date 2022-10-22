package com.djxf.bubblepopup

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.util.AttributeSet
import android.widget.RelativeLayout

class BubbleRelativeLayout(mContext: Context, attributeSet: AttributeSet?, style: Int) : RelativeLayout(mContext, attributeSet, style) {


    constructor(mContext: Context): this(mContext, null)
    constructor(mContext: Context, attributeSet: AttributeSet?) : this(mContext, attributeSet, 0)

    init {
        initView(attributeSet)
    }

    var PADDING = 30
    var LEG_HALF_BASE = 30
    var STROKE_WIDTH = 2.0f
    var CORNER_RADIUS = 8.0f
    var SHADOW_COLOR = Color.BLACK
    var MIN_LEG_DISTANCE = (PADDING + LEG_HALF_BASE).toFloat()

    private val mPath = Path()
    private val mBubbleLegPrototype = Path()
    private val mRoundRect = RectF(PADDING.toFloat(), PADDING.toFloat(),
        (width - PADDING).toFloat(),
        (height-PADDING).toFloat()
    )
    private val mPaint = Paint(Paint.DITHER_FLAG)
    private val mBubbleLegOffset = 0.75f
    private val mBubbleOrientation: BubbleRelativeLayout.BubbleLegOrientation = BubbleLegOrientation.LEFT

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
        generateRenderBubbleLegPrototype()
    }

    fun generateRenderBubbleLegPrototype() {
        mBubbleLegPrototype.moveTo(0f, 0f)
        mBubbleLegPrototype.lineTo((PADDING * 1.5).toFloat(), (-PADDING/1.5).toFloat())
        mBubbleLegPrototype.lineTo((PADDING * 1.5).toFloat(), (PADDING/1.5).toFloat())
        mBubbleLegPrototype.close()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.rewind()
        mPath.addRoundRect(mRoundRect, CORNER_RADIUS, CORNER_RADIUS, Path.Direction.CW)
        mPath.addPath(mBubbleLegPrototype)
        canvas?.drawPath(mPath, mPaint)
    }

    enum class BubbleLegOrientation {
        TOP, LEFT, RIGHT, BOTTOM, NONE
    }
}