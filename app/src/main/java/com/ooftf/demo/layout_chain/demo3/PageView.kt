package com.ooftf.demo.layout_chain.demo3

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PageView:LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
    val tabLayout:TabLayout = TabLayout(context)
    val viewPager: ViewPager2 = ViewPager2(context)
    init {
        orientation = VERTICAL
        addView(tabLayout, LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT))
        val lp = LayoutParams(LayoutParams.MATCH_PARENT,0)
        lp.weight = 1.0f
        addView(viewPager,lp)

        viewPager.adapter = object : FragmentStateAdapter(context as FragmentActivity){
            override fun getItemCount(): Int {
                return 5
            }

            override fun createFragment(position: Int): Fragment {
                return TestFragment()
            }

        }
        TabLayoutMediator(tabLayout,viewPager
        ) { tab, position -> tab.text = "第$position" }.attach()
    }
}