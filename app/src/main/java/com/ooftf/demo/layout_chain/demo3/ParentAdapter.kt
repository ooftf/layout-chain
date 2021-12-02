package com.ooftf.demo.layout_chain.demo3

import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.ooftf.basic.utils.getVisibleRectOfScreen
import java.util.*

class ParentAdapter : RecyclerView.Adapter<ParentAdapter.MyViewHolder>() {
    var random = Random()
    var sia = SparseIntArray()
    var h = SparseIntArray()

    /**
     * 返回 item 个数
     * @return
     */
    override fun getItemCount(): Int {
        return sia.size() + 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder.itemView as TextView).height = h[position]
            holder.itemView.setBackgroundColor(
                Color.argb(
                    255,
                    random.nextInt(255),
                    random.nextInt(255),
                    random.nextInt(255)
                )
            )
            holder.itemView.text = "i::$position"
        } else {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (viewType == 0) {
            val textView = TextView(parent.context)
            textView.setPadding(10, 10, 10, 10)
            textView.gravity = Gravity.CENTER
            textView.setOnClickListener { }
            MyViewHolder(textView)
        } else {
            val pageView = PageView(parent.context)
            pageView.layoutParams = StaggeredGridLayoutManager.LayoutParams(
                StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,
                StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT
            )
            MyViewHolder(pageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < h.size()) {
            0
        } else {
            1
        }
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        lp.isFullSpan = isFullSpan(holder.bindingAdapterPosition)
        holder.itemView.viewTreeObserver.addOnScrollChangedListener (holder.OnScrollChangedListener)
        obserable.put(holder.bindingAdapterPosition, false)
    }
    var obserable = SparseBooleanArray()

    fun isFullSpan(position: Int): Boolean {
        return if (position == h.size()) {
            true
        } else sia[position] == 0
    }

    fun notifyShowChange(holder:RecyclerView.ViewHolder,show:Boolean){
        Log.e("ParentChange","$holder ::"+show)
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {

        val oldShow = obserable.get(holder.bindingAdapterPosition)
        obserable.delete(holder.bindingAdapterPosition)
        holder.itemView.viewTreeObserver.removeOnScrollChangedListener(holder.OnScrollChangedListener)
        if(oldShow){
            notifyShowChange(holder,false)
        }

    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val OnScrollChangedListener = MyOnScrollChangedListener()
        inner class MyOnScrollChangedListener : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                val nowShow = isShow(this@MyViewHolder)
                val oldShow = obserable.get(bindingAdapterPosition)
                if(nowShow!=oldShow){
                    notifyShowChange(this@MyViewHolder,nowShow)
                    obserable.put(bindingAdapterPosition, nowShow)
                }
            }

        }
    }

    fun isShow(view: MyViewHolder): Boolean {
        val r = view.itemView.getVisibleRectOfScreen()
        return r.intersect(screen)
    }

    val screen : Rect = Rect().apply {
        set(0,0, ScreenUtils.getAppScreenWidth(), ScreenUtils.getAppScreenHeight())
    }

    init {
        for (i in 0..19) {
            sia.put(i, random.nextInt(2))
            h.put(i, (random.nextInt(2) + 1) * 100)
        }
    }
}