package com.wlp.myapplication.kt2

import java.lang.IllegalArgumentException
import kotlin.math.roundToInt

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

//replace
fun main45(){
    val srcPwd = "ABCDEFGHIJKLMNOPQ"

    val newPwd = srcPwd.replace(Regex("[AKM]")){
        it.value //完全没有做任何操作

        when(it.value){
            "A" -> "9"
            "M" -> "8"
            "K" -> "7"
            else -> it.value
        }
    }
}

//46. == 与 === 的区别
// == 值、内容的比较 相当于java的equals
// === 引用的比较
fun main46(){
    val name1 = "DERRY"
    val name2 = "DERRY"
    val name3 = "ww"

    println(name1.equals(name2))//java
    println(name1 == name2)//kt

    println(name1 === name2)//true 常量池
    println(name1 === name3)//false

    val name4 = "derry".capitalize()
    println(name1 === name4)//false
}

//47、字符串的遍历操作
fun main47(){
    val src = "ABCDEFGHIJKLMNOPQ"

    src.forEach { c ->
        //println("所有的字符是：$it ")
        println("所有的字符是：$c ")
    }
}

//48、数字类型的安全转换函数
fun main48(){
    val number : Int = "666".toInt()

    val number2 : Int = "666.6".toInt()//崩溃

    val number3: Int? = "666.6".toIntOrNull()

    val number4: Int? = "888.8".toIntOrNull()
    println(number4 ?: "原来你是null,转换错误了")
}

//49、Double转Int 与类型格式化
fun main49(){
    println(65.455.toInt()) //舍去小数
    println(65.555.roundToInt()) //四舍五入

    println("%.3f".format(65.555666))//格式化
}

//50、apply内置函数
// 有点像链式编程
fun main50(){
    val info = "123dff"

    info.apply {
        println("长度是：$length")
    }.apply {
        println("${this[length - 1]}")
    }.apply {
        println("小写：${toLowerCase()}")
    }
}

//51、let内置函数
fun main53(){
    val result2 = listOf(6,5,2,3,5,7).let {
        //it == list集合
        //匿名函数的最后一行，作为返回值。
        it.first() + it.first()
    }
    println(result2)
}

fun getMethod2(value: String?) = if (value == null) "你是null" else "你是$value"

//run
//run 函数返回类型，是根据匿名函数最后一行返回的
//run 函数的匿名函数持有的是this == str 本身
fun main54(){
    val str = "1234ppp"

    str
        .run(::isLong)
        .run(::showText)
        .run(::mapText)
        .run(::println)

    str.run{
        this.length > 5
    }.run{
        if(this) "合格" else "不合格"
    }.run{
        "[$this]"
    }.run{
        println(this)
    }
}

fun isLong(str: String) = str.length > 5

fun showText(isLong: Boolean) = if (isLong) "合格" else "不合格"

fun mapText(getShow: String) = "【$getShow】"

