package com.ooftf.demo.layout_chain.demo4

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.RuntimeException

abstract class StickyAdapter<VH:RecyclerView.ViewHolder>(val layout: StickyRecyclerViewLayout): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val stickyItemViewType = 3857
    var placeholderViewHolder:RecyclerView.ViewHolder?=null
    var stickyViewHolder:VH?=null

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (stickyItemViewType == viewType) { View(parent.context)

            if(stickyViewHolder == null){
                stickyViewHolder = onCreateViewHolderEx(layout, viewType)
                layout.setStickyView(stickyViewHolder!!.itemView)
                stickyViewHolder!!.itemView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    placeholderViewHolder?.itemView?.layoutParams?.height = v.height
                }
            }
            if(placeholderViewHolder == null){
                placeholderViewHolder =  object :RecyclerView.ViewHolder(View(parent.context)){

                }
                placeholderViewHolder?.setIsRecyclable(false)
            }
            placeholderViewHolder!!
        } else {
            onCreateViewHolderEx(parent, viewType)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getStickyPosition() == position) {
            stickyViewHolder?.let {
                onBindViewHolderEx(it, position)
            }
        } else {
            onBindViewHolderEx(holder as VH, position)
        }
    }
    abstract fun onBindViewHolderEx(holder: VH, position: Int)
    abstract fun onCreateViewHolderEx(parent: ViewGroup, viewType: Int): VH


    override fun getItemViewType(position: Int): Int {
        if (getStickyPosition() == position) {
            return stickyItemViewType
        } else {
            var type = super.getItemViewType(position)
            if(type == stickyItemViewType){
                throw RuntimeException("$type 已经被 stickyView 占用")
            }
            return type
        }

    }

    abstract fun getStickyPosition(): Int


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = isFullSpan(holder.adapterPosition)
        }
    }

    open fun isFullSpan(position:Int) = false

}
