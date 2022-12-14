package com.djxf.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * @author yuyh.
 * @date 2016/8/25.
 */
public class BubblePopupWindow extends PopupWindow {

    private BubbleRelativeLayout bubbleView;
    private Context context;
    private int finalWidth;

    public BubblePopupWindow(Context context) {
        this.context = context;
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setOutsideTouchable(false);
        setClippingEnabled(false);

        ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);
    }

    public void setBubbleView(View view) {
        bubbleView = new BubbleRelativeLayout(context);
        bubbleView.setBackgroundColor(Color.TRANSPARENT);
        bubbleView.addView(view);
        setContentView(bubbleView);
    }

    public void setParam(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void show(View parent) {
        show(parent, Gravity.TOP, getMeasuredWidth() / 2);
    }

    public void show(View parent, int gravity) {
        show(parent, gravity, getMeasuredWidth() / 2);
    }

    /**
     * 显示弹窗
     *
     * @param parent
     * @param gravity
     * @param bubbleOffset 气泡尖角位置偏移量。默认位于中间
     */
    public void show(View parent, int gravity, float bubbleOffset) {
        BubbleRelativeLayout.BubbleLegOrientation orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
        if (!this.isShowing()) {
            switch (gravity) {
                case Gravity.BOTTOM:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.TOP;
                    break;
                case Gravity.TOP:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.BOTTOM;
                    break;
                case Gravity.RIGHT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
                    break;
                case Gravity.LEFT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.RIGHT;
                    break;
                default:
                    break;
            }


            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            if (getMeasuredWidth() >= (screenWidth - (location[0]*2 + parent.getWidth()) / 2)) {
                finalWidth = 2*(screenWidth - (location[0]*2 + parent.getWidth()) / 2);
                setWidth((int) finalWidth);
            } else if (getMeasuredWidth()/2 >= (location[0] + parent.getWidth()/2)) {
                finalWidth = (location[0] + parent.getWidth()/2)*2;
                setWidth((int) (finalWidth));
            }

            bubbleView.setBubbleParams(orientation, getMeasuredWidth()/2); // 设置气泡布局方向及尖角偏移

            switch (gravity) {
                case Gravity.BOTTOM:
                    showAtLocation(parent, Gravity.NO_GRAVITY, (int) (location[0] + parent.getWidth()/2 - getMeasuredWidth()/2), location[1] + parent.getHeight());
                    break;
                case Gravity.TOP:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] - getMeasureHeight());
                    break;
                case Gravity.RIGHT:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1] - (parent.getHeight() / 2));
                    break;
                case Gravity.LEFT:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - getMeasuredWidth(), location[1] - (parent.getHeight() / 2));
                    break;
                default:
                    break;
            }
        } else {
            this.dismiss();
        }
    }

    /**
     * 测量高度
     * @return
     */
    public int getMeasureHeight() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight = getContentView().getMeasuredHeight();
        return popHeight;
    }

    /**
     * 测量宽度
     * @return
     */
    public int getMeasuredWidth() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = getContentView().getMeasuredWidth();
        return finalWidth == 0 ? popWidth : finalWidth;
    }

    public static class Builder {
        private Context mContext;
        private View mShowView;
        private View mAnchors;
        private int mGravity;
        private float mBubbleOffset;

        public Builder() {
        }

        public Builder(Context mContext, View mShowView, View mAnchors) {
            this.mContext = mContext;
            this.mShowView = mShowView;
            this.mAnchors = mAnchors;
        }

        public Builder setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }


    }
}