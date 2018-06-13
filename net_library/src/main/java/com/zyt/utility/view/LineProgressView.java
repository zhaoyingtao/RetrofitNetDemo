package com.zyt.utility.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zyt.utility.R;

/**
 * Created by zyt on 2017/12/1.
 * 线性进度条---水平
 */

public class LineProgressView extends View {
    private Paint paint;//背景的画笔
    private Paint mPaint;//进度的画笔
    private Context mContext;
    private float paintWidth; // 进度画笔宽度
    private int paintColor; // 进度画笔颜色
    private float progress;//进度值
    private int defaultColor;//背景色


    private int width;
    private int height;
    private int startX;
    private int endX;
    private float length;
    private float lineLength;//线的总长度

    public LineProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        getAttrs(attrs);
        initPaint();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LineProgressView);
        paintColor = typedArray.getColor(R.styleable.LineProgressView_paintColor, Color.parseColor("#df0781"));
        defaultColor = typedArray.getColor(R.styleable.LineProgressView_defaultColor, Color.parseColor("#e1e1e1"));
        paintWidth = typedArray.getDimension(R.styleable.LineProgressView_paintWidth, dp2px(10));
        lineLength = typedArray.getDimension(R.styleable.LineProgressView_lineLength, dp2px(300));
        typedArray.recycle();
    }


    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(defaultColor);
        paint.setStrokeWidth(paintWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);


        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = (int) lineLength;
        height = h;
        startX = (int) paintWidth;
        endX = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        length = (endX * progress / 100) + startX - (progress / 100 * startX);
        canvas.drawLine(startX, height / 2, endX, height / 2, paint);
        //绘制进度
        canvas.drawLine(startX, height / 2, length, height / 2, mPaint);
    }

    // 设置进度
    public void setProgress(float mprogress) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, mprogress);
        anim.setDuration(3000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
