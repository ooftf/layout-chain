package com.ooftf.demo.layout_chain.demo2

import android.content.Context
import android.util.AttributeSet
import com.ooftf.basic.utils.DensityUtil.dip2pxInt
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent
import com.ooftf.basic.utils.DensityUtil
import com.ooftf.demo.layout_chain.demo2.CoordinatorLayout.ProgressChangeListener
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import android.widget.OverScroller
import androidx.core.view.ScrollingView
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseIntArray
import android.widget.TextView
import android.view.Gravity
import com.ooftf.demo.layout_chain.demo3.PageView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.MotionEvent
import android.view.View
import com.ooftf.demo.layout_chain.demo4.StickyRecyclerViewLayout
import com.ooftf.demo.layout_chain.demo4.StickyAdapter
import java.util.ArrayList

class CoordinatorLayout : ViewGroup, NestedScrollingParent {
    var header: View? = null
    var body: View? = null
    var toolbar: View? = null
    var overLapMax = dip2pxInt(54f)
    var progressChangeListeners: MutableList<ProgressChangeListener> = ArrayList()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    fun addProgressChangedListener(listener: ProgressChangeListener) {
        progressChangeListeners.add(listener)
    }

    fun removeProgressChangedListener(listener: ProgressChangeListener) {
        progressChangeListeners.remove(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (header == null) {
            header = findViewWithTag("header")
        }
        if (body == null) {
            body = findViewWithTag("body")
        }
        if (toolbar == null) {
            toolbar = findViewWithTag("toolbar")
        }
        measureChildWithMargins(header, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(toolbar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(
            body,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            toolbar!!.measuredHeight
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    val canScrollDistance: Int
        get() = header!!.measuredHeight - toolbar!!.measuredHeight

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutHeader()
        layoutBody()
        layoutToolbar()
    }

    private fun layoutHeader() {
        val lp = header!!.layoutParams as MarginLayoutParams
        header!!.layout(
            lp.leftMargin,
            lp.topMargin,
            lp.leftMargin + header!!.measuredWidth,
            lp.topMargin + header!!.measuredHeight
        )
    }

    private val progress: Float
        private get() = scrollY / canScrollDistance.toFloat()

    private fun layoutBody() {
        val lp = body!!.layoutParams as MarginLayoutParams
        val overlap = (overLapMax * (1 - progress)).toInt()
        val bodyTop = header!!.measuredHeight - overlap + lp.topMargin
        body!!.layout(
            lp.leftMargin,
            bodyTop,
            lp.leftMargin + body!!.measuredWidth,
            body!!.measuredHeight + bodyTop
        )
    }

    private fun layoutToolbar() {
        toolbar!!.alpha = progress
        val lp = toolbar!!.layoutParams as MarginLayoutParams
        toolbar!!.layout(
            lp.leftMargin,
            lp.topMargin + scrollY,
            lp.leftMargin + toolbar!!.measuredWidth,
            lp.topMargin + scrollY + toolbar!!.measuredHeight
        )
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (dy > 0) {
            //没有滚动到顶部
            var newCurrentScroll = scrollY + dy
            if (newCurrentScroll < 0) {
                newCurrentScroll = 0
                consumed[1] = newCurrentScroll - scrollY
            } else if (newCurrentScroll > canScrollDistance) {
                newCurrentScroll = canScrollDistance
                consumed[1] = newCurrentScroll - scrollY
            } else {
                consumed[1] = dy
            }
            scrollBy(0, consumed[1])
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        if (dyUnconsumed < 0) {
            //没有滚动到顶部
            var consumedDy = 0
            var newCurrentScroll = scrollY + dyUnconsumed
            if (newCurrentScroll < 0) {
                newCurrentScroll = 0
                consumedDy = newCurrentScroll - scrollY
            } else if (newCurrentScroll > canScrollDistance) {
                newCurrentScroll = canScrollDistance
                consumedDy = newCurrentScroll - scrollY
            } else {
                consumedDy = dyUnconsumed
            }
            scrollBy(0, consumedDy)
        }
    }

    var scroller = OverScroller(context)
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return if (velocityY > 0) {
            if (scrollY >= canScrollDistance) {
                //已经到顶，不消费
                false
            } else {
                scroller.fling(
                    0,
                    scrollY,
                    0,
                    velocityY.toInt(),
                    0,
                    0,
                    0,
                    canScrollDistance
                )
                postInvalidate()
                true
            }
        } else false
    }

    override fun computeScroll() {
        //动画未完成
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.currY)
            //触发draw()，必须有这一步
            postInvalidate()
        }
        notifyProgressChange()
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        val targetOffset = getViewOffset(target)
        return if (velocityY < 0 && targetOffset == 0) {
            if (scrollY > 0) {
                scroller.fling(
                    0,
                    scrollY,
                    0,
                    velocityY.toInt(),
                    0,
                    0,
                    0,
                    canScrollDistance
                )
                postInvalidate()
                true
            } else {
                //已经到顶，不消费
                false
            }
        } else false
    }

    fun getViewOffset(target: View): Int {
        return if (target is ScrollingView) {
            (target as ScrollingView).computeVerticalScrollOffset()
        } else {
            target.scrollY
        }
    }

    fun notifyProgressChange() {
        val progress = progress
        for (listener in progressChangeListeners) {
            listener.onProgressChange(progress)
        }
    }

    override fun generateLayoutParams(lp: LayoutParams): LayoutParams {
        return MarginLayoutParams(lp)
    }

    override fun generateDefaultLayoutParams(): MarginLayoutParams {
        return MarginLayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    interface ProgressChangeListener {
        fun onProgressChange(progress: Float)
    }

    init {
        addProgressChangedListener(object : ProgressChangeListener {
            override fun onProgressChange(progress: Float) {
                layoutBody()
                layoutToolbar()
            }
        })
    }
}