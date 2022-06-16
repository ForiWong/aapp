package com.wlp.myapplication.ktsample.life

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wlp.myapplication.R
import com.wlp.myapplication.databinding.ActivityFirst2Binding

class LifeCycleActivity : AppCompatActivity() {

    lateinit var binding: ActivityFirst2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityFirst2Binding>(this, R.layout.activity_first2)

        lifecycle.addObserver(
            LocationComponent(this,
                object : LocationComponent.OnLocationChangedListener {
                    override fun onChanged(location: Location) {
                        //展示收到的位置信息
                        binding.textview.text =
                            "latitude:${location.latitude}  longitude:${location.longitude}"
                    }
                })
        )
    }
}