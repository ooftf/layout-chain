package com.ooftf.demo.layout_chain.demo3

import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseIntArray
import android.util.SparseBooleanArray
import android.widget.TextView
import android.view.ViewGroup
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import com.blankj.utilcode.util.ScreenUtils
import com.ooftf.basic.utils.getVisibleRectOfScreen
import java.util.*

class ChildAdapter : RecyclerView.Adapter<ChildAdapter.MyViewHolder>() {
    var random = Random()
    var sia = SparseIntArray()
    var h = SparseIntArray()
    var obserable = SparseBooleanArray()

    /**
     * 返回 item 个数
     * @return
     */
    override fun getItemCount(): Int {
        return sia.size()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Log.e("onBindViewHolder", "position::$position")
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = TextView(parent.context)
        textView.setPadding(10, 10, 10, 10)
        textView.gravity = Gravity.CENTER
        textView.setOnClickListener { }
        return MyViewHolder(textView)
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {

            //Log.e("onViewAttachedToWindow", "$holder  ::  ${holder.itemView.viewTreeObserver} :: ${holder.OnScrollChangedListener.hashCode()}" )
        holder.itemView.viewTreeObserver.addOnScrollChangedListener (holder.OnScrollChangedListener)
        obserable.put(holder.bindingAdapterPosition, false)

    }
    fun notifyShowChange(holder:RecyclerView.ViewHolder,show:Boolean){
            // 正确监听，view 显示在视野中，没有计算遮盖层
            //Log.e("ChildShowChange","$holder ::"+show)


    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
            //Log.e("onViewDetachedFromWindow", "$holder  ::  ${holder.itemView.viewTreeObserver} :: ${holder.OnScrollChangedListener.hashCode()}" )
            val oldShow = obserable.get(holder.bindingAdapterPosition)
            obserable.delete(holder.bindingAdapterPosition)
            holder.itemView.viewTreeObserver.removeOnScrollChangedListener(holder.OnScrollChangedListener)
            if(oldShow){
                notifyShowChange(holder,false)
            }


    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val OnScrollChangedListener = MyOnScrollChangedListener()
        inner class MyOnScrollChangedListener :OnScrollChangedListener{
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



    fun isShow(holder: MyViewHolder): Boolean {
        val frame = Rect()
        holder.itemView.getWindowVisibleDisplayFrame(frame)
        return  holder.itemView.getVisibleRectOfScreen().intersect(frame)
    }



    init {
        for (i in 0..199) {
            sia.put(i, random.nextInt(2))
            h.put(i, (random.nextInt(2) + 1) * 100)
        }
    }
}