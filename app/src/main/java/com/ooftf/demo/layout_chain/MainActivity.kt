package com.ooftf.demo.layout_chain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ooftf.arch.frame.mvvm.activity.BaseMvvmActivity
import com.ooftf.arch.frame.mvvm.activity.BaseViewBindingActivity
import com.ooftf.demo.layout_chain.databinding.ActivityMainBinding
import com.ooftf.demo.layout_chain.demo1.Demo1Activity
import com.ooftf.demo.layout_chain.demo2.Demo2Activity
import com.ooftf.demo.layout_chain.demo3.Demo3Activity
import com.ooftf.demo.layout_chain.demo4.Demo4Activity

class MainActivity : BaseMvvmActivity<ActivityMainBinding,MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    data class Item(val name:String,val desc:String,val clazz:Class<*>)


}