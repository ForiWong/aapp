Lifecycle:使用生命周期感知型组件处理生命周期

生命周期感知型组件可执行操作来响应另外一个组件（如Activity和Fragment）的生命周期状态的变化。这些组件有助于您写出更有条理且  
往往更精简的代码，这样的代码更易于维护；

## 问题：为啥框架中使用lifecycleviewmodel没有执行ondestroy ，各种交叉引用导致没有回收吗？

官网：LiveData是一个可观察的数据持有者类。与常规observable不同，LiveData是生命周期感知的。
可观察、生命周期感知。简单来说，Google给我们提供了一个可以被观察的，并且拥有生命周期感知能力的类。那有什么用呢？

Lifecycle

核心抽象类。架构的核心，继承该类的子类，表示本身是一个 具有Android生命周期特性的对象，能对外提供如下能力：

1. Android生命周期 事件的监听

2. Android生命周期 状态的获取

抽象类内部，定义了2个用于生命周期相关的枚举类：

1. enum Event： 生命周期 事件

2. enum State： 生命周期 状态

ViewModel是存储UI相关数据并不会因为旋转而销毁的类。
最为重要的就是ViewModel具有下面的生命周期，这就是ViewModel的最可贵之处：

正因为ViewModel有如此的生命周期，所以ViewModel在MVVM可以作为数据存储区，是连接View和Model重要组件，ViewModel的核心作用如下图所示：

