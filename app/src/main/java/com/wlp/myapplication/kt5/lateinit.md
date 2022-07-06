在Kotlin中定义变量或者属性都是需要初始化值的，并且其都是private的，但是有些时候对于变量或者属性只需要声明，  
但是不需要现在就初始化的，则kotlin提供了lateinit关键字来实现:

class Student {
    lateinit var name: String //√
    var address :String = "北京"
    lateinit val sex :String //lateinit不可以修饰 val

    lateinit var age: Int //x
    lateinit var sorce : Double //x
}

lateinit 对应使用var来声明属性
lateinit 不可以修饰原始数据类型（byte，char，short ,int，long，float，double）

也许有人会问到为什么不可以修饰原始数据类型？
对于对象类型，Kotlin使用 null 值，以标记 lateinit 属性尚未初始化，并在访问该属性时引发适当的异常。
对于原始类型，没有这样的值，因此无法将属性标记为未初始化并提供的诊断信息lateinit 需要提供。
因此，仅对象类型的属性支持 lateinit。

最后再提一下Kotlin lateinit 和 by lazy 的区别：
1.lazy{} 只能用在val类型, lateinit 只能用在var类型 如 ：
val name: String by lazy { “sherlbon” }
lateinit var adapter: MyAdapter12

2.lateinit不能用在可空的属性上和java的基本类型上 如：
lateinit var age: Int //会报错

3.lateinit可以在任何位置初始化并且可以初始化多次。  
而lazy在第一次被调用时就被初始化，想要被改变只能重新定义

4.lateinit 有支持（反向）域（Backing Fields）

