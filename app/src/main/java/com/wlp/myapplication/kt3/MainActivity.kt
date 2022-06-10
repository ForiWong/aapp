package com.wlp.myapplication.kt3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wlp.myapplication.R
import kotlinx.android.synthetic.main.activity_login.*

class MainActivity : AppCompatActivity() {
    private lateinit var mViewModel: Main2ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mViewModel = ViewModelProvider(this)[Main2ViewModel::class.java]
        initListener()
    }

    private fun initListener() {
        login.setOnClickListener { mViewModel.getUserData() }

        mViewModel.user.observe(this, Observer {
            if (it.first) {
                login.text = it.second
            } else {
                Toast.makeText(this, it.second, Toast.LENGTH_SHORT).show()
            }
        })
    }
}