###mvvm java
# 1、MVVMHabit-master，6.5k， java
MVVMHabit的Repository的模型问题，万一业务模块多的话，怎么将他分模块的问题
细读基于谷歌最新AAC架构，MVVM设计模式的一套快速开发库，整合Okhttp+RxJava+Retrofit+Glide等主流模块，满足日常开发需求。使用该框架可以快速开发一个高质量、易维护的Android应用。

# 2、KunMinX/Jetpack-MVVM-Best-Practice 6.3k,Java
是 难得一见 的 Jetpack MVVM 最佳实践！在 以简驭繁 的代码中，对 视图控制器 乃至 标准化开发模式 形成正确、深入的理解！

# 3、android-MvvmComponent-App-master细读 Java 1.5k
Android 组件化开源app -开眼短视频(OpenEyes)

4、CloudReader，4.5k，云阅 java
一款基于网易云音乐UI，使用玩Android、Gank.Io Api，MVVM-DataBinding架构开发的符合Google Material Design的Android客户端

###mvvm kotlin
1、https://github.com/ahmedeltaher/MVVM-Kotlin-Android-Architecture 2k √√  先看
MVVM + Kotlin + Retrofit2 + Hilt + Coroutines + Kotlin Flow + mockK + Espresso + Junit5
Hilt:  Hilt 是 Android 的依赖项注入库，可减少在项目中执行手动依赖项注入的样板代码。
       Hilt就是对dagger2的进一步封装.
Coroutines: 协程
Kotlin Flow： 用于流式编程
mockK：kotlin单元测试利器
Espresso：自动化测试框架

2、https://github.com/skydoves/Pokedex 5.3k  先看
Pokedex demonstrates modern Android development with Hilt, Material Motion, Coroutines, Flow,  
Jetpack (Room, ViewModel) based on MVVM architecture.

3、https://github.com/hegaojian/JetpackMvvm 2.2k
一个Jetpack结合MVVM的快速开发框架，基于MVVM模式集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle、  
Navigation组件 使用Kotlin语言，添加大量拓展函数，简化代码 加入Retrofit网络请求,协程，帮你简化各种操作，让你快速  
开发项目

4、https://github.com/igorwojda/android-showcase 5.5k
Android application following best practices: Kotlin, Coroutines, JetPack, Clean Architecture, Feature Modules,  
Tests, MVVM, DI, Static Analysis

5、https://github.com/TanJiaJunBeyond/AndroidGenericFramework ×× 不看

6、https://github.com/VIPyinzhiwei/Eyepetizer 1.5k 可以看看
基于 Kotlin 语言仿写「开眼 Eyepetizer」的一个短视频 Android 客户端项目，采用 Jetpack + 协程实现的 MVVM 架构。

7、PaoNet泡网

//repository
https://github.com/skydoves/DisneyMotions √√
A Disney app using transformation motions based on MVVM (ViewModel, Coroutines, Flow, Room, Repository, Koin) architecture.
koin 依赖注入框架

https://github.com/skydoves/MarvelHeroes √√ 和上面那个框架是一样的
A sample Marvel heroes application based on MVVM (ViewModel, Coroutines, Room, Repository, Koin) architecture.

https://github.com/MindorksOpenSource/android-mvvm-architecture  ×××，java语言
This repository contains a detailed sample app that implements MVVM architecture using Dagger2, Room, RxJava2, FastAndroidNetworking and PlaceholderView

https://github.com/MindorksOpenSource/MVVM-Architecture-Android-Beginners  ××× 很简单的
This repository contains a sample app that implements MVVM architecture using Kotlin, ViewModel, LiveData, and etc.

https://github.com/hi-dhl/PokemonGo  pokemonGo √√√
神奇宝贝 (PokemonGo) 基于 Jetpack + MVVM + Repository + Paging3 + Kotlin Flow 的实战项目
这是一个小型的 App 项目，涉及到技术：Paging3（network + db），Dagger-Hilt，App Startup，DataBinding，Room，Motionlayout，
Kotlin Flow，Coil，JProgressView 等等。

https://github.com/hi-dhl/AndroidX-Jetpack-Practice
一个很好的实践项目，各个专题有技术文章可以看。
本仓库致力于建立最全、最新的的 AndroidX Jetpack 相关组件的实践项目 以及组件对应的分析文章（持续更新中）如果对你有帮助，请在右上角 
star 一下，感谢

https://github.com/ibrahimsn98/web-dev-tools-android   ××× 先不看
Sample Android Application - MVVM, Clean Architecture, Modularization, Repository Pattern

https://github.com/jenly1314/MVVMFrame  ××× 先不看，Java语言
MVVMFrame for Android 是一个基于Google官方推出的Architecture Components dependencies（现在叫JetPack）
{ Lifecycle，LiveData，ViewModel，Room } 构建的快速开发框架。有了MVVMFrame的加持，从此构建一个MVVM模
式的项目变得快捷简单。

B站看相关课程
###=====框架相关模块
MVVM DataBinding
协程 flow
Retrofit2
Room
Hilt vs koin 依赖注入
rxlife

绑定
binding RecyckerViewBinding
		ViewBinding

model
	data
	local dao/room

dto
data class
数据类

error

moshi

utils

base
 baseActivity
 baseFragment
 BaseViewModel
 ViewModelFactory

Retrofit
