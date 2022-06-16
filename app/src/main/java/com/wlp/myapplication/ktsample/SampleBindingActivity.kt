package com.wlp.myapplication.ktsample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wlp.myapplication.R
import com.wlp.myapplication.databinding.ActivityFirst2Binding
import com.wlp.myapplication.ktsample.data.Person


/**
 * Created by wlp on 2022/6/9
 * Description: 使用DataBinding
 */
class SampleBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding = DataBindingUtil.setContentView<ActivityFirst2Binding>(this, R.layout.activity_first2)
        //绑定数据
        dataBinding.person = Person("yiyi","women")
        dataBinding.activity = this;
    }

    //注意：方法里面需要参数view
    fun onClick(view: View) {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
    }

    fun onClickTwo(view: View) {
        Toast.makeText(this, "world", Toast.LENGTH_SHORT).show()
    }
}