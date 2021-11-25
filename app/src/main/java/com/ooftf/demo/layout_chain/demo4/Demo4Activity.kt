package com.ooftf.demo.layout_chain.demo4

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ooftf.demo.layout_chain.R


class Demo4Activity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo4)
        val recyclerViewLayout = findViewById<StickyRecyclerViewLayout>(R.id.recyclerView)
        recyclerViewLayout.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerViewLayout.recyclerView.adapter = MyAdapter(recyclerViewLayout)

    }
}