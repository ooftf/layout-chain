package com.ooftf.demo.layout_chain.demo4

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StickyRecyclerViewLayout:ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
    val recyclerView = RecyclerView(context)
    var mStickyView :View?=null
    init {
        addView(recyclerView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adapter = recyclerView.adapter as? StickyAdapter<RecyclerView.ViewHolder>
                    ?: return
                val placeholderView = adapter.placeholderViewHolder?.itemView
                var isTop = false
                if(placeholderView != null){
                    if(adapter.getStickyPosition()>findLastVisibleItemPosition()){
                        mStickyView?.visibility = View.GONE
                    }else{
                        mStickyView?.visibility = View.VISIBLE
                    }
                    if(findFirstVisibleItemPosition()>=adapter.getStickyPosition()){
                        isTop = true
                    }
                    if(isTop){
                        (mStickyView?.layoutParams as MarginLayoutParams).topMargin = 0
                        mStickyView?.requestLayout()
                    }else if(mStickyView?.visibility == View.VISIBLE){
                        placeholderView.top.apply {
                            val topMargin = 0.coerceAtLeast(this)
                            (mStickyView?.layoutParams as MarginLayoutParams).topMargin = topMargin
                            mStickyView?.requestLayout()
                        }
                    }

                }else{
                    ( recyclerView.layoutManager as? StaggeredGridLayoutManager)?.apply {
                        val result =  IntArray(this.spanCount)
                        findLastVisibleItemPositions(result)
                        if(adapter.getStickyPosition()>result[0]){
                            mStickyView?.visibility = View.GONE
                        }else{
                            mStickyView?.visibility = View.VISIBLE
                        }
                    }
                }



            }
        })
    }


    fun findFirstVisibleItemPosition(): Int {

        ( recyclerView.layoutManager as? StaggeredGridLayoutManager)?.apply {
            val result =  IntArray(this.spanCount)
            findFirstVisibleItemPositions(result)
            return result[0]
        }
        ( recyclerView.layoutManager as? LinearLayoutManager)?.apply {
            return findFirstVisibleItemPosition()
        }

        return 0
    }

    fun findLastVisibleItemPosition(): Int {
        ( recyclerView.layoutManager as? StaggeredGridLayoutManager)?.apply {
            val result =  IntArray(this.spanCount)
            findLastVisibleItemPositions(result)
            return result[0]
        }
        ( recyclerView.layoutManager as? LinearLayoutManager)?.apply {
            return findLastVisibleItemPosition()
        }
        return 0
    }
    fun setStickyView(view:View){
        mStickyView?.let {
            removeView(it)
        }
        mStickyView = view
        if(view.layoutParams == null){
            view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        }
        (view.layoutParams as LayoutParams).topToTop = LayoutParams.PARENT_ID
        addView(view)
    }



}