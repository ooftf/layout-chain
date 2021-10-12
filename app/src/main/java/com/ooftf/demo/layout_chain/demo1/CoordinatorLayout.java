package com.ooftf.demo.layout_chain.demo1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.recyclerview.widget.RecyclerView;

import com.ooftf.basic.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent {
    View header = null;
    View body = null;
    View toolbar = null;
    int overLapMax = DensityUtil.INSTANCE.dip2pxInt(54);
    List<ProgressChangeListener> progressChangeListeners = new ArrayList<>();
    public CoordinatorLayout(Context context) {
        super(context);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    {
        addProgressChangedListener(new ProgressChangeListener() {
            @Override
            public void onProgressChange(float progress) {
                layoutBody();
                layoutToolbar();
            }
        });
    }

    public void addProgressChangedListener(ProgressChangeListener listener){
        progressChangeListeners.add(listener);
    }
    public void removeProgressChangedListener(ProgressChangeListener listener){
        progressChangeListeners.remove(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (header == null) {
            header = findViewWithTag("header");
            header.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    //requestLayout();
                }
            });
        }
        if (body == null) {
            body = findViewWithTag("body");
        }
        if (toolbar == null) {
            toolbar = findViewWithTag("toolbar");
        }
        measureChildWithMargins(header, widthMeasureSpec, 0, heightMeasureSpec, 0);
        measureChildWithMargins(toolbar, widthMeasureSpec, 0, heightMeasureSpec, 0);
        measureChildWithMargins(body, widthMeasureSpec, 0, heightMeasureSpec, toolbar.getMeasuredHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int getCanScrollDistance() {
        return header.getMeasuredHeight() - toolbar.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int headerLeft = ((MarginLayoutParams) header.getLayoutParams()).leftMargin;
        header.layout(headerLeft, 0, headerLeft + header.getMeasuredWidth(), header.getMeasuredHeight());
        layoutBody();
        layoutToolbar();
    }

    private float getProgress() {
        return getScrollY() / (float) getCanScrollDistance();
    }
    private void layoutBody(){
        int overlap = (int) (overLapMax * (1 - getProgress()));
        int bodyTop = header.getMeasuredHeight() - overlap;
        int bodyLeft = ((MarginLayoutParams) body.getLayoutParams()).leftMargin;
        body.layout(bodyLeft, bodyTop, bodyLeft + body.getMeasuredWidth(), body.getMeasuredHeight() + bodyTop);
    }
    private void layoutToolbar() {
        toolbar.setAlpha(getProgress());
        int toolbarLeft = ((MarginLayoutParams) toolbar.getLayoutParams()).leftMargin;
        toolbar.layout(toolbarLeft, getScrollY(), toolbarLeft + toolbar.getMeasuredWidth(), getScrollY() +toolbar.getMeasuredHeight());
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        if (dy > 0) {
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
            scrollBy(0, consumed[1]);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        if (dyUnconsumed < 0) {
            //没有滚动到顶部
            int consumedDy = 0;
            int newCurrentScroll = getScrollY() + dyUnconsumed;
            if (newCurrentScroll < 0) {
                newCurrentScroll = 0;
                consumedDy = newCurrentScroll - getScrollY();
            } else if (newCurrentScroll > getCanScrollDistance()) {
                newCurrentScroll = getCanScrollDistance();
                consumedDy = newCurrentScroll - getScrollY();
            } else {
                consumedDy = dyUnconsumed;
            }
            scrollBy(0, consumedDy);
        }
    }

    OverScroller scroller = new OverScroller(getContext());
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

        if (velocityY > 0) {
            if(getScrollY()>=getCanScrollDistance()){
                //已经到顶，不消费
                return false;
            }else{
                scroller.fling(0,
                        getScrollY(),
                        (int)velocityX,
                        (int)velocityY,
                        0,
                        0,
                        0,
                        getCanScrollDistance());
                postInvalidate();
                return true;
            }
        }
        return false;
    }
    @Override
    public void computeScroll() {
        //动画未完成
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            //触发draw()，必须有这一步
            postInvalidate();
        }
        notifyProgressChange();
    }


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        int targetOffset = getViewOffset(target);
        if (velocityY < 0&&targetOffset==0) {
            if(getScrollY()>0){
                scroller.fling(0,
                        getScrollY(),
                        (int)velocityX,
                        (int)velocityY,
                        0,
                        0,
                        0,
                        getCanScrollDistance());
                postInvalidate();
                return true;
            }else{
                //已经到顶，不消费
                return false;
            }
        }
        return false;
    }

    int getViewOffset(View target){
        if(target instanceof ScrollingView){
            return  ((ScrollingView) target).computeVerticalScrollOffset();
        }else{
            return target.getScrollY();
        }
    }

    void notifyProgressChange(){
        float progress = getProgress();
        for(ProgressChangeListener listener : progressChangeListeners){
            listener.onProgressChange(progress);
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

    static interface ProgressChangeListener{
       void onProgressChange(float progress);
    }
}
