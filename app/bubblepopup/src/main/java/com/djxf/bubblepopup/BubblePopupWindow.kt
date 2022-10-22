package com.djxf.bubblepopup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow

class BubblePopupWindow(builder: Builder) : PopupWindow(builder.context) {

    var mContext: Context = builder.context
    var mShowView: View = builder.mShowView
    private val mAnchor: View = builder.mAnchor
    private var mGravity: Int = builder.mGravity
    var mBubbleOffset: Float = builder.mBubbleOffset


    /**
     * 显示弹窗
     */
    fun show() {
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
        TODO("Not yet implemented")
    }

    private fun getMeasureHeight(): Int {
        TODO("Not yet implemented")
    }


    inner class Builder(val context: Context, val mShowView: View, val mAnchor: View) {

        var mGravity: Int = Gravity.BOTTOM
        var mBubbleOffset: Float = 0F

        fun gravity(gravity: Int) = apply {
            this.mGravity = gravity
        }

        fun bubbleOffset(offset: Float) = apply {
            this.mBubbleOffset = offset
        }

        fun build(): BubblePopupWindow = BubblePopupWindow(builder = this)
    }
}