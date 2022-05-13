//https://www.runoob.com/kotlin.html 菜鸟教程
package com.wlp.myapplication.kt//可选的包头
//Kotlin 程序文件以 .kt 结尾，如：Hello.kt。

/**
 * Created by wlp on 2022/5/12
 * Description: 基础语法、基本数据类型
 */
fun main(args: ArrayList<String>) {//包级别可见的函数
    println("hello wd")//语句结尾的;可省略

    Hell("Word!").greet()//创建一个对象，不用new关键字
}

class Hell(val name: String) {
    fun greet() {
        println("Hello, $name")
    }
}

//函数定义
fun sum(a: Int, b: Int): Int {//Int 参数， 返回值 Int
    return a + b;
}

fun sum1(a: Int, b: Int) = a + b;//表达式作为函数体，返回类型自行推断

public fun sum2(a: Int, b: Int): Int = a + b //public 方法则必须明确写出返回类型

//无返回值的函数，类似于java中的void
fun printSum(a: Int, b: Int): Unit {
    println(a + b)
}

//如果是返回Unit类型，则可以省略
fun printSum2(a: Int, b: Int) {
    println(a + b)
}

//函数的变长参数可以用vararg关键字进行标识
fun vars(vararg v: Int) {
    for (vt in v) {
        println(vt)
    }
}

//测试
fun main(args: Array<String>) {
    vars(1, 2, 3, 5)
}

//lambda匿名函数
fun main5(args: Array<String>) {
    val sumLambda: (Int, Int) -> Int = { x, y -> x + y }
    println(sumLambda(1, 2))
}

//可变变量定义 var 关键字
//var <标识符> : <类型> = <初始化值>

//不可变变变量定义 val 关键字，只能赋值一次的变量，类似于java的final
//val <标识符> : <类型> = <初始化值>

//常量与变量都可以没有初始值，但是在引用前必须初始化
//编译器支持自动化类型判断，即声明时可以不指定类型，由编译器判断。
val a: Int = 1
val b = 1
//val c: Int
//c = 1

class Num() {
    var x = 5
//    x += 1
}

//字符串模板
//$ 表示一个变量名或者变量值
//$varName 表示变量值
//${varName.fun()}表示变量的方法返回值

var aa = 1
val s1 = "a is $aa"

var aaa = 2
val s2 = "${s1.replace("is", "was")}, but now is $aa"

//NULL 检查机制
//类型后面加?表示可为空
var age: String? = "23"

//抛出空指针异常
val ages = age!!.toInt()

//不做处理返回null
val ages1 = age?.toInt()

//age 为空返回-1
val ages2 = age?.toInt() ?: -1

fun parseInt(str: String): Int? {
    return str.toIntOrNull()
}

fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)

    // 直接使用 `x * y` 会导致错误, 因为它们可能为 null
    if (x != null && y != null) {
        // 在进行过 null 值检查之后, x 和 y 的类型会被自动转换为非 null 变量
        println(x * y)
    } else {
        println("'$arg1' or '$arg2' is not a number")
    }
}

fun main() {
    printProduct("6", "7")
    printProduct("a", "7")
    printProduct("a", "b")
}

fun printProduct2(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)

    // ...
    if (x == null) {
        println("Wrong number format in arg1: '$arg1'")
        return
    }
    if (y == null) {
        println("Wrong number format in arg2: '$arg2'")
        return
    }

    // 在进行过 null 值检查之后, x 和 y 的类型会被自动转换为非 null 变量
    println(x * y)
}

//类型检测及自动类型转换
//我们可以使用 is 运算符检测一个表达式是否某类型的一个实例(类似于Java中的instanceof关键字)。
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // 做过类型判断以后，obj会被系统自动转换为String类型
        return obj.length
    }

    //在这里还有一种方法，与Java中instanceof不同，使用!is
    // if (obj !is String){
    //   // XXX
    // }

    // 这里的obj仍然是Any类型的引用
    return null
}

fun getStringLength2(obj: Any): Int? {
    if (obj !is String)
        return null
    // 在这个分支中, `obj` 的类型会被自动转换为 `String`
    return obj.length
}

fun getStringLength3(obj: Any): Int? {
    // 在 `&&` 运算符的右侧, `obj` 的类型会被自动转换为 `String`
    if (obj is String && obj.length > 0)
        return obj.length
    return null
}

/**
 * 区间表达式由具有操作符形式 .. 的 rangeTo 函数辅以 in 和 !in 形成。
 * 区间是为任何可比较类型定义的，但对于整型原生类型，它有一个优化的实现。以下是使用区间的一些示例:
 */
fun rang() {
    for (i in 1..4) print(i) // 输出“1234”

    for (i in 4..1) print(i) // 什么都不输出

    var i = 0
    if (i in 1..10) { // 等同于 1 <= i && i <= 10
        println(i)
    }

    // 使用 step 指定步长
    for (i in 1..4 step 2) print(i) // 输出“13”

    for (i in 4 downTo 1 step 2) print(i) // 输出“42”

    // 使用 until 函数排除结束元素
    for (i in 1 until 10) {   // i in [1, 10) 排除了 10
        println(i)
    }
}

fun main10(args: Array<String>) {
    print("循环输出：")
    for (i in 1..4) print(i) // 输出“1234”
    println("\n----------------")
    print("设置步长：")
    for (i in 1..4 step 2) print(i) // 输出“13”
    println("\n----------------")
    print("使用 downTo：")
    for (i in 4 downTo 1 step 2) print(i) // 输出“42”
    println("\n----------------")
    print("使用 until：")
    // 使用 until 函数排除结束元素
    for (i in 1 until 4) {   // i in [1, 4) 排除了 4
        print(i)
    }
    println("\n----------------")
}

// Kotlin 的基本数值类型包括 Byte、Short、Int、Long、Float、Double 等。
// 不同于 Java 的是，字符不属于数值类型，是一个独立的数据类型。
/**
 * 字面常量
 * 下面是所有类型的字面常量：
 * 十进制：123
 * 长整型以大写的 L 结尾：123L
 * 16 进制以 0x 开头：0x0F
 * 2 进制以 0b 开头：0b00001011
 * 注意：8进制不支持
 * Kotlin 同时也支持传统符号表示的浮点数值：
 *
 * Doubles 默认写法: 123.5, 123.5e10
 * Floats 使用 f 或者 F 后缀：123.5f
 * 你可以使用下划线使数字常量更易读：
 */
val oneMillion = 1_000_000
val creditCardNumber = 1234_5678_9012_3456L //这个真挺好的
val socialSecurityNumber = 999_99_9999L
val hexBytes = 0xFF_EC_DE_5E
val bytes = 0b11010010_01101001_10010100_10010010

/**
 * 比较两个数字
 * Kotlin 中没有基础数据类型，只有封装的数字类型，你每定义的一个变量，其实 Kotlin 帮你封装了一个对象，这样可以保证不会
 * 出现空指针。数字类型也一样，所以在比较两个数字的时候，就有比较数据大小和比较两个对象是否相同的区别了。
 * 在 Kotlin 中，三个等号 === 表示比较对象地址，两个 == 表示比较两个值大小。
 */
fun compareValue(args: Array<String>) {
    val a: Int = 10000
    println(a === a) // true，值相等，对象地址相等

    //经过了装箱，创建了两个不同的对象
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a

    //虽然经过了装箱，但是值是相等的，都是10000
    println(boxedA === anotherBoxedA) //  false，值相等，对象地址不一样
    println(boxedA == anotherBoxedA) // true，值相等
}

/**
 * 类型转换
 * 由于不同的表示方式，较小类型并不是较大类型的子类型，较小的类型不能隐式转换为较大的类型。 这意味着在
 * 不进行显式转换的情况下我们不能把 Byte 型值赋给一个 Int 变量。
 * 每种数据类型都有下面的这些方法，可以转化为其它的类型：
 * toByte(): Byte
 * toShort(): Short
 * toInt(): Int
 * toLong(): Long
 * toFloat(): Float
 * toDouble(): Double
 * toChar(): Char
 * 有些情况下也是可以使用自动类型转化的，前提是可以根据上下文环境推断出正确的数据类型而且数学操作符会做相应的重载。
 * 例如下面是正确的：
 */
val w: Byte = 1 // OK, 字面值是静态检测的

//val v: Int = w // 错误
//我们可以代用其toInt()方法。
val i: Int = b.toInt() // OK

val l = 1L + 3 // Long + Int => Long

/**
位操作符
对于Int和Long类型，还有一系列的位操作符可以使用，分别是：
shl(bits) – 左移位 (Java’s <<)
shr(bits) – 右移位 (Java’s >>)
ushr(bits) – 无符号右移位 (Java’s >>>)
and(bits) – 与
or(bits) – 或
xor(bits) – 异或
inv() – 反向
字符
和 Java 不一样，Kotlin 中的 Char 不能直接和数字操作，Char 必需是单引号 ' 包含起来的。比如普通字符 '0'，'a'。

字符字面值用单引号括起来: '1'。 特殊字符可以用反斜杠转义。
支持这几个转义序列：\t、 \b、\n、\r、\'、\"、\\ 和 \$。 编码其他字符要用 Unicode 转义序列语法：'\uFF00'。

我们可以显式把字符转换为 Int 数字：
 */
fun decimalDigitValue(c: Char): Int {
    if (c !in '0'..'9')
        throw IllegalArgumentException("Out of range")
    return c.toInt() - '0'.toInt() // 显式转换为数字
}

//当需要可空引用时，像数字、字符会被装箱。装箱操作不会保留同一性。

/**
布尔
布尔用 Boolean 类型表示，它有两个值：true 和 false。

若需要可空引用布尔会被装箱。

内置的布尔运算有：

|| – 短路逻辑或
&& – 短路逻辑与
! - 逻辑非
 */

// 数组
// 数组用类 Array 实现，并且还有一个 size 属性及 get 和 set 方法，由于使用 [] 重载了 get 和 set 方法，所以我们可以通过
// 下标很方便的获取或者设置数组对应位置的值。
//  数组的创建两种方式：一种是使用函数arrayOf()；另外一种是使用工厂函数。如下所示，我们分别是两种方式创建了两个数组：
fun makeArray(args: Array<String>) {
    //[1,2,3]
    val a = arrayOf(1, 2, 3)
    //[0,2,4]
    val b = Array(3, { i -> (i * 2) })

    //读取数组内容
    println(a[0])    // 输出结果：1
    println(b[1])    // 输出结果：2
}

//如上所述，[] 运算符代表调用成员函数 get() 和 set()。
//注意: 与 Java 不同的是，Kotlin 中数组是不协变的（invariant）。
//除了类Array，还有ByteArray, ShortArray, IntArray，用来表示各个类型的数组，省去了装箱操作，因此效率更高，其用法同Array一样：

//val x: IntArray = intArrayOf(1, 2, 3)
//x[0] = x[1] + x[2]

/**
字符串
和 Java 一样，String 是不可变的。方括号 [] 语法可以很方便的获取字符串中的某个字符，也可以通过 for 循环来遍历：

for (c in str) {
println(c)
}
Kotlin 支持三个引号 """ 扩起来的字符串，支持多行字符串，比如：
 */
fun main22(args: Array<String>) {
    val text = """
多行字符串
多行字符串
"""
    println(text)   // 输出有一些前置空格
}

//String 可以通过 trimMargin() 方法来删除多余的空白。

fun main23(args: Array<String>) {
    val text = """
|多行字符串
|菜鸟教程
|多行字符串
|Runoob
""".trimMargin()

    println(text)    // 前置空格删除了
}

//默认 | 用作边界前缀，但你可以选择其他字符并作为参数传入，比如 trimMargin(">")。

//字符串模板
//字符串可以包含模板表达式 ，即一些小段代码，会求值并把结果合并到字符串中。 模板表达式以美元符（$）开头，由一个简单的名字构成:
fun main24(args: Array<String>) {
    val i = 10
    val s = "i = $i" // 求值结果为 "i = 10"
    println(s)
}

//或者用花括号扩起来的任意表达式:
fun main25(args: Array<String>) {
    val s = "runoob"
    val str = "$s.length is ${s.length}" // 求值结果为 "runoob.length is 6"
    println(str)
}

//原生字符串和转义字符串内部都支持模板。 如果你需要在原生字符串中表示字面值 $ 字符（它不支持反斜杠转义），
// 你可以用下列语法：

fun main26(args: Array<String>) {
    val price = """
${'$'}9.99
"""
    println(price)  // 求值结果为 $9.99
}
