package com.ooftf.demo.layout_chain.demo1

import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import com.ooftf.arch.frame.mvvm.activity.BaseViewBindingActivity
import com.ooftf.demo.layout_chain.R
import com.ooftf.demo.layout_chain.databinding.ActivityDemo1Binding

class Demo1Activity : BaseViewBindingActivity<ActivityDemo1Binding>() {
    val s: MotionLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            var progress = (-verticalOffset / appBarLayout.totalScrollRange.toFloat() * 2)
            if (progress < 1) {
                binding.motionLayout.progress = progress
                binding.motionLayout.loadLayoutDescription(R.xml.scene_09)
            }else{
                binding.motionLayout.progress = progress-1
                binding.motionLayout.loadLayoutDescription(R.xml.scene_appbar_2)
            }

        })
    }

}