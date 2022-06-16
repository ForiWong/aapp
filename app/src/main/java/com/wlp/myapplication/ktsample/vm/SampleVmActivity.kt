package com.wlp.myapplication.ktsample.vm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wlp.myapplication.R

class SampleVmActivity : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first2)
        //获取ViewModel
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
    }
}
