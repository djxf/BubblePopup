package com.djxf.myapplication;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 气泡布局
 */
public class BubbleRelativeLayout extends RelativeLayout {

    /**
     * 气泡尖角方向
     */
    public enum BubbleLegOrientation {
        TOP, LEFT, RIGHT, BOTTOM, NONE
    }

    public static int PADDING = 30;
    public static int LEG_HALF_BASE = 30;
    public static float STROKE_WIDTH = 2.0f;
    public static float CORNER_RADIUS = 8.0f;
    public static int SHADOW_COLOR = Color.BLACK;
    public static float MIN_LEG_DISTANCE = PADDING + LEG_HALF_BASE;

    private final Path mPath = new Path();
    private final Path mBubbleLegPrototype = new Path();
    private final Paint mPaint = new Paint(Paint.DITHER_FLAG);

    private float mBubbleLegOffset = 0.75f;
    private BubbleLegOrientation mBubbleOrientation = BubbleLegOrientation.LEFT;

    public BubbleRelativeLayout(Context context) {
        this(context, null);
    }

    public BubbleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.bubble);

            try {
                PADDING = a.getDimensionPixelSize(R.styleable.bubble_padding, PADDING);
                SHADOW_COLOR = a.getInt(R.styleable.bubble_shadowColor, SHADOW_COLOR);
                LEG_HALF_BASE = a.getDimensionPixelSize(R.styleable.bubble_halfBaseOfLeg, LEG_HALF_BASE);
                MIN_LEG_DISTANCE = PADDING + LEG_HALF_BASE;
                STROKE_WIDTH = a.getFloat(R.styleable.bubble_strokeWidthBu, STROKE_WIDTH);
                CORNER_RADIUS = a.getFloat(R.styleable.bubble_cornerRadiusBu, CORNER_RADIUS);
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }

        mPaint.setColor(SHADOW_COLOR);
        mPaint.setStyle(Style.FILL);
        mPaint.setStrokeCap(Cap.BUTT);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setPathEffect(new CornerPathEffect(CORNER_RADIUS));

        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);

        Paint mFillPaint = new Paint(mPaint);
        mFillPaint.setColor(Color.BLACK);
        mFillPaint.setShader(new LinearGradient(100f, 0f, 100f, 200f, Color.WHITE, Color.WHITE, TileMode.CLAMP));

        setLayerType(LAYER_TYPE_SOFTWARE, mFillPaint);
        mPaint.setShadowLayer(2f, 2F, 5F, SHADOW_COLOR);

        renderBubbleLegPrototype();

        setPadding(PADDING, PADDING, PADDING, PADDING);

    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 尖角path
     */
    private void renderBubbleLegPrototype() {
        mBubbleLegPrototype.moveTo(0, 0);
        mBubbleLegPrototype.lineTo(PADDING * 1.5f, -PADDING / 1.5f);
        mBubbleLegPrototype.lineTo(PADDING * 1.5f, PADDING / 1.5f);
        mBubbleLegPrototype.close();
    }

    public void setBubbleParams(final BubbleLegOrientation bubbleOrientation, final float bubbleOffset) {
        mBubbleLegOffset = bubbleOffset;
        mBubbleOrientation = bubbleOrientation;
    }

    /**
     * 根据显示方向，获取尖角位置矩阵
     * @param width
     * @param height
     * @return
     */
    private Matrix renderBubbleLegMatrix(final float width, final float height) {

        final float offset = Math.max(mBubbleLegOffset, MIN_LEG_DISTANCE);

        float dstX = 0;
        float dstY = Math.min(offset, height - MIN_LEG_DISTANCE);
        final Matrix matrix = new Matrix();

        switch (mBubbleOrientation) {

            case TOP:
                dstX = Math.min(offset, width - MIN_LEG_DISTANCE);
                dstY = 0;
                matrix.postRotate(90);
                break;

            case RIGHT:
                dstX = width;
                dstY = Math.min(offset, height - MIN_LEG_DISTANCE);
                matrix.postRotate(180);
                break;

            case BOTTOM:
                dstX = Math.min(offset, width - MIN_LEG_DISTANCE);
                dstY = height;
                matrix.postRotate(270);
                break;

        }

        matrix.postTranslate(dstX, 0);
//        matrix.postTranslate(0, dstY);
        return matrix;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final float width = canvas.getWidth();
        final float height = canvas.getHeight();

        mPath.rewind();
        mPath.addRoundRect(new RectF(PADDING, PADDING, width - PADDING, height - PADDING), CORNER_RADIUS, CORNER_RADIUS, Direction.CW);
        mPath.addPath(mBubbleLegPrototype, renderBubbleLegMatrix(width, height));

        canvas.drawPath(mPath, mPaint);
//        canvas.scale((width - STROKE_WIDTH) / width, (height - STROKE_WIDTH) / height, width / 2f, height / 2f);
//
//        canvas.drawPath(mPath, mFillPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}