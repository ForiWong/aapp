package com.wlp.myapplication.ktsample.vm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wlp.myapplication.R
import kotlinx.android.synthetic.main.activity_first3.*

/**
 * Created by wlp on 2022/6/9
 * Description:
 */
class Vm3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first3)
        var num = 0

        textview.text = "0" //这里的textView就是在main.xml中textview的id 相当于databing的作用 但是ketlin可以直接调用
        buttonPlus.setOnClickListener {//添加点击事件  这里面可以调用函数 也可以自己写多条语句  里面的it就是view
            textview.text = (++num).toString();
        }

        buttonMinus.setOnClickListener {
            textview.text = (--num).toString();
        }

        //viewModel的定义和初始化  最新版本不建议使用ViewModelProviders（以后可能删除） 现在使用ViewModelProvider
        var viewModel = ViewModelProvider(this).get(My2ViewModel::class.java);
        //给viewmodel中的number设置观察  当值发生变化时就会更新textview
        viewModel.number.observe(this, Observer { textview.text = it.toString() })

        buttonVmPlus.setOnClickListener {//加一操作
            viewModel.modifyNumber(2)
        }
        buttonVmMinus.setOnClickListener {//减一操作
            viewModel.modifyNumber(-2)
        }
    }

}