package com.ooftf.demo.layout_chain.demo3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class ParentRecyclerView : RecyclerView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val result = super.onInterceptTouchEvent(e)
        return if (e.action == MotionEvent.ACTION_DOWN) { // 向下fling 的时候，不能向上滑动
            false
        } else result
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        if (target is ScrollingView) {
            requestDisallowInterceptTouchEvent(true)
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        scrollBy(0, dyUnconsumed)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (dy > 0) { //向上滑动
            if (canScrollVertically(1)) { // 没有到底部
                consumed[1] = dy
                scrollBy(0, dy)
            }
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (velocityY > 0) { // 向上 fling
            if (canScrollVertically(1)) { // 没有到底部
                Log.e(
                    "onNestedPreFling",
                    "::" + computeVerticalScrollOffset() + "::" + computeVerticalScrollRange()
                )
                fling(velocityX.toInt(), velocityY.toInt())
                return true
            }
        }
        return false
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        if (velocityY < 0) { // 向下 fling
            if (!target.canScrollVertically(-1)) { // 到顶
                fling(velocityX.toInt(), velocityY.toInt())
            }
        } else {
            fling(velocityX.toInt(), velocityY.toInt())
        }
        return true
    }
}