package com.zyt.utility.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.zyt.utility.R;

/**
 * Created by zyt on 2017/12/1.
 * 垂直柱状图---进度条
 */

public class RectangularBarView extends View {
    private Paint paint;
    private Context mContext;
    private int paintColor;
    private float barHeight;
    private float progress;
    private int width;

    public RectangularBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(attrs);
        initPaint();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RectangularBarView);
        paintColor = typedArray.getColor(R.styleable.RectangularBarView_barPaintColor, Color.parseColor("#e1e1e1"));
        typedArray.recycle();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();// 宽度值
        barHeight = getMeasuredHeight();// 高度值
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length = (int) (barHeight - (barHeight * (progress / 100)));
        RectF rectF = new RectF(0, length, width, barHeight);
        canvas.drawRoundRect(rectF, 5, 5, paint);
    }

    /**
     * 设置开始绘制
     *
     * @param value 预计达到的总值
     * @param time  毫秒值
     */
    public void startDraw(int value, long time) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, value);
        anim.setDuration(time);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }

    private int dp2Px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

}
