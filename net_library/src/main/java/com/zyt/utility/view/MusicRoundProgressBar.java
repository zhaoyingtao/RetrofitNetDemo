package com.zyt.utility.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zyt.utility.R;


/**
 * Created by zyt on 2017/6/26.
 * 音乐===圆圈进度条
 */

public class MusicRoundProgressBar extends View {
    private Paint mCirclePaint;//中心园的画笔
    private Paint mArcPaint;//外圆环的画笔
    private int mCircleBackground;//中心园的背景色
    private int mRadius;//圆的半径
    private int mRingColor;//进度条的颜色

    //圆心位置
    private int mCircleX;
    private int mCircleY;

    private double mCurrentAngle;//当前角度
    private int mStartSweepValue = -90;//起始角度位置//圆环开始角度 -90° 正北方向
    private int mCurrentPercent = 0;//当前百分比
    private float mTargetPercent = 0;//目标百分比
    private boolean isDirectArrival;//是否直接跑到指定值
    private float circle_width;//圆环的大小

    public MusicRoundProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public MusicRoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MusicRoundProgressBar);
        mRadius = (int) typedArray.getDimension(R.styleable.MusicRoundProgressBar_music_radius, 50);
        mCircleBackground = typedArray.getColor(R.styleable.MusicRoundProgressBar_music_circle_bg, 0xffafb4db);
        mRingColor = typedArray.getColor(R.styleable.MusicRoundProgressBar_music_arc_color, 0xff6950a1);
        circle_width = typedArray.getDimension(R.styleable.MusicRoundProgressBar_music_circle_width, (float) (0.075 * mRadius));
        typedArray.recycle();
        initPaint();
    }

    public MusicRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        //设置中心园的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleBackground);
        //样式一
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(circle_width);
        //样式二
        //设置外圆环的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(circle_width);
        mArcPaint.setColor(mRingColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(widthMeasureSpec));
    }

    // 当wrap_content的时候，view的大小根据半径大小改变，但最大不会超过屏幕
    private int measure(int measureSpec) {
        int result = 0;
        //1、先获取测量模式 和 测量大小
        //2、如果测量模式是MatchParent 或者精确值，则宽为测量的宽
        //3、如果测量模式是WrapContent ，则宽为 直径值 与 测量宽中的较小值；否则当直径大于测量宽时，会绘制到屏幕之外；
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //result = 2*mRadius;
            //result =(int) (1.075*mRadius*2);
            result = (int) (mRadius * 2 + mArcPaint.getStrokeWidth() * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //需要将计算放在绘制之前
        if (isDirectArrival) {
            if (mCurrentPercent < mTargetPercent) {
                //当前百分比+1
                mCurrentPercent += 1;
                //当前角度+360
                mCurrentAngle += 3.6;
                //每10ms重画一次
                postInvalidateDelayed(10);
            }
        } else {
            //当前角度+360
            if (mCurrentPercent <= 100) {
                mCurrentAngle = 3.6 * mCurrentPercent;
            }
        }

        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;
        //1、画中间背景圆
        //2、画文字
        //3、画圆环
        //4、判断进度，重新绘制
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mCirclePaint);
        RectF mArcRectF = new RectF(mCircleX - mRadius, mCircleY - mRadius, mCircleX + mRadius, mCircleY + mRadius);
        canvas.drawArc(mArcRectF, mStartSweepValue, (float) mCurrentAngle, false, mArcPaint);

    }

    public void setTargetPercent(float targetPercent) {
        mTargetPercent = targetPercent;
        isDirectArrival = true;
        invalidate();
    }

    public void setProgress(int progress) {
        mCurrentPercent = progress;
        isDirectArrival = false;
        postInvalidate();
    }
}
