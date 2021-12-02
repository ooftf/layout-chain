package com.ooftf.demo.layout_chain.demo4

import android.graphics.Color
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
import java.util.*

class MyAdapter(layout: StickyRecyclerViewLayout) : StickyAdapter<MyAdapter.MyViewHolder>(layout) {
    var random = Random()
    var sia = SparseIntArray()
    var h = SparseIntArray()

    /**
     * 返回 item 个数
     * @return
     */
    override fun getItemCount(): Int {
        return 1000
    }

    override fun onBindViewHolderEx(holder: MyViewHolder, position: Int) {
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

    override fun onCreateViewHolderEx(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = TextView(parent.context)
        textView.setPadding(10, 10, 10, 10)
        textView.gravity = Gravity.CENTER
        return MyViewHolder(textView)
    }

    /**
     * 返回 粘性布局的位置
     * @return
     */
    override fun getStickyPosition(): Int {
        return 30
    }

    /**
     * 返回view 的类型，所有 card 类型都是一样的，其他 根据 数据中 layout.type 类型的不同返回不同的值
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun isFullSpan(position: Int): Boolean {
        return sia[position] == 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        for (i in 0..999) {
            if (i == getStickyPosition()) {
                sia.put(i, 0)
                h.put(i, 200)
            } else {
                sia.put(i, random.nextInt(2))
                h.put(i, (random.nextInt(2) + 1) * 100)
            }
        }
    }
}