package com.ooftf.demo.layout_chain.demo1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;

public class Coordinator extends ViewGroup implements NestedScrollingParent {
    View header = null;
    View body = null;
    View toolbar = null;
    int headerHeight = 1000;
    int toolbarHeight = 200;
    int overLapMax = 100;
    int overlap = overLapMax;

    public Coordinator(Context context) {
        super(context);
    }

    public Coordinator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Coordinator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Coordinator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        header = findViewWithTag("header");
        toolbar = findViewWithTag("toolbar");
        body = findViewWithTag("body");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (body == null) {
            body = findViewWithTag("body");
        }
        measureChildWithMargins(header, widthMeasureSpec, 0, heightMeasureSpec, 0);
        measureChildWithMargins(toolbar, widthMeasureSpec, 0, heightMeasureSpec, 0);
        measureChildWithMargins(body, widthMeasureSpec, 0, heightMeasureSpec, toolbar.getMeasuredHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int getCanScrollDistance() {
        return headerHeight - toolbarHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        headerHeight = header.getMeasuredHeight();
        toolbarHeight = toolbar.getMeasuredHeight();
        int width = r - l;
        int height = b - t;
        header.layout(0, 0, width, header.getMeasuredHeight());
        overlap = (int) (overLapMax * (1 - getScrollY() / (float)getCanScrollDistance()));
        Log.e("overlap",""+overlap);
        int bodyTop = headerHeight - overlap;
        body.layout(0, bodyTop, width, body.getMeasuredHeight() + bodyTop);
        Log.e("headerHeight", "" + headerHeight);
        Log.e("overlap", "" + overlap);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.e("headerHeight", "" + headerHeight);
        Log.e("toolbarHeight", "" + toolbarHeight);
        Log.e("getScrollY", "" + getScrollY());
        if (getScrollY() >= headerHeight - toolbarHeight) {
            // 已经滚动到顶部
            if (dy > 0) {//向上滑动
                consumed[1] = 0;
            } else {//向下滑动
                consumed[1] = dy;
            }
        } else {
            //没有滚动到顶部
            int newCurrentScroll = getScrollY() + dy;
            if (newCurrentScroll < 0) {
                newCurrentScroll = 0;
                consumed[1] = newCurrentScroll - getScrollY();
            } else if (newCurrentScroll > getCanScrollDistance()) {
                newCurrentScroll = getCanScrollDistance();
                consumed[1] = newCurrentScroll - getScrollY();
            } else {
                consumed[1] = dy;
            }
        }
        scrollBy(dx, consumed[1]);
        requestLayout();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        /*if(velocityY>0){
            //向上滑动
            if(headerHeight-toolbarHeight>=currentScroll){
                // 已经滚动到顶部
                return false;
            }else{
                return true;
            }
        }else{
            //向下滑动
            return false;
        }*/
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        /*if(velocityY>0 && !consumed){
            return true;
        }else {
            return false;
        }*/
        return false;
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new MarginLayoutParams(lp);
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
