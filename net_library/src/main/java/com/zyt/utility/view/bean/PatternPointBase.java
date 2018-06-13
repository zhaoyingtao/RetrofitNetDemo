package com.zyt.utility.view.bean;

/**
 * Created by zyt on 2017/9/12.
 */

public class PatternPointBase {
    protected int centerX;     //圆心X
    protected int centerY;     //圆心Y
    protected int radius;      //半径
    public String tag;      //密码标签

    public int status;         //状态

    public static final int STATE_NORMAL = 0;       //正常
    public static final int STATE_SELECTED = 1;     //选中
    public static final int STATE_ERROR = 2;        //错误

    public boolean isPointArea(double x, double y) {
        double len = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
        return radius > len;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getRadius() {
        return radius;
    }

    public String getTag() {
        return tag;
    }

    public int getStatus() {
        return status;
    }

}
