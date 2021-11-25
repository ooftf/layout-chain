package com.ooftf.demo.layout_chain

import android.app.Application
import android.content.Intent
import android.view.View
import com.ooftf.arch.frame.mvvm.vm.BaseListViewModel
import com.ooftf.demo.layout_chain.demo1.Demo1Activity
import com.ooftf.demo.layout_chain.demo2.Demo2Activity
import com.ooftf.demo.layout_chain.demo3.Demo3Activity
import com.ooftf.demo.layout_chain.demo4.Demo4Activity

class MainViewModel(application: Application) : BaseListViewModel<MainActivity.Item>(application) {
    init {
        items.apply {
            add(MainActivity.Item("demo1", "MotionLayout demo", Demo1Activity::class.java))
            add(MainActivity.Item("demo2", "头部区域和列表的一种联动效果，渐变和错位遮挡", Demo2Activity::class.java))
            add(MainActivity.Item("demo3", "仿京东淘宝商品详情页面", Demo3Activity::class.java))
            add(MainActivity.Item("demo4", "一种 RecyclerView item 吸顶方案", Demo4Activity::class.java))
        }
    }
    override fun getItemLayout(): Int {
        return R.layout.item_main
    }

    override fun onItemClick(v: View, item: MainActivity.Item) {
        getActivity()?.apply {
            startActivity(Intent(this,item.clazz))
        }
    }
}