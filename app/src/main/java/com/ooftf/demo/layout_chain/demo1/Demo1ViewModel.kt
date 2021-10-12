package com.ooftf.demo.layout_chain.demo1

import android.app.Application
import android.os.Handler
import com.ooftf.arch.frame.mvvm.vm.BaseListViewModel
import com.ooftf.arch.frame.mvvm.vm.BaseViewModel
import com.ooftf.basic.armor.ObservableArrayListPro
import com.ooftf.demo.layout_chain.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author 
 * @email 
 * @date 2021-10-11
 */
class Demo1ViewModel (application: Application) : BaseListViewModel<String>(application) {


    val items2 = ObservableArrayListPro<String>()
    val itemBinding2 by lazy {
        createItemBinding()
    }
    init {

        Handler().postDelayed ({
            (0..100).forEach {
                items.add(""+it)
            }
            (0..6).forEach {
                items2.add(""+it)
            }
        },2000)

    }
    override fun getItemLayout(): Int {
        return R.layout.item_demo1
    }

}