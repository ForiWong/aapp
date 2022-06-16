package com.wlp.myapplication.kt2java

/**
 * Created by wlp on 2022/6/2
 * Description:
 */
class MyKtJava: MyJava() {

    override fun getAllInfo(): String {
        return super.getAllInfo() + "kt"
    }

}