package com.wlp.myapplication.kt2

import java.lang.IllegalArgumentException

/**
 * Created by wlp on 2022/5/20
 * Description:
 */
//kt语言的可空性特点
fun main() {

    //默认是不可空类型的，不能随意赋值为null
    var name: String = "Derry"
    //name = null //Null can not be a value of a non-null type String

    //声明时指定为可空类型 ?
    var name2: String?
    name2 = null
    println(name2)

    //安全调用操作符
    //name2.capitalize()
    //Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type String?
    val r = name?.capitalize() //如果为null，这句不执行，下面的打印还是会执行的
    println(r)

    //let的安全调用
    var names: String? = null
    names = ""

    //name 是可空类型的，如果真的是null， ？后面的这一段不执行，就不会引发空指针异常
    val r2 = names?.let{
        // it == name 本身
        // 如果能够执行这里面的，it一定不为null
        if(it.isBlank()){
            "Default"
        }else{
            "[$it]"
        }
    }
    println(r2)


    //非空断言操作符
    val r3 = names!!.capitalize()
    //断言，不管name是不是null，都执行，这个和java一样
    //如果为空，就会空指针异常

    if(names != null){

    }else{

    }

    var info2: String? = "lxl"
    info2 = null

    //空合并操作符
    println( info2 ?: "原来你是null啊")//如果info2是null,就会得到 ?: 后面的

    //let函数 + 空合并操作符
    println(info2?.let{"[$it]"} ?: "原来你是null啊")


    //异常处理 与 自定义异常
    try{
        var info: String? = null

        checkException(info)

        println(info!!.length)
    }catch (e: Exception){
        println("aaa:$e")
    }
}

fun checkException(info: String?){
    info ?: throw CustomException()
}

class CustomException : IllegalArgumentException("你的代码出错了")

//先决条件函数
fun main12(){
    var value: String ?= null
    var value2: Boolean = true

    checkNotNull(value)

    requireNotNull(value)

    require(value2)
}

//subString
fun main13(){
    val info = "122fffff"
    val indexOf = info.indexOf('f')

    println(info.substring(0, indexOf))
    println(info.substring(0 until indexOf))
}

//split
fun mainSplit(){
    val json = "kk, ll, ee, ff"

    //list 自动类型推断为list == List<String>
    val list = json.split(",")

    //C++解构 kt 也有解构
    val(v1, v2, v3, v4) = list
}


//...45