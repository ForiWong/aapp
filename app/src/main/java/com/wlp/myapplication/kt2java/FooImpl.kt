package com.wlp.myapplication.kt2java

//kotlin继承java接口
class FooImpl : IFoo {
    override fun foo(p: Int?) {
    }

    override fun test(a: String?, i: Int): Int {
        return 12
    }

    override fun convert(o: Any?): String {
        TODO("Not yet implemented")
        return o.toString()
    }

}