package com.yefoo.demopullrecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LoadMoreView extends LinearLayout {
    private ImageView imageLoadMore;
    private TextView textTip;

    private int whiteColor, blackColor;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View loadMoreContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_recyclerview_load_more_footer, this, false);
        LayoutParams params = (LayoutParams)loadMoreContentView.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        loadMoreContentView.setLayoutParams(params);
//        imageLoadMore = loadMoreContentView.findViewById(R.id.load_more_footer_iv);
        textTip = loadMoreContentView.findViewById(R.id.load_more_footer_tv);
        imageLoadMore = loadMoreContentView.findViewById(R.id.load_more_footer_iv);
        setGravity(Gravity.CENTER);
        addView(loadMoreContentView);

        whiteColor = ContextCompat.getColor(context, R.color.white);
        blackColor = ContextCompat.getColor(context, R.color.black53);

//        int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//        imageLoadMore.measure(w, h);
//        int imageWidth = imageLoadMore.getMeasuredWidth();
//        float textWidth = textTip.getPaint().measureText(textTip.getText().toString());
//        int deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageLoadMore.getLayoutParams();
//        layoutParams.leftMargin = (int) ((deviceWidth - (imageWidth + textWidth)) / 2);
//        textTip.setLayoutParams(layoutParams);
    }

    public void setLoadMoreResource(int resId) {
        imageLoadMore.setImageResource(resId);
    }

    public void setLoadMoreTheme(boolean isWhite){
//        if (isWhite){
//            imageLoadMore.setImageResource(R.drawable.common_tap_white_loading_drawable);
//            textTip.setTextColor(whiteColor);
//        } else {
//            imageLoadMore.setImageResource(R.drawable.common_tap_loading_drawable);
//            textTip.setTextColor(blackColor);
//        }
    }

    public void startAnimation() {
        try {
            AnimationDrawable drawable = (AnimationDrawable) imageLoadMore.getDrawable();
            drawable.start();
            imageLoadMore.setVisibility(View.VISIBLE);
            textTip.setVisibility(GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAnimation() {
        try {
            AnimationDrawable drawable = (AnimationDrawable) imageLoadMore.getDrawable();
            drawable.stop();
            imageLoadMore.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMoreComplete(boolean isShow, String msg) {
        if (isShow) {
            if (TextUtils.isEmpty(msg)) {
                textTip.setVisibility(VISIBLE);
                stopAnimation();
            } else {
                textTip.setVisibility(VISIBLE);
                startAnimation();
                textTip.setText(msg);
            }
        } else {
            textTip.setVisibility(GONE);
            stopAnimation();
        }
    }

    public void loadMoreEnd(boolean showLoading, String text) {

        if (showLoading){
            startAnimation();
            if (TextUtils.isEmpty(text)){
                textTip.setVisibility(GONE);
            } else {
                textTip.setVisibility(VISIBLE);
                textTip.setText(text);
            }
        } else {
            stopAnimation();
            if (TextUtils.isEmpty(text)){
                textTip.setVisibility(GONE);
            } else {
                textTip.setVisibility(VISIBLE);
                textTip.setText(text);
            }
        }
    }

    public void loadMoreFail(String errorMsg) {
//        textTip.setVisibility(VISIBLE);
//        imageLoadMore.setVisibility(INVISIBLE);
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setVisibility(GONE);
//                stopAnimation();
//                refreshRecyclerView.scrollBy(0, -getHeight());
//                textTip.setVisibility(INVISIBLE);
//                imageLoadMore.setVisibility(VISIBLE);
//            }
//        }, 800);

        if (TextUtils.isEmpty(errorMsg)) {
            textTip.setVisibility(GONE);
        } else {

        }
    }

    public void onClick() {
        if (textTip != null && textTip.getVisibility() != GONE) {
            textTip.setVisibility(GONE);
        }
        if (imageLoadMore != null) {
            imageLoadMore.setVisibility(VISIBLE);
            startAnimation();
        }
    }
}
