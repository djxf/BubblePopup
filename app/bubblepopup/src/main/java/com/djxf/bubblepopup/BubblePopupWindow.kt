package com.djxf.bubblepopup

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow

class BubblePopupWindowCustom constructor(builder: Builder) : PopupWindow(builder.context) {

    var mContext: Context = builder.context
    var mShowView: View = builder.mShowView
    private val mAnchor: View = builder.mAnchor
    private var mGravity: Int = builder.mGravity
    private var mBubbleOffset: Float = builder.mBubbleOffset
    private var mFillColor: Int = builder.mFillColor
    private var finalWidth = 0

    init {
        isTouchable = true
        isFocusable = true
        width = LayoutParams.WRAP_CONTENT
        height = LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(0))
    }


    /**
     * 显示弹窗
     */
    fun show() {




        val bubbleRelativeLayout = BubbleRelativeLayout(mContext)
        bubbleRelativeLayout.addView(mShowView)
        bubbleRelativeLayout.setBackgroundColor(Color.TRANSPARENT)
        bubbleRelativeLayout.setBubbleLegOffset(mBubbleOffset)
        bubbleRelativeLayout.setPaintColor(mFillColor)
        bubbleRelativeLayout.setBubbleOrientation(mGravity)
        contentView = bubbleRelativeLayout
        val location = IntArray(2)
        mAnchor.getLocationOnScreen(location)
        val resources: Resources = mContext.getResources()
        val dm = resources.displayMetrics
        val screenWidth = dm.widthPixels
        if (mGravity == Gravity.BOTTOM && getMeasuredWidth() >= screenWidth - (location[0] * 2 + mAnchor.width) / 2) {
            finalWidth = 2 * (screenWidth - (location[0] * 2 + mAnchor.width) / 2)
            width = finalWidth
        } else if (getMeasuredWidth() / 2 >= location[0] + mAnchor.width / 2) {
            finalWidth = (location[0] + mAnchor.width / 2) * 2
            width = finalWidth
        } else if (mGravity == Gravity.LEFT) {
            if (getMeasuredWidth() >= location[0]) {
                finalWidth = location[0]
                width = finalWidth
            }
        }

        if (!isShowing) {
            val location = IntArray(2) {
                return@IntArray 0
            }
            mAnchor.getLocationOnScreen(location)
            when (mGravity) {
                Gravity.BOTTOM -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    (location[0]),
                    location[1] + mAnchor.height
                )
                Gravity.TOP -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    (location[0] + mAnchor.width / 2 - bubbleRelativeLayout.getOffsetWidthDistance()).toInt(),
                    location[1] + mAnchor.height
                )
                Gravity.RIGHT -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    location[0] + mAnchor.width,
                    (location[1] + mAnchor.height/2 - bubbleRelativeLayout.getOffsetHeightDistance()).toInt()
                )
                Gravity.LEFT -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    location[0] - bubbleRelativeLayout.getMeasureWidth(),
                    (location[1] - bubbleRelativeLayout.getOffsetHeightDistance() + mAnchor.height/2).toInt()
                )
                else -> {}
            }
        } else {
            dismiss()
        }
    }


    private fun getMeasuredWidth(): Int {
        contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val popWidth = contentView.measuredWidth
        return if (finalWidth == 0) popWidth else finalWidth
    }

    private fun getMeasureHeight(): Int {
        contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)

        return contentView.measuredHeight
    }


    class Builder(val context: Context, var mShowView: View, val mAnchor: View) {

        //尖角方向朝向
        var mGravity: Int = Gravity.TOP
        var mBubbleOffset: Float = 0.5f
        var mFillColor: Int = Color.BLACK

        fun gravity(gravity: Int) = apply {
            this.mGravity = gravity
        }

        fun bubbleOffset(offset: Float) = apply {
            if (offset < 0 || offset > 1) {
                throw IllegalStateException("offset cannot < 0 or > 1")
            }
            this.mBubbleOffset = offset
        }

        fun fillColor(fillColor: Int) = apply {
            this.mFillColor = fillColor
        }

        fun build(): BubblePopupWindowCustom = BubblePopupWindowCustom(builder = this)
    }
}