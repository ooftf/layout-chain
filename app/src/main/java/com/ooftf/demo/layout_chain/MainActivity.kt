package com.ooftf.demo.layout_chain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ooftf.demo.layout_chain.demo0.Demo0Activity
import com.ooftf.demo.layout_chain.demo1.Demo1Activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, Demo1Activity::class.java))
    }


}