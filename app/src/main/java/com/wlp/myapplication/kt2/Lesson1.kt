package com.wlp.myapplication.kt2

/**
 * Created by wlp on 2022/5/17
 * Description:kotlin 学习
 */
class Lesson1 {

    fun main(){
        println("Hello World")
        //声明变量
        /**
         * 可读可改 变量名 类型   值
         * var name: String = "Derry"
         *
         */
        var name: String = "Derry"
        name = "Lance"
        println(name)

        /**
         * 内阻数据类型
         * String  字符串
         * Char 单字符
         * Boolean true/false
         * Int 整型
         * Double 小数
         * List 集合
         * Set 无重复的元素集合
         * Map 键值对集合
         *
         * Int --> java int
         * Float --> java float
         *
         */

        //val 只读变量 不能被修改了  = final
        val info : String = "MyInfo"
        //Explicitly given type is redundant here 显式给定的类型在这里是多余的
        //val info = "MyInfo"
        //info = "AA"
        println(info)

        val age : Int = 99
        println(age)

        var a = 1

        //Modifier 'const' is not applicable to 'local variable'
        //提示：修饰符“const”不适用于“局部变量”
        //const val PI = 45

        val aa = 45 //这个是  只读类型的变量
    }

    //Const 'val' are only allowed on top level or in objects
    //刚在类中还是还是会提示：Const 'val'只允许在顶层或object中使用
    //const val PI = 3.1415
}

const val PI = 3.1415 //定义编译时常量
//编译时常量只能是常用的基本数据类型：（String\Double\Int|Float\Long\Short\Byte\Char\Boolean）
//编译时常量只能在函数之外定义，为什么？如果在函数内定义，就必须在运行时才能调用函数赋值，何来编译时一说。
//结论：编译时常量只能在函数之外定义，就可以在编译期间初始化了。

fun main2(){
    val num  = 148

    //range范围从哪里到哪里
    if(num in 10..59){
        println("不及格")
    }else if(num in 60..100){
        println("合格")
    }else if(num !in 0..100){
        println("分数不在0到00范围内")
    }

    //when表达式
    val week = 0
    //java 的if语句
    //kt 的if是表达式有返回值
    val info = when(week) {
        1 -> "星期一"
        else ->{
            "不管今天是星期几啊"
        }
    }
    println(info)

    val graden  = "黄石公园"
    val time = 6

    println("今天天气好啊，去玩$graden,玩了$time 小时")
    println("今天天气好啊，去玩${graden},玩了$time 小时")
}

//函数默认都是public的
//其实Kotlin的函数，更规范：先有输入，再有输出
private fun method01(age: Int, name: String): Int {
    println("你的姓名是：$name,你的年龄是：$age")
    return 200
}

//上面的kt函数转成java相当于
//private static final int method01(int age, String name){
//    return 200
//}

//默认参数的运用
private fun method02(age: Int = 26, name: String = "wangwu"): Int {
    println("你的姓名是：$name,你的年龄是：$age")
    return 200
}

fun main3(){
    //具名函数参数的运用
    method02(name = "222", age = 23)
}

private fun doWork(): Unit{
    return println()
}

private fun doWork2(){
    return println()
}

private fun doWork3(){
    println()
}
//以上三个函数都是一样的，理解Unit
//Java语言的void关键字（void是无参数返回值 忽略类型 ）但是他是关键字啊，不是类型，这很矛盾
//Unit 函数返回值不写 默认也有，
//Unit 代表无参数返回的 忽略类型 == Unit类型类


//Nothing 类型特点
private fun show(number: Int){
    when(number){
        //不是注释提示，会终止程序的
        -1 -> TODO("没有这种分数")
        in 0..59 -> println("分数不及格")
        in 60..100 -> println("及格")
    }
}


//反引号中函数的名特点
fun main11(){
    //第一种
    `登录功能2021年8月8号测试环境`("wlp", "888")

    //第二种情况：//in is 在kt中是关键字，但是在java中不是，使用反引号
//    KtBase21.`is()
//    KtBase21.`in()

    //第三种
    `87899`()
}

private fun `登录功能2021年8月8号测试环境`(name: String, pwd: String){
    println("模拟：用户名是：$name, 密码是：$pwd")
}

private fun `87899`(){
    //...应用加密复杂核心函数供第三方使用
}

//匿名函数
fun main31(){
    val len = "Derry".count()
    println(len)

    val len2 = "Derry".count(){
        it == 'r'
    }
//    val len2 = "Derry".count{
//        it == 'r'
//    }
    println(len2)
}

//隐式返回
fun main32(){
    //写一个函数

    //第一步对函数的输入输出声明
    val methodAction: () -> String

    //第二步
    methodAction = {
        val input = 9999
        "$input Derry" //相当于return
        //隐式函数不写return 最后一行就是返回值
    }

    //第三步 调用此函数
    println(methodAction())
}

//函数参数
fun main33(){
    //相当于对上面隐式返回的丰富
    val methodAction : (Int, Int, Int) -> String = { number1, number2, number3 ->
        val inout = 999
        "$inout Derry 参数： $number1, $number2, $number3"
    }

    //调用函数
    methodAction(1,2,3)

    //it 关键字
    val methodAction2 : (String) -> String = {
        "$it Derry" //it 就是默认的入参，一个参数的时候
    }

    //匿名函数的类型推断
    //常规 方法名后为冒号
    val method : () -> String = {"00"}

    //类型推断 返回为 String
    val method1 = {v1:Double, v2 :Float ->
        "v1:$v1, v2:$v2"
    }

    //匿名函数就是lambda
    val addMethod = {num1: Int, num2: Int ->
        "$num1 : $num2"
    }
    println(addMethod(1,2))

    //类型推断为Any类型
    val weekMethod = {num: Int ->
        when(num){
            1 -> "yi"
            2 -> "er"
            else -> -1
        }
    }
    println(weekMethod(2))

}

//定义 参数是函数 的函数
//在java只能用回调了
const val USER_DB = "Derry"
const val PWD_DB = "123456"

fun main(){
    loginAPI("abc", "123", {msg: String, code: Int ->
        println("用户名：$msg, 密码：$code")
    })

    loginAPI("abc", "123", responseResult = {msg: String, code: Int ->
        println("用户名：$msg, 密码：$code")
    })

    //最终的方式
    loginAPI("abc", "123") { msg: String, code: Int ->
        println("用户名：$msg, 密码：$code")
    }
}

inline fun loginAPI(user: String, pwd: String, responseResult:(String, Int) -> Unit){
    if(user == null || pwd == null){
        TODO("用户名或密码为空")
    }
    //做很多的经验 前端经验
    if(user.length > 3 && pwd.length > 3){
        if(webServieLoginApi(user, pwd)){
            responseResult("login success", 200)
        }else{
            responseResult("login fail", 200)
        }
    }else{
        TODO("用户名和密码不合格")
    }
}

//登录的api
fun webServieLoginApi(user:String, pwd:String): Boolean{
    return user== USER_DB && pwd== PWD_DB
}

//此函数有使用lamda作为参数，就需要声明成内联
//不使用内联，在调用时，会生成多个对象完成lamda的调用，会造成性能损耗
//使用内联，相当于C++ #define 宏定义 宏替换，会把代码低缓到调用处，没有任何的对象开辟。
//内联使用 inline，函数参数尽量使用内联！！！

//函数的参数是函数：函数的引用
fun main51() {
    //方式一
    loginAPI("abc", "123", ::methodResponseResult)
    //方式二
    val obj = :: methodResponseResult
    loginAPI("abc", "123", obj);
}

fun methodResponseResult(msg:String, code:Int){
    println("用户名：$msg, 密码：$code")
}


//函数类型作为函数的返回类型
fun main52(){
    show51("msg")

    val method = showMethod("abc")
    //返回函数
    method("adc", 123)
}

fun show51(info:String):Boolean{
    println("我的$info")
    return true
}

fun showMethod(info: String): (String, Int) -> String {
    println("打印$info")

    //返回一个函数，匿名函数
    return {name: String, age: Int ->
        "我是$name, $age"
    }
}

/**
//匿名函数 与 具名函数
    {

}

::

 */
