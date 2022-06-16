package com.wlp.myapplication.ktsample.life

import android.app.Activity
import android.location.Location
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


//让自定义组件能够感受到生命周期的变化，在自定义组件内部就可以管理好其生命周期，避免内存泄漏，降低模块间耦合度。
open class LocationComponent(
    var context: Activity,
    var onLocationChangedListener: OnLocationChangedListener
) : LifecycleObserver {
    val TAG = "LocationComponent"

    init {
        checkPermission()
    }

    private fun checkPermission() {
        //初始化操作
//        RxPermissionsTool.with(context)
//            .addPermission(Manifest.permission_group.LOCATION)
//            .initPermission()
//        Log.d(TAG, "initLocationManager")
//        if (!RxLocationTool.isGpsEnabled(context) || !RxLocationTool.isLocationEnabled(context)) {
//            RxLocationTool.openGpsSettings(context)
//        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startGetLocation() {
        Log.d(TAG, "startGetLocation")
//        RxLocationTool.registerLocation(
//            context,
//            1000,
//            5,
//            object : RxLocationTool.OnLocationChangeListener {
//                override fun getLastKnownLocation(location: Location) {
//
//                }
//
//                override fun onLocationChanged(location: Location) {
//                    Log.d(
//                        TAG,
//                        "onLocationChanged latitude：${location.latitude} longitude: ${location.longitude}"
//                    )
//                    onLocationChangedListener.onChanged(location)
//                }
//
//                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//
//                }
//            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopGetLocation() {
        Log.d(TAG, "stopGetLocation")
//        RxLocationTool.unRegisterLocation()
    }

    interface OnLocationChangedListener {
        fun onChanged(location: Location)
    }
}