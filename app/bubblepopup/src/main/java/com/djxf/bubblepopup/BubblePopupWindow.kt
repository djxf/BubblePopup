package com.djxf.bubblepopup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow

class BubblePopupWindowCustom constructor(builder: Builder) : PopupWindow(builder.context) {

    var mContext: Context = builder.context
    var mShowView: View = builder.mShowView
    private val mAnchor: View = builder.mAnchor
    private var mGravity: Int = builder.mGravity
    var mBubbleOffset: Float = builder.mBubbleOffset

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
        contentView = bubbleRelativeLayout
        if (!isShowing) {
            val location = IntArray(2) {
                return@IntArray 0
            }
            mAnchor.getLocationOnScreen(location)
            when (mGravity) {
                Gravity.BOTTOM -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    (location[0] + mAnchor.width / 2 - mShowView.paddingLeft * 2.5).toInt(),
                    location[1] + mAnchor.height
                )
                Gravity.TOP -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    location[0],
                    location[1] - getMeasureHeight()
                )
                Gravity.RIGHT -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    location[0] + mAnchor.width,
                    location[1] - mAnchor.height / 2
                )
                Gravity.LEFT -> showAtLocation(
                    mAnchor,
                    Gravity.NO_GRAVITY,
                    location[0] - getMeasuredWidth(),
                    location[1] - mAnchor.height / 2
                )
                else -> {}
            }
        } else {
            dismiss()
        }
    }


    private fun getMeasuredWidth(): Int {
        return mShowView.measuredWidth
    }

    private fun getMeasureHeight(): Int {
        return mShowView.measuredWidth
    }


     class Builder(val context: Context, var mShowView: View, val mAnchor: View) {

        var mGravity: Int = Gravity.BOTTOM
        var mBubbleOffset: Float = 0F

        fun gravity(gravity: Int) = apply {
            this.mGravity = gravity
        }

        fun bubbleOffset(offset: Float) = apply {
            this.mBubbleOffset = offset
        }

        fun build(): BubblePopupWindowCustom = BubblePopupWindowCustom(builder = this)
    }
}