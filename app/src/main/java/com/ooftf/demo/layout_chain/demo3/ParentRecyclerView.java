package com.ooftf.demo.layout_chain.demo3;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ParentRecyclerView extends RecyclerView {

    public ParentRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean result =  super.onInterceptTouchEvent(e);
        if(e.getAction()==MotionEvent.ACTION_DOWN){// 向下fling 的时候，不能向上滑动
            return false;
        }
        return result;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if(target instanceof ScrollingView){
            requestDisallowInterceptTouchEvent(true);
        }

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        scrollBy(0, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {//向上滑动
            if (canScrollVertically(1)) {// 没有到底部
                consumed[1] = dy;
                scrollBy(0, dy);
            }
        }

    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (velocityY > 0) {// 向上 fling
            if (canScrollVertically(1)) {// 没有到底部
                Log.e("onNestedPreFling", "::" + computeVerticalScrollOffset() + "::" + computeVerticalScrollRange());
                fling((int) velocityX, (int) velocityY);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY < 0) { // 向下 fling
            if (!(target.canScrollVertically(-1))) {// 到顶
                fling((int) velocityX, (int) velocityY);
            }
        } else {
            fling((int) velocityX, (int) velocityY);
        }
        return true;
    }

}
