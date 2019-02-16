package com.yefoo.demopullrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class RefreshHead extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_RELEASE_TO_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;
    public static final int STATE_DONE = 3;
    public static final int STATE_FAIL = 4;

    private int refreshState = 0;
    /**
     * 触发刷新操作最小的高度
     */
    private int refreshLimitHeight;
    private View refreshContentView;
    //    private TextView textTip;
    private ImageView imageRefreshing;

    private PullToRefreshListener pullToRefreshListener;
//    private Animation circle_anim;

    public RefreshHead(Context context) {
        this(context, null);
    }

    public RefreshHead(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        refreshLimitHeight = getScreenHeight() / 12;

        refreshContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_recyclerview_refresh_header, this, false);

        RelativeLayout refreshLayout = refreshContentView.findViewById(R.id.refresh_layout);
        LayoutParams params = (LayoutParams)refreshLayout.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        refreshLayout.setLayoutParams(params);
//        textTip = refreshContentView.findViewById(R.id.refresh_header_tv);
        imageRefreshing = refreshContentView.findViewById(R.id.refresh_header_iv);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        addView(refreshContentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setVisibleHeight(-getVisibleHeight());


//        int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//        imageRefreshing.measure(w, h);
//        int imageWidth = imageRefreshing.getMeasuredWidth();
//        float textWidth = textTip.getPaint().measureText(textTip.getText().toString());
//        int deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageRefreshing.getLayoutParams();
//        layoutParams.leftMargin = (int) ((deviceWidth - (imageWidth + textWidth)) / 2);
//        imageRefreshing.setLayoutParams(layoutParams);


//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)refreshContentView.getLayoutParams();
//        params.width = DensityUtil.getScreenWidth(context);
//        refreshContentView.setLayoutParams(params);

//        circle_anim = AnimationUtils.loadAnimation(context, R.anim.round_rotate);
//        circle_anim.setInterpolator(new LinearInterpolator());
    }

    /**
     * 设置触发刷新的高度
     *
     * @param height height
     */
    public void setRefreshLimitHeight(int height) {
        refreshLimitHeight = height;
        if (refreshLimitHeight < getVisibleHeight()) {
            refreshLimitHeight = getVisibleHeight();
        }
    }

    public void setRefreshingResource(int resId) {
        imageRefreshing.setImageResource(resId);
    }

    public void onMove(int move) {
        int newRefreshHeight = getVisibleHeight() + move;
        Log.e("RefreshHead", "move, getVisibleHeight() = "+ getVisibleHeight() + ", move="+move);
        if (imageRefreshing != null && imageRefreshing.getVisibility() != VISIBLE) {
            imageRefreshing.setVisibility(VISIBLE);
            Log.i("RefreshHead", "imageRefreshing.setVisibility(VISIBLE)");
        }
        if (newRefreshHeight >= refreshLimitHeight && refreshState != STATE_RELEASE_TO_REFRESH) {
            refreshState = STATE_RELEASE_TO_REFRESH;
//            textTip.setText("release to refresh");
            Log.i("RefreshHead", "refreshState = STATE_RELEASE_TO_REFRESH");
        } else if (newRefreshHeight > refreshLimitHeight && refreshState == STATE_RELEASE_TO_REFRESH) {
            Log.i("RefreshHead", "newRefreshHeight > refreshLimitHeight && refreshState == STATE_RELEASE_TO_REFRESH");
            return;
        }
        if (newRefreshHeight < refreshLimitHeight && refreshState != STATE_NORMAL) {
            refreshState = STATE_NORMAL;
//            textTip.setText("pull to refresh");
        }
        setVisibleHeight(newRefreshHeight);

    }

    /**
     * 触摸事件结束后检查是否需要刷新
     */
    public void checkRefresh() {
        if (getVisibleHeight() <= 0) return;
        if (refreshState == STATE_NORMAL) {
            smoothScrollTo(0);
            refreshState = STATE_DONE;
        } else if (refreshState == STATE_RELEASE_TO_REFRESH) {
            setState(STATE_REFRESHING);
        }
    }

    public void setRefreshing() {
        refreshState = STATE_REFRESHING;
        imageRefreshing.setVisibility(VISIBLE);
        startRefreshingAnimation();
//        textTip.setText("refreshing");
        smoothScrollTo(refreshLimitHeight);
        if (pullToRefreshListener != null) {
            pullToRefreshListener.onRefresh();
        }
    }

    public void startRefreshingAnimation() {
//        textTip.setText(R.string.refresh_loading);
//        if (circle_anim != null) {
//            imageRefreshing.startAnimation(circle_anim);
//        }
        if (imageRefreshing != null && imageRefreshing.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable) imageRefreshing.getDrawable();
            animationDrawable.start();
        }
    }

    public void stopRefreshingAnimation(){
        if (imageRefreshing != null && imageRefreshing.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable) imageRefreshing.getDrawable();
            animationDrawable.stop();
        }
    }


    public void setState(int state) {
        if (refreshState == state) return;
        switch (state) {
            case STATE_REFRESHING://切换到刷新状态
                refreshState = state;
                imageRefreshing.setVisibility(VISIBLE);
                startRefreshingAnimation();
//                textTip.setText(R.string.refresh_loading);
                smoothScrollTo(refreshLimitHeight);

                if (pullToRefreshListener != null) {
                    pullToRefreshListener.onRefresh();
                }
                break;
            case STATE_DONE://切换到刷新完成或者加载成功的状态
                if (refreshState == STATE_REFRESHING) {
                    refreshState = state;
                    imageRefreshing.clearAnimation();
                    stopRefreshingAnimation();
                    imageRefreshing.setVisibility(INVISIBLE);
//                    textTip.setText(R.string.refresh_load_complete);
                    smoothScrollTo(0);
                }
                break;
            case STATE_FAIL://切换到刷新失败或者加载失败的状态
                if (refreshState == STATE_REFRESHING) {
                    refreshState = state;
                    imageRefreshing.clearAnimation();
                    imageRefreshing.setVisibility(INVISIBLE);
//                    textTip.setText(R.string.refresh_fail);
                    smoothScrollTo(0);
                }
                break;
        }
    }

    public int getRefreshState() {
        return refreshState;
    }

    public void setRefreshComplete() {
        setState(STATE_DONE);
    }

    public void setRefreshFail() {
        setState(STATE_FAIL);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) refreshContentView.getLayoutParams();
//        return lp.height;
        return lp.topMargin;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) refreshContentView.getLayoutParams();
//        lp.height = height;
        lp.topMargin = height;
        refreshContentView.setLayoutParams(lp);
    }

    public void setPullToRefreshListener(PullToRefreshListener pullToRefreshListener) {
        this.pullToRefreshListener = pullToRefreshListener;
    }


    private void smoothScrollTo(int destHeight) {
        if (getVisibleHeight() == destHeight) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
    }


    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }


}
