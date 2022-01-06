package com.ooftf.demo.layout_chain.demo3

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import androidx.core.widget.ScrollerCompat
import androidx.recyclerview.widget.RecyclerView

class ParentRecyclerView : RecyclerView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    val flingHelper = FlingHelper(this)

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
        Log.e(
            "fling",
            "onNestedScroll"
        )
        scrollBy(0, dyUnconsumed)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.e(
            "fling",
            "onNestedPreScroll"
        )
        if (dy > 0) { //向上滑动
            if (canScrollVertically(1)) { // 没有到底部
                consumed[1] = dy
                scrollBy(0, dy)
            }
        }
    }

//    override fun fling(velocityX: Int, velocityY: Int): Boolean {
//
//        return super.fling(velocityX, velocityY)
//        return true
//    }
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        Log.e(
            "fling",
            "onNestedPreFling velocityY:"+velocityY
        )
        if (velocityY > 0) { // 向上 fling
            if (canScrollVertically(1)) { // 没有到底部
                fling(velocityX.toInt(), velocityY.toInt())
                return true
            }
        }
        return false
    }



    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        flingHelper.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }
    override fun computeScroll() {
        super.computeScroll()
        flingHelper.computeScroll()
    }


    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.e(
            "fling",
            "onNestedFling velocityY:"+velocityY
        )
        if (velocityY < 0) { // 向下 fling
            if (!target.canScrollVertically(-1)) { // 到顶z
               fling(velocityX.toInt(), velocityY.toInt())
            }
        } else {
            fling(velocityX.toInt(), velocityY.toInt())
        }
        return true
    }
}