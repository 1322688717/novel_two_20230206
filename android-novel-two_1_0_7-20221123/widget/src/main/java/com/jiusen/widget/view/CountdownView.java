package com.jiusen.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * desc   : 验证码倒计时
 */
public final class CountdownView extends AppCompatTextView implements Runnable {

    /**
     * 倒计时秒数
     */
    private int mTotalSecond = 60;
    /**
     * 秒数单位文本
     */
//    private static final String TIME_UNIT = "S";
    private static final String TIME_UNIT = "秒后再次获取";

    /**
     * 当前秒数
     */
    private int mCurrentSecond;
    /**
     * 记录原有的文本
     */
    private CharSequence mRecordText;
    private OnTimeListener mTimeListener;
    public boolean isTiming = false;

    public CountdownView(Context context) {
        super(context);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置倒计时总秒数
     */
    public void setTotalTime(int totalTime) {
        this.mTotalSecond = totalTime;
    }

    /**
     * 开始倒计时
     */
    public void start() {
        mRecordText = getText();
        setEnabled(false);
        mCurrentSecond = mTotalSecond;
        post(this);
        isTiming = true;
    }

    /**
     * 结束倒计时
     */
    public void stop() {
        setText(mRecordText);
        setEnabled(true);
        if (mTimeListener != null)
            mTimeListener.stop();
        isTiming = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 设置点击的属性
        setClickable(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        // 移除延迟任务，避免内存泄露
        removeCallbacks(this);
        super.onDetachedFromWindow();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void run() {
        if (mCurrentSecond == 0) {
            stop();
        } else {
            mCurrentSecond--;
            setText(mCurrentSecond + " " + TIME_UNIT);
            postDelayed(this, 1000);
        }
    }

    public interface OnTimeListener {
        void time(int second);

        void stop();
    }

    public void setTimelistener(OnTimeListener timelistener) {
        mTimeListener = timelistener;
    }
}