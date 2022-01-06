package com.ooftf.demo.layout_chain.demo3;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.OverScroller;

import androidx.recyclerview.widget.RecyclerView;

public class FlingHelper {
    RecyclerView recyclerView;
    OverScroller scroll;

    FlingHelper(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        scroll = new OverScroller(recyclerView.getContext());
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                fling(velocityY);
                return true;
            }
        });
    }

    int currentY = 0;

    void fling(int velocityY) {
        currentY = 0;
        scroll.fling(0, currentY, 0, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        recyclerView.postInvalidate();
    }


    void dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            scroll.forceFinished(true);
        }
    }

    void computeScroll() {
        if (scroll.computeScrollOffset()) {
            if (scroll.getCurrY() > 0) {//向上滑动
                if (recyclerView.canScrollVertically(1)) {// 没有到底部
                    recyclerView.scrollBy(0, scroll.getCurrY() - currentY);
                    currentY = scroll.getCurrY();
                    recyclerView.postInvalidate();
                } else {
                    RecyclerView child = getChildRecyclerView(recyclerView);
                    if (child != null) {
                        child.fling(0, (int) scroll.getCurrVelocity());
                    }
                    scroll.forceFinished(true);
                }
            } else {
                if (recyclerView.canScrollVertically(-1)) {// 没有到顶部
                    recyclerView.scrollBy(0, scroll.getCurrY() - currentY);
                    currentY = scroll.getCurrY();
                    recyclerView.postInvalidate();
                } else {
                    RecyclerView parentRecyclerView = getParentRecyclerView(recyclerView);
                    if(parentRecyclerView!=null){
                        parentRecyclerView.fling(0, (int) -scroll.getCurrVelocity());
                    }
                    scroll.forceFinished(true);
                }
            }

        }
    }


    RecyclerView getChildRecyclerView(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childAt = parent.getChildAt(i);
            if (childAt instanceof RecyclerView) {
                boolean localVisibleRect = childAt.getLocalVisibleRect(new Rect());
                if(localVisibleRect){
                    return (RecyclerView) childAt;
                }
            } else if (childAt instanceof ViewGroup) {
                RecyclerView canScrollUpChild = getChildRecyclerView((ViewGroup) childAt);
                if (canScrollUpChild != null) {
                    return canScrollUpChild;
                }
            }
        }
        return null;
    }

    RecyclerView getParentRecyclerView(View child) {
        ViewParent parent = child.getParent();
        if (parent == null) {
            return null;
        } else if (parent instanceof RecyclerView) {
            return (RecyclerView) parent;
        }  else if (parent instanceof View) {
            return getParentRecyclerView((View) parent);
        }
        return null;
    }
}
