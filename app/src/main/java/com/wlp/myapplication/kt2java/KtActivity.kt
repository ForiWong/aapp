package com.wlp.myapplication.kt2java

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wlp.myapplication.R
import kotlinx.android.synthetic.main.activity_kt.*

class KtActivity : AppCompatActivity /* IGBaseActivity<ActivityKtBinding, BaseViewModel<BaseModel>>*/() {

//## net网络库Retrofit、MVVM 转成kotlin
// 网络请求协程的使用
//## Mvvm的基础类BaseActivity\Fragment\ViewModel\Model

//相互调用各种情况：
//√  kotlin可以继承java类/接口吗?
//√  反之，java类可以继承kotlin类/接口吗？
//√  java页面跳转到kotlin页面？反之，kotlin跳转java
//√  kotlin引用java类的方法：new构造函数、static静态、单例类DataCenter、Utils、自定义组件
//√ java转kotlin，工具

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kt)

        to_test.text = "去测试页"

        //kt -> kt
        to_test.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,TestActivity::class.java)
            startActivity(intent)

//            Api.getApiService().sms("mobile", 1)
//                //.compose(RxHelper.bindToLifecycle(getLifecycleProvider()))
//                .compose(RxHelper.io_main<Any>())
//                .subscribe(object : CommonObserver<BaseResponse<Any>>() {
//                    override fun onError(errorMsg: String) {
//                        ToastUtils.showShort2(errorMsg, R.string.net_error)
//                    }
//
//                    override fun onSuccess(o: BaseResponse<Any>) {
//
//                    }
//                })
        })

//        var ad = AdTextBean("content", "name")

        var app_use = "使用java类："
//        app_use += AppUtil.getAppName(this) + AppUtil.getVersionName(this)
//        app_use += ad.adName + ad.adContent
        tv_app_use.text = app_use
    }

//    override fun initVariableId(): Int {
//        return 0
//    }
//
//    override fun initContentView(savedInstanceState: Bundle?): Int {
//        return R.layout.activity_kt
//    }
}