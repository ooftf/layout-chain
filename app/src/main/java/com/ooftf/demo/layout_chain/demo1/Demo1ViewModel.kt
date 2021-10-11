package com.ooftf.demo.layout_chain.demo1

import android.app.Application
import com.ooftf.arch.frame.mvvm.vm.BaseListViewModel
import com.ooftf.arch.frame.mvvm.vm.BaseViewModel
import com.ooftf.demo.layout_chain.R

/**
 * @author 
 * @email 
 * @date 2021-10-11
 */
class Demo1ViewModel (application: Application) : BaseListViewModel<String>(application) {
    init {
        (0..100).forEach {
            items.add(""+it)
        }

    }
    override fun getItemLayout(): Int {
        return R.layout.item_demo1
    }

}