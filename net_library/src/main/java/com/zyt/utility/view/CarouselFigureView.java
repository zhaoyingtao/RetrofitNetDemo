package com.zyt.utility.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zyt.utility.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * AUTHOR:        ZhaoYingTao
 * CREATE-TIME:  17/6/21
 * DESC:轮播图----支持无限循环，自动播放，手动滑动，点击事件
 */
public class CarouselFigureView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    //----------－－－－-控件－－－－－－－－－－－
    private ViewPager viewPager;//ImageView容器
    private LinearLayout indicationPointLayout; //指示点容器

    //-------------－－自定义属性－－－－－－－－－－－－
    private boolean isAutoPlay = true; //是否自动切换
    private boolean isInfiniteLoop = true; //是否无限循环
    private boolean isNeedIndicationPoint = true;//是否需要指示点
    private float pointLeft_Right_Margin = 4;//指示点的间距，单位是px,在布局文件中设置单位最好为dp
    private float pointBottomMargin = 5;//指示点上移的距离,单位是px，在布局文件中设置单位最好为dp
    private int playIntervalTime = 3000;//自动切换间隔，默认3秒，单位毫秒
    private int imgPlaceholderResource = -1;//图片加载出之前显示的图片，占位图
    private int indicationLocation = Gravity.CENTER;//指示点的位置
    //    private int indicationLocation = Gravity.CENTER_HORIZONTAL;//指示点的位置
    private int interPointBgResource;//指示点的背景资源
    //－－－－－－－－－数据－－－－－－－－－－－
    private int itemCount; //条目数量
    private int lastPosition;//上一个显示的条目下标
    private List<String> urlList;//url集合
    private List<Integer> resourceList;//资源集合
    private List<ImageView> pictureList;//存放图片的ImageView
    //--------------其他－－－－－－－－－－－
    private int MAX_VALUE = Integer.MAX_VALUE; //最大值，用于无限循环
    private boolean isHaveHandler = true;//当用户点击轮播图时，用来取消handler队列
    private int SELCET_LOAD_MODE; //图片加载模式
    private CarouselFigureItemClickListener listener;//条目点击事件
    private final int HANDLE_MSG = 7758;

    /**
     * 图片的加载方式
     */
    private interface LOAD_MODE {
        int URL = 1001, RESOURCE = 1002;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isAutoPlay) {
                viewPager.setCurrentItem(lastPosition + 1);
                handler.sendEmptyMessageDelayed(HANDLE_MSG, playIntervalTime);
            }
        }
    };


    public CarouselFigureView(Context context) {
        this(context, null);
    }

    public CarouselFigureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselFigureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        initViewPager(context);
        if (isNeedIndicationPoint) {
            initPonitLinearLayout(context);
        }
    }

    /**
     * 解析自定义属性
     */
    private void parseCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CarouselFigureView);
        // 获得xml里定义的属性,格式为 名称_属性名 后面是默认值
        isAutoPlay = a.getBoolean(R.styleable.CarouselFigureView_isAutoPlay, true);
        isInfiniteLoop = a.getBoolean(R.styleable.CarouselFigureView_isInfiniteLoop, true);
        isNeedIndicationPoint = a.getBoolean(R.styleable.CarouselFigureView_isNeedIndicationPoint, true);
        pointLeft_Right_Margin = a.getDimension(R.styleable.CarouselFigureView_pointLeft_Right_Margin, 5);
        pointBottomMargin = a.getDimension(R.styleable.CarouselFigureView_pointBottomMargin, 2);
        playIntervalTime = a.getInt(R.styleable.CarouselFigureView_playIntervalTime, 3000);
        imgPlaceholderResource = a.getResourceId(R.styleable.CarouselFigureView_imgPlaceholderResource, -1);
        interPointBgResource = a.getResourceId(R.styleable.CarouselFigureView_interPointBgResource, -1);
        // 为了保持以后使用该属性一致性,返回一个绑定资源结束的信号给资源
        a.recycle();
    }

    /**
     * 初始化指示点布局
     *
     * @param context
     */
    private void initPonitLinearLayout(Context context) {

        indicationPointLayout = new LinearLayout(context);
        indicationPointLayout.setOrientation(LinearLayout.HORIZONTAL);

//        indicationPointLayout.setGravity(indicationLocation);
        indicationPointLayout.setGravity(Gravity.CENTER);
//        indicationPointLayout.setGravity( Gravity.CENTER_VERTICAL);
        indicationPointLayout.setBackground(getResources().getDrawable(R.drawable.bg_layout_ponit));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 0, 10);
        this.addView(indicationPointLayout, layoutParams);

    }


    /**
     * 初始化ViewPager
     *
     * @param context
     */
    private void initViewPager(Context context) {
        viewPager = new ViewPager(context);
        viewPager.addOnPageChangeListener(this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(viewPager, layoutParams);
    }


    /**
     * set data
     *
     * @param urlList：图片的url地址
     */
    public CarouselFigureView setURL(List<String> urlList) {
        if (urlList == null || urlList.size() <= 0)
            throw new RuntimeException("your data size = 0:::你设置的数据为空");
        this.urlList = urlList;
        SELCET_LOAD_MODE = LOAD_MODE.URL;
        itemCount = urlList.size();
        startLoad();
        return this;
    }


    /**
     * set data
     *
     * @param resourceList 图片资源地址
     */
    public CarouselFigureView setResourceList(List<Integer> resourceList) {
        if (resourceList == null || resourceList.size() <= 0)
            throw new RuntimeException("your data size = 0:::你设置的数据为空");
        this.resourceList = resourceList;
        SELCET_LOAD_MODE = LOAD_MODE.RESOURCE;
        itemCount = resourceList.size();
        startLoad();
        return this;
    }

    /**
     * 改变自动切换的状态
     *
     * @param flag
     */
    public CarouselFigureView setAutoPlayState(boolean flag) {
        if (flag != isAutoPlay) {
            isAutoPlay = flag;
            handler.removeMessages(HANDLE_MSG);
            if (isAutoPlay) {
                handler.sendEmptyMessageDelayed(HANDLE_MSG, playIntervalTime);
            }
        }
        return this;
    }

    /**
     * 设置指示点的bg资源
     *
     * @param interPointBgResource
     * @return
     */
    public CarouselFigureView setPointBgResource(int interPointBgResource) {
        this.interPointBgResource = interPointBgResource;
        return this;
    }

    /**
     * 改变无限循环的状态
     *
     * @param flag
     */
    public CarouselFigureView setInfiniteLoopState(boolean flag) {
        if (flag != isInfiniteLoop) {
            isInfiniteLoop = flag;
            startLoad();
        }
        return this;
    }

    /**
     * 修改指示点布局的显示隐藏状态
     *
     * @param flag true为显示，false为隐藏
     */
    public CarouselFigureView setIndicationPointState(boolean flag) {
        if (flag) {
            if (!isNeedIndicationPoint) {
                isNeedIndicationPoint = true;
                initPonitLinearLayout(this.getContext());
                addIndicationPointToView();
            }
        } else {
            removeIndicationPoint();
        }
        return this;
    }

    /**
     * 设置指示点的位置，
     *
     * @param gravity Gravity.CENTER_HORIZONTAL Gravity.RIGHT Gravity.LEFT
     */
    public CarouselFigureView setIndicationLocation(int gravity) {
        this.indicationLocation = gravity;
        if (indicationPointLayout != null) {
            indicationPointLayout.setGravity(indicationLocation);
        }
        return this;
    }

    public CarouselFigureView setCarouselFigureItemClickListener(CarouselFigureItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 改变ViewPager的切换方式
     *
     * @param transformer 可自定义ViewPager.PageTransformer，此类代表ViewPager切换动画
     */
    public void setViewPagerSwitchStyle(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
    }

    /**
     * 改变ViewPager的切换速度
     *
     * @param switchSpeed 单位毫秒
     */
    public CarouselFigureView setViewPagerSwitchSpeed(int switchSpeed) {
        if (switchSpeed >= playIntervalTime)
            throw new RuntimeException("你设置的切换速度必须小于轮播图的切换时间");
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(this.getContext(), new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(switchSpeed);
        } catch (Exception e) {
        }
        return this;
    }

    private void startLoad() {
        if (SELCET_LOAD_MODE == 0)
            throw new RuntimeException("你必须设置数据 可以使用setUrl()或者setResourceList()来设置数据");
        if (isNeedIndicationPoint) {
            addIndicationPointToView();
        }
        viewPager.setAdapter(new MyViewPagerAdapter());
        if (isInfiniteLoop) {
            viewPager.setCurrentItem(200 - (200 % itemCount));
        }
        if (isAutoPlay) {
            handler.removeMessages(HANDLE_MSG);
            handler.sendEmptyMessageDelayed(HANDLE_MSG, playIntervalTime);
        }
    }

    /**
     * 移除指示点布局
     */
    private void removeIndicationPoint() {
        if (isNeedIndicationPoint) {
            this.removeView(indicationPointLayout);
            isNeedIndicationPoint = false;
        }
    }


    /**
     * 添加指示点到布局当中
     */
    private void addIndicationPointToView() {
        //防止刷新重复添加
        if (indicationPointLayout.getChildCount() > 0) {
            indicationPointLayout.removeAllViews();
        }


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins((int) pointLeft_Right_Margin, 0, (int) pointLeft_Right_Margin, (int) pointBottomMargin);
        layoutParams.setMargins((int) pointLeft_Right_Margin, 0, (int) pointLeft_Right_Margin, 0);

        for (int i = 0; i < itemCount; i++) {

            ImageView pointImageView = new ImageView(this.getContext());
            pointImageView.setBackgroundResource(interPointBgResource);
//            pointImageView.setImageResource(interPointBgResource);

//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(12,12);
//            pointImageView.setLayoutParams(lp);

            if (i == lastPosition) {
                pointImageView.setSelected(true);
            } else {
                pointImageView.setSelected(false);
            }
            indicationPointLayout.addView(pointImageView, layoutParams);
        }
    }


    private class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (isInfiniteLoop) {
                return MAX_VALUE;
            } else {
                return itemCount;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            RoundAngelImageView imageView = new RoundAngelImageView(container.getContext());
            imageView.setType(1);
            imageView.setBorderRadius(5);
//            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            switch (SELCET_LOAD_MODE) {
                case LOAD_MODE.URL:
                    loadImgByUrl(urlList.get(position % itemCount), imageView);
                    break;
                case LOAD_MODE.RESOURCE:
                    loadImgByResourceId(resourceList.get(position % itemCount), imageView);
                    break;
            }
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(v, position % itemCount);
                    }
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void cancelSelect() {
        for (int i = 0; i < indicationPointLayout.getChildCount(); i++) {
            indicationPointLayout.getChildAt(i).setSelected(false);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (isNeedIndicationPoint) {
            cancelSelect();
            indicationPointLayout.getChildAt(position % itemCount).setSelected(true);
        }
        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://空闲状态
                if (isAutoPlay) {
                    if (!isHaveHandler) {
                        isHaveHandler = true;
                        handler.sendEmptyMessageDelayed(HANDLE_MSG, playIntervalTime);
                    }
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://用户滑动状态
                if (isAutoPlay) {
                    handler.removeMessages(HANDLE_MSG);
                    isHaveHandler = false;
                }
                break;
        }
    }

    /**
     * 点击监听回调
     */
    public interface CarouselFigureItemClickListener {
        void onClick(View view, int position);
    }

    //------------------other----------------------------------


    private void loadImgByUrl(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.DATA);
        options.placeholder(imgPlaceholderResource);
        Glide.with(this.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    private void loadImgByResourceId(int resource, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(imgPlaceholderResource);
        Glide.with(this.getContext())
                .load(resource)
                .apply(options)
                .into(imageView);
    }

    class ViewPagerScroller extends Scroller {
        private int mDuration = 1500;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}
