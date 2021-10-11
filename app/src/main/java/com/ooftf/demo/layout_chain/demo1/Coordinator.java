package com.ooftf.demo.layout_chain.demo1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.view.NestedScrollingParent;

public class Coordinator extends ViewGroup implements NestedScrollingParent {
    View header = null;
    View body = null;
    View toolbar = null;
    int headerHeight = 1000;
    int currentScroll = 0;
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
        if(body == null){
            body = findViewWithTag("body");
        }
        measureChildWithMargins(header,widthMeasureSpec,0,heightMeasureSpec,0);
        measureChildWithMargins(toolbar,widthMeasureSpec,0,heightMeasureSpec,0);
        measureChildWithMargins(body,widthMeasureSpec,0,heightMeasureSpec,toolbar.getMeasuredHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int getCanScrollDistance(){
        return headerHeight - toolbarHeight;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headerHeight = header.getMeasuredHeight();
        toolbarHeight = toolbar.getHeight();
        int width = r- l;
        int height = b - t;
        header.layout(0,-currentScroll,width,header.getMeasuredHeight()-currentScroll);
        int bodyTop = headerHeight-currentScroll-overlap;
        body.layout(0,bodyTop,width,body.getMeasuredHeight()+bodyTop);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return  true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if(headerHeight-toolbarHeight>=currentScroll){
            // 已经滚动到顶部
            if(dy>0){//向下滑动
                consumed[1]=0;
            }else{//向上滑动
                consumed[1]=0;
            }
        }{
            //没有滚动到顶部
            int newCurrentScroll =   currentScroll-dy;
            if(newCurrentScroll<0){
                newCurrentScroll = 0;
                consumed[1] = -(newCurrentScroll-currentScroll);
            }else if(newCurrentScroll>getCanScrollDistance()){
                newCurrentScroll = getCanScrollDistance();
                consumed[1] = -(newCurrentScroll-currentScroll);
            }else{
                consumed[1]=dy;
            }
            currentScroll = newCurrentScroll;
            overlap = overLapMax*(1-currentScroll/getCanScrollDistance());
        }
        requestLayout();
    }
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onStopNestedScroll(View child) {

    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if(velocityY>0){
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
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if(velocityY>0 && !consumed){
            return true;
        }else {
            return false;
        }
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
