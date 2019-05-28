package com.lixin.customview.statelayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixin.customview.R;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * 支持多种状态显示的frameLayout
 * Created by pokercc on 2018-8-2 10:43:35.
 */
public class StateFrameLayout extends FrameLayout {
    private static final String TAG = "StateView";
    private String errorMessage, emptyMessage, loadingMessage, netErrorMessage;
    private Drawable errorDrawable, emptyDrawable, loadingDrawable, netErrorDrawable;

    private StateViewCover stateViewCover;

    public enum ViewState {LOADING, CONTENT, EMPTY, NET_ERROR, OTHER_ERROR}

    private OnReloadListener onReloadListener;

    private static final OnClickListener DO_NOTHING_ON_CLICK_LISTENER = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    /**
     * 防止loading闪屏
     */
    private boolean avoidLoadingFlash = true;

    public StateFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public StateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyleAttr) {
        stateViewCover = new StateViewCover(getContext());
        addView(stateViewCover, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        stateViewCover.setOnClickListener(DO_NOTHING_ON_CLICK_LISTENER);
        if (attrs != null) {
            obtainAttributes(attrs, defStyleAttr);
        }
    }

    private void obtainAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StateFrameLayout, defStyleAttr, 0);


        errorMessage = typedArray.getString(R.styleable.StateFrameLayout_error_message);
        emptyMessage = typedArray.getString(R.styleable.StateFrameLayout_empty_message);
        loadingMessage = typedArray.getString(R.styleable.StateFrameLayout_loading_message);
        netErrorMessage = typedArray.getString(R.styleable.StateFrameLayout_net_error_message);

        errorDrawable = typedArray.getDrawable(R.styleable.StateFrameLayout_error_drawable);
        emptyDrawable = typedArray.getDrawable(R.styleable.StateFrameLayout_empty_drawable);
        loadingDrawable = typedArray.getDrawable(R.styleable.StateFrameLayout_loading_drawable);
        netErrorDrawable = typedArray.getDrawable(R.styleable.StateFrameLayout_net_error_drawable);

        typedArray.recycle();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        if (stateViewCover != null) {
            stateViewCover.setBackground(background);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (stateViewCover != null) {
            stateViewCover.setBackgroundColor(color);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setViewStatus(isInEditMode() ? ViewState.CONTENT : ViewState.LOADING);

    }

    private <T> T defaultIfNull(T e, T df) {
        return e == null ? df : e;
    }


    private int getColorPrimary() {


        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    private void setViewStatus(ViewState status) {
        if (status == null) {
            throw new IllegalArgumentException("status == null");
        }
        switch (status) {
            case LOADING:

                // 防止loading闪屏
                if (avoidLoadingFlash && stateViewCover.getVisibility() == GONE) {
                    break;
                }
                String message = defaultIfNull(loadingMessage, getResources().getString(R.string.view_state_loading_msg));
                showState(message, defaultIfNull(loadingDrawable, PcViewUtil.getLoadingDrawable(getContext())), false);

                break;
            case EMPTY:
                showState(defaultIfNull(emptyMessage, getResources().getString(R.string.view_state_empty_msg)), emptyDrawable, true);
                break;
            case CONTENT:
                stateViewCover.clearAnimator();
                stateViewCover.setVisibility(GONE);
                break;
            case NET_ERROR:
                showState(defaultIfNull(netErrorMessage, getResources().getString(R.string.view_state_net_error_msg)), netErrorDrawable, true);
                break;
            case OTHER_ERROR:
                showState(defaultIfNull(errorMessage, getResources().getString(R.string.view_state_error_msg)), errorDrawable, true);
                break;
        }

    }

    private void showState(String message, Drawable drawable, boolean reloadEnable) {
        stateViewCover.clearAnimator();
        stateViewCover.setMessage(message);
        stateViewCover.setDrawable(drawable);

        if (reloadEnable) {
            if (onReloadListener == null) {
                throw new RuntimeException("please set onReloadListener before");
            }
            stateViewCover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    if (onReloadListener == null) {
                        throw new RuntimeException("please set onReloadListener before");
                    }
                    onReloadListener.onReload(StateFrameLayout.this);
                }
            });
        } else {
            stateViewCover.setOnClickListener(DO_NOTHING_ON_CLICK_LISTENER);
        }
        stateViewCover.setVisibility(VISIBLE);
        stateViewCover.bringToFront();
    }


    public void showViewState(ViewState viewState) {
        showViewState(viewState, null, null);
    }

    public void showViewState(ViewState viewState, String message, Drawable drawable) {
        setViewStatus(viewState);
        if (message != null && !message.isEmpty()) {
            stateViewCover.setMessage(message);
        }
        if (drawable != null) {
            stateViewCover.setDrawable(drawable);
        }


    }

    /**
     * 设置防止loading闪屏
     *
     * @param avoidLoadingFlash
     */
    public void setAvoidLoadingFlash(boolean avoidLoadingFlash) {
        this.avoidLoadingFlash = avoidLoadingFlash;
    }

    public void showContentView() {
        setViewStatus(ViewState.CONTENT);
    }

    public void showLoadingView() {
        setViewStatus(ViewState.LOADING);
    }

    public void showErrorView() {
        setViewStatus(ViewState.OTHER_ERROR);
    }

    public void showNetErrorView() {
        setViewStatus(ViewState.NET_ERROR);
    }

    public void showEmptyView() {
        setViewStatus(ViewState.EMPTY);
    }

    public interface OnReloadListener {
        void onReload(View view);
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    static class StateViewCover extends RelativeLayout {

        private final TextView textView;
        private final ImageView imageView;
        private final LinearLayout linearLayout;

        public StateViewCover(Context context) {
            super(context);
            linearLayout = new LinearLayout(context);
            LayoutParams containerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            containerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            imageView = new ImageView(context);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageParams.bottomMargin = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
            linearLayout.addView(imageView, imageParams);
            textView = new TextView(context);
            textView.setSingleLine();
            linearLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.parseColor("#7a7a7a"));
            addView(linearLayout, containerParams);

        }

        @Override
        public void setOnClickListener(OnClickListener l) {
            super.setOnClickListener(l);
            linearLayout.setOnClickListener(l);
        }

        public void setMessage(String message) {
            textView.setText(message);
            requestLayout();
        }

        public void clearAnimator() {
            super.clearAnimation();
            if (imageView.getDrawable() instanceof AnimationDrawable) {
                ((AnimationDrawable) imageView.getDrawable()).stop();
            }
            imageView.clearAnimation();

        }

        public void setDrawable(Drawable drawable) {
            imageView.setImageDrawable(drawable);
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

}
