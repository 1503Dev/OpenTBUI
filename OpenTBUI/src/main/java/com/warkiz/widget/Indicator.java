package com.warkiz.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * created by zhuangguangquan on 2017/9/9
 * <p>
 * https://github.com/warkiz/IndicatorSeekBar
 * <p>
 * Donation/打赏:
 * If this library is helpful to you ,you can give me a donation by:
 *
 * @see <a href="https://www.paypal.me/BuyMeACupOfTeaThx">ZhuanGuangQuan's Paypal</a>, or
 * @see <a href="https://github.com/warkiz/IndicatorSeekBar/blob/master/app/src/main/res/mipmap-xxhdpi/wechat_pay.png?raw=true">微信支付</a>, or
 * @see <a href="https://github.com/warkiz/IndicatorSeekBar/blob/master/app/src/main/res/mipmap-xxhdpi/alipay.png?raw=true">支付宝</a>
 * <p>
 */
public class Indicator {
    private final int mWindowWidth;
    private int[] mLocation = new int[2];
    private ArrowView mArrowView;
    private TextView mProgressTextView;
    private PopupWindow mIndicatorPopW;
    private LinearLayout mTopContentView;
    private int mGap;
    public int mIndicatorColor;
    private Context mContext;
    private int mIndicatorType;
    private IndicatorSeekBar mSeekBar;
    private View mIndicatorView;
    private View mIndicatorCustomView;
    private View mIndicatorCustomTopContentView;
    private float mIndicatorTextSize;
    private int mIndicatorTextColor;

    public Indicator(Context context,
                     IndicatorSeekBar seekBar,
                     int indicatorColor,
                     int indicatorType,
                     int indicatorTextSize,
                     int indicatorTextColor,
                     View indicatorCustomView,
                     View indicatorCustomTopContentView) {
        this.mContext = context;
        this.mSeekBar = seekBar;
        this.mIndicatorColor = indicatorColor;
        this.mIndicatorType = indicatorType;
        this.mIndicatorCustomView = indicatorCustomView;
        this.mIndicatorCustomTopContentView = indicatorCustomTopContentView;
        this.mIndicatorTextSize = indicatorTextSize;
        this.mIndicatorTextColor = indicatorTextColor;

        mWindowWidth = getWindowWidth();
        mGap = SizeUtils.dp2px(mContext, 2);
        initIndicator();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        if (mIndicatorView != null) {
            if (mIndicatorType == IndicatorType.CUSTOM) {
                if (mIndicatorCustomTopContentView != null) {
                    ((GradientDrawable) mIndicatorCustomTopContentView.getBackground()).setColor(indicatorColor);
                }
            } else {
                if (mArrowView != null) {
                    mArrowView.setColor(indicatorColor);
                }
                if (mTopContentView != null) {
                    ((GradientDrawable) mTopContentView.getBackground()).setColor(indicatorColor);
                }
            }
        }
    }

    // indicator 布局中的 View ID，用纯代码 set 生成
    private static final int ID_CONTAINER = 0x7F000001;
    private static final int ID_PROGRESS = 0x7F000002;
    private static final int ID_ARROW = 0x7F000003;

    private void initIndicator() {
        if (mIndicatorType == IndicatorType.CUSTOM) {
            if (mIndicatorCustomView != null) {
                mIndicatorView = mIndicatorCustomView;
                // 查找自定义布局中 id 为 ID_PROGRESS 的 TextView
                View view = mIndicatorView.findViewById(ID_PROGRESS);
                if (view instanceof TextView) {
                    mProgressTextView = (TextView) view;
                    mProgressTextView.setText(mSeekBar.getIndicatorTextString());
                    mProgressTextView.setTextSize(SizeUtils.px2sp(mContext, mIndicatorTextSize));
                    mProgressTextView.setTextColor(mIndicatorTextColor);
                }
            } else {
                throw new IllegalArgumentException("the attr: indicator_custom_layout must be set while you set the indicator type to CUSTOM.");
            }
        } else {
            if (mIndicatorType == IndicatorType.CIRCULAR_BUBBLE) {
                mIndicatorView = new CircleBubbleView(mContext, mIndicatorTextSize, mIndicatorTextColor, mIndicatorColor, "1000");
                ((CircleBubbleView) mIndicatorView).setProgress(mSeekBar.getIndicatorTextString());
            } else {
                // 纯代码构建 indicator 布局，替代 isb_indicator.xml
                mIndicatorView = buildIndicatorView();
                mTopContentView = (LinearLayout) mIndicatorView.findViewById(ID_CONTAINER);
                mArrowView = (ArrowView) mIndicatorView.findViewById(ID_ARROW);
                mArrowView.setColor(mIndicatorColor);
                mProgressTextView = (TextView) mIndicatorView.findViewById(ID_PROGRESS);
                mProgressTextView.setText(mSeekBar.getIndicatorTextString());
                mProgressTextView.setTextSize(SizeUtils.px2sp(mContext, mIndicatorTextSize));
                mProgressTextView.setTextColor(mIndicatorTextColor);
                mTopContentView.setBackground(getGradientDrawable());

                //custom top content view
                if (mIndicatorCustomTopContentView != null) {
                    View topContentView = mIndicatorCustomTopContentView;
                    View tv = topContentView.findViewById(ID_PROGRESS);
                    if (tv instanceof TextView) {
                        setTopContentView(topContentView, (TextView) tv);
                    } else {
                        setTopContentView(topContentView);
                    }
                }
            }
        }
    }

    /**
     * 纯代码构建 indicator 布局（替代 isb_indicator.xml）
     */
    private View buildIndicatorView() {
        LinearLayout root = new LinearLayout(mContext);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // container (top content + progress text)
        LinearLayout container = new LinearLayout(mContext);
        container.setId(ID_CONTAINER);
        container.setGravity(Gravity.CENTER);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // progress text
        TextView progressText = new TextView(mContext);
        progressText.setId(ID_PROGRESS);
        progressText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        progressText.setMaxLines(1);
        progressText.setMinWidth(SizeUtils.dp2px(mContext, 12));
        int padV = SizeUtils.dp2px(mContext, 4);
        int padH = SizeUtils.dp2px(mContext, 8);
        progressText.setPadding(padH, padV, padH, padV);
        progressText.setText("Indicator");
        progressText.setTextSize(SizeUtils.px2sp(mContext, SizeUtils.sp2px(mContext, 13)));
        container.addView(progressText);

        root.addView(container);

        // arrow
        ArrowView arrow = new ArrowView(mContext);
        arrow.setId(ID_ARROW);
        LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        arrowParams.gravity = Gravity.CENTER_HORIZONTAL;
        root.addView(arrow, arrowParams);

        return root;
    }

    @NonNull
    private GradientDrawable getGradientDrawable() {
        GradientDrawable tvDrawable = new GradientDrawable();
        tvDrawable.setShape(GradientDrawable.RECTANGLE);
        tvDrawable.setColor(mIndicatorColor);
        if (mIndicatorType == IndicatorType.ROUNDED_RECTANGLE) {
            int radius = SizeUtils.dp2px(mContext, 8);
            tvDrawable.setCornerRadius(radius);
            tvDrawable.setSize(SizeUtils.dp2px(mContext, 28), SizeUtils.dp2px(mContext, 16));
        } else {
            tvDrawable.setCornerRadius(0);
            tvDrawable.setSize(SizeUtils.dp2px(mContext, 24), SizeUtils.dp2px(mContext, 16));
        }
        return tvDrawable;
    }

    private int getWindowWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return wm.getCurrentWindowMetrics().getBounds().width();
        } else {
            return wm.getDefaultDisplay().getWidth();
        }
    }

    private int getIndicatorScreenX() {
        mSeekBar.getLocationOnScreen(mLocation);
        return mLocation[0];
    }

    private void adjustArrow(float touchX) {
        if (mIndicatorType == IndicatorType.CUSTOM || mIndicatorType == IndicatorType.CIRCULAR_BUBBLE) {
            return;
        }
        int indicatorScreenX = getIndicatorScreenX();
        if (indicatorScreenX + touchX < mIndicatorPopW.getContentView().getMeasuredWidth() / 2) {
            setMargin(mArrowView, -(int) (mIndicatorPopW.getContentView().getMeasuredWidth() / 2 - indicatorScreenX - touchX), -1, -1, -1);
        } else if (mWindowWidth - indicatorScreenX - touchX < mIndicatorPopW.getContentView().getMeasuredWidth() / 2) {
            setMargin(mArrowView, (int) (mIndicatorPopW.getContentView().getMeasuredWidth() / 2 - (mWindowWidth - indicatorScreenX - touchX)), -1, -1, -1);
        } else {
            setMargin(mArrowView, 0, 0, 0, 0);
        }
    }

    private void setMargin(View view, int left, int top, int right, int bottom) {
        if (view == null) {
            return;
        }
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(left == -1 ? layoutParams.leftMargin : left, top == -1 ? layoutParams.topMargin : top, right == -1 ? layoutParams.rightMargin : right, bottom == -1 ? layoutParams.bottomMargin : bottom);
            view.requestLayout();
        }
    }

    void iniPop() {
        if (mIndicatorPopW != null) {
            return;
        }
        if (mIndicatorType != IndicatorType.NONE && mIndicatorView != null) {
            mIndicatorView.measure(0, 0);
            mIndicatorPopW = new PopupWindow(mIndicatorView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        }
    }

    View getInsideContentView() {
        return mIndicatorView;
    }

    void setProgressTextView(String text) {
        if (mIndicatorView instanceof CircleBubbleView) {
            ((CircleBubbleView) mIndicatorView).setProgress(text);
        } else if (mProgressTextView != null) {
            mProgressTextView.setText(text);
        }
    }

    void updateIndicatorLocation(int offset) {
        setMargin(mIndicatorView, offset, -1, -1, -1);
    }

    void updateArrowViewLocation(int offset) {
        setMargin(mArrowView, offset, -1, -1, -1);
    }


    /**
     * update the indicator position
     *
     * @param touchX the x location you touch without padding left.
     */
    void update(float touchX) {
        if (!mSeekBar.isEnabled() || !(mSeekBar.getVisibility() == View.VISIBLE)) {
            return;
        }
        refreshProgressText();
        if (mIndicatorPopW != null) {
            mIndicatorPopW.getContentView().measure(0, 0);
            try {
                mIndicatorPopW.update(mSeekBar, (int) (touchX - mIndicatorPopW.getContentView().getMeasuredWidth() / 2),
                        -(mSeekBar.getMeasuredHeight() + mIndicatorPopW.getContentView().getMeasuredHeight() - mSeekBar.getPaddingTop() /*- mSeekBar.getTextHeight() */ + mGap), -1, -1);
            } catch (Exception e) {
                // **&# there is a exception should be record
            }
            adjustArrow(touchX);
        }
    }

    /**
     * call this method to show the indicator.
     *
     * @param touchX the x location you touch, padding left excluded.
     */
    void show(float touchX) {
        if (!mSeekBar.isEnabled() || !(mSeekBar.getVisibility() == View.VISIBLE)) {
            return;
        }
        refreshProgressText();
        if (mIndicatorPopW != null) {
            mIndicatorPopW.getContentView().measure(0, 0);
            try {
                mIndicatorPopW.showAsDropDown(mSeekBar, (int) (touchX - mIndicatorPopW.getContentView().getMeasuredWidth() / 2f),
                        -(mSeekBar.getMeasuredHeight() + mIndicatorPopW.getContentView().getMeasuredHeight() - mSeekBar.getPaddingTop() /*- mSeekBar.getTextHeight()*/ + mGap));
            } catch (Exception e) {
                // **&# there is a exception should be record
            }
            adjustArrow(touchX);
        }
    }

    void refreshProgressText() {
        String tickTextString = mSeekBar.getIndicatorTextString();
        if (mIndicatorView instanceof CircleBubbleView) {
            ((CircleBubbleView) mIndicatorView).setProgress(tickTextString);
        } else if (mProgressTextView != null) {
            mProgressTextView.setText(tickTextString);
        }
    }

    /**
     * call this method hide the indicator
     */
    void hide() {
        if (mIndicatorPopW == null) {
            return;
        }
        try {
            mIndicatorPopW.dismiss();
        } catch (Exception e) {
            // **&# there is a exception should be record
        }
    }

    boolean isShowing() {
        return mIndicatorPopW != null && mIndicatorPopW.isShowing();
    }


    /*----------------------API START-------------------*/

    /**
     * get the indicator content view.
     *
     * @return the view which is inside indicator.
     */
    public View getContentView() {
        return mIndicatorView;
    }

    /**
     * call this method to replace the current indicator with a new indicator view , indicator arrow will be replace ,too.
     *
     * @param customIndicatorView a new content view for indicator.
     */
    public void setContentView(@NonNull View customIndicatorView) {
        this.mIndicatorType = IndicatorType.CUSTOM;
        this.mIndicatorCustomView = customIndicatorView;
        initIndicator();
    }

    /**
     * call this method to replace the current indicator with a new indicator view, indicator arrow will be replace ,too.
     *
     * @param customIndicatorView a new content view for indicator.
     * @param progressTextView    this TextView will show the progress or tick text, must be found in @param customIndicatorView
     */
    public void setContentView(@NonNull View customIndicatorView, TextView progressTextView) {
        this.mProgressTextView = progressTextView;
        this.mIndicatorType = IndicatorType.CUSTOM;
        this.mIndicatorCustomView = customIndicatorView;
        initIndicator();
    }

    /**
     * get the indicator top content view.
     * if indicator type {@link IndicatorType} is CUSTOM or CIRCULAR_BUBBLE, call this method will get a null value.
     *
     * @return the view which is inside indicator's top part, not include arrow
     */
    public View getTopContentView() {
        return mTopContentView;
    }

    /**
     * set the View to the indicator top container, not influence indicator arrow ;
     * if indicator type {@link IndicatorType} is CUSTOM or CIRCULAR_BUBBLE, call this method will be not worked.
     *
     * @param topContentView the view is inside the indicator TOP part, not influence indicator arrow;
     */
    public void setTopContentView(@NonNull View topContentView) {
        setTopContentView(topContentView, null);
    }

    /**
     * set the  View to the indicator top container, and show the changing progress in indicator when seek;
     * not influence indicator arrow;
     * if indicator type is custom , this method will be not work.
     *
     * @param topContentView   the view is inside the indicator TOP part, not influence indicator arrow;
     * @param progressTextView this TextView will show the progress or tick text, must be found in @param topContentView
     */
    public void setTopContentView(@NonNull View topContentView, @Nullable TextView progressTextView) {
        this.mProgressTextView = progressTextView;
        this.mTopContentView.removeAllViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            topContentView.setBackground(getGradientDrawable());
        } else {
            topContentView.setBackgroundDrawable(getGradientDrawable());
        }
        this.mTopContentView.addView(topContentView);
    }

    /*----------------------API END-------------------*/

}