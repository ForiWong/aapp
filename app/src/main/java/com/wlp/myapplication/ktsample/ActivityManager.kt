package com.wlp.myapplication.ktsample

import android.app.Activity

/**
 * 创建单例用于管理启动的Activity，定义删除所有Activity 的方法finishAll
 *
 * 定义基类用于，让其他所有的activity都实现这个，就能方便的管理Activity

Log.d("BaseActivity",javaClass.simpleName) 注意这个非常有用，当一个新的项目，不熟悉的时候加上这个启动应用程序可以清楚的看到页面启动了那个页面，便于定位功能属于哪个页面。

作者：何以邀微微
链接：https://www.jianshu.com/p/c1a20bb15f6d
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

ActivityManager.finishAll()
android.os.Process.killProcess(android.os.Process.myPid())
退出应用的时候只需要调用这个就可以了。注意killProcess这里只能杀死本应用进程，不能杀死另外一个进程。

ProgressBar 这个view是原生的进度，可以设置style="?android:attr/progressBarStyleHorizontal" 来变成横向的。

作者：何以邀微微
链接：https://www.jianshu.com/p/c1a20bb15f6d
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
object ActivityManager {
    private val activityList=ArrayList<Activity>()

    fun addActivity(activity: Activity){
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity){
        activityList.remove(activity)
    }

    fun finishAll(){
        for (activity in activityList){
            if (activity.isFinishing){
                activity.finish()
            }
        }
        activityList.clear()
    }
}