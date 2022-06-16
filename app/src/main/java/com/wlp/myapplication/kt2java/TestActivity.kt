package com.wlp.myapplication.kt2java

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myapplication.R
import kotlinx.android.synthetic.main.activity_test.*


/**
 * Created by wlp on 2022/6/2
 * Description:
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        test_tv.setText("accomplish")

        //kt -> java
        test_tv.setOnClickListener(View.OnClickListener {
//            var intent = Intent(this, CopyLoginActivity::class.java)//
//            startActivity(intent)
        })
    }

}