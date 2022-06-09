基础语法
基本数据类型
条件控制
循环控制
类和对象、接口
继承
扩展
数据类和密封类
泛型
枚举类
对象表达式/声明
委托

（1）kotlin跳转到kotlin，或者kotlin跳转到java
btn_interface.setOnClickListener {
    var intent = Intent()
    intent.setClass(this, MainActivity::class.java)
    startActivity(intent)
}

（2）java跳转到kotlin中，和java跳转到java中是一样的
Intent intent = new Intent(this, MainActivity.class);
startActivity(intent);

//交叉使用java、kotlin

