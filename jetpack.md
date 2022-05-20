4、Android Jetpack应用指南
ViewModel 4.18
DataBinding 4.19
LiveData 4.19
mvvm、mvp 4.20

LifeCycle 5.6
Room 5.6
Navigation
WorkManager 处理后台任务
# kotlin ☆☆
Paging
    使用kotlin
# composeUI ☆
    与flutter高度相似，使用kotlin语言

# dataStore DataStore 使用kotlin, 有两种实现方式：Preferences 和 Proto，从中选择其一即可
                      使用上跟SharedPreference很类似，也很简单，Android官方推荐使用
multidex ×
    超过最大方法数限制的问题，分包。
dagger2 ☆
    依赖注入
hilt
startup
viewpager2
flutter

IOC
APT
反射及应用场景
DataStore 与 MMKV
泛型

5、java和kotlin语言；
多线程开发；
mvvm+jetpack+kt协程框架；
room；
Flutter跨平台。
性能、内存调优工作。
自定义UI组件、图像动画、数据库、greenDao；
rxjava,okhttp,retrofit,databinding,lambada，gradle脚本；
具备C++开发经验，JNI开发；
具备扎实的数据结构和算法基础；
熟练掌握设计模式。

-Kotlinx Coroutines-Kotlin协程

-Retrofit-网络请求框架封装
-Glide-图片加载
-OkHttp-网络请求
-Gson-Gson 解析
#  -Glide Transformations-图像转换
#  -FlycoTabLayout-TabLayout封装
#  -SmartRefreshLayout-下拉刷新框架
#  -PhotoView-支持手势缩放图片

6、Flutter
（1）难点、亮点：自定义组件、绘制、事件、动画
（2）基础：dart、flutter基础组件
（3）架构：第三方框架、网络、数据库、图片缓存、工具类
性能优化调优
（4）flutter与原生开发
C/C++
熟悉Flutter:StatelessWidget，StatefulWidget以及常用基础组件。
熟悉Flutter:redux，provider，fish_redux任一框架。
熟悉dart语言开发，了解isolate机制，事件驱动模型。
熟悉Dart编程语言，熟悉Flutter控件相关API及第三方框架，能够使用Flutter进行跨平台的移动端开发。
（5）特殊：
蓝牙、穿戴设备
（6）项目管理
数据结构算法

UnPeek-LiveData
https://github.com/KunMinX/UnPeek-LiveData
About LiveData 数据倒灌：别问，问就是不可预期 - Perfect alternative to SingleLiveEvent, supporting multiple  
observers.

KunMinX 目录
https://kunminx.gitbook.io/relearn-android/new_moments
ViewModel
	State-ViewModel 托管和恢复状态
	SavedStated
LiveData
    UnPeek-LiveData
	解决“数据倒灌”问题
DataBinding
	LiveData 和 ObservableField的本质区别
	BindingAdapter
Lifecycle
	ContentProvider
声明式UI
    compose UI
Navigation SmoothNavigation
	Add hide replace
	navigation 解决的问题，使用，原理
MVVM\MVP架构设计
	JetpackMvvm脚手架

事件分发、滑动冲突

databinding 列表及多列表页面

# 难点技术、核心模块和核心技术
性能、性能监控、稳定性和性能优化
静态代码分析
代码混淆、防破解
数据加密
打包编译、插件化、热修复
插件化实践
内存泄漏排查
闪退、ANR排查
网络优化、restful接口
NDK

# 架构设计
# 公共库、框架、工具类
# UI动画、自定义View、事件分发
android framework、底层库
# kotlin
# 跨平台Flutter、混合开发

开发流程、规范、提高团队效率
自动化测试、测试架构
项目管理、团队管理

# 蓝牙：BLE通信流程、收发数据、拆包组包、大数据包发收。
物联网经验
开源库源码

TCP、HTTP、Socket、WebSocket协议
数据结构
算法
设计模式

Android开发不仅仅限于应用开发，IoT、音视频、边缘计算、VR/AR，很多新的技术浪潮其实都可以融入到移动开发的体系里。往深度发展的话，可以结合具体的业务进行学习提升，比如说音视频、物联网这种5G时代的风口技术，努力。

调优专题

gradle实战

边缘计算与边缘人工智能。

1. 提升通用技术能力
半衰期长的技术要打好基础，也就是更加通用的技术。
一来通用技术如数据结构和算法、计算机网络、操作系统、计算机系统结构等，对技术的深层理解有很大的帮助，二来这类技术在不同的语言和技术方向上切换时是共通的。

2. 提升软实力
沟通、文档能力。
软实力实际上也是一项通用的技能，甚至比第一条更加通用。
良好的沟通可以缩短开会的时长、节省相互之间的沟通成本，
也使得团队合作更加愉快。不错的文档表达能力可以省去不少的重复沟通。

3. 选择一个细分领域不断深入
插件化技术、响应式编程、组件化框架、系统架构等进阶技术，承托着业务之下的基础框架，能够使得开发者在面对需求和功能变化的时候有更快的反应和更优雅的行动。
业务逻辑日渐成熟的形势下，用户体验越来越重要，突然的软件崩溃或是加载图标持续5秒，对于高质量应用都是阻碍。
渲染速度、网络请求体验、I/O优化、热修复技术、耗电优化，都是性能优化需要重视的点。
Android现在细分的领域非常多，逆向安全、音视频、物联网、SDK开发等等，可以在这些领域中选择一个并不断深入。若是从时代背景的角度考虑，当下处于信息时代，用户接收和喜爱信息传播的形式一路从文字、图片、音频、视频，到了如今的直播。音视频会是一个不错的选择。其中会涉及到音视频编解码和音视频同步处理、特效处理、合成等等技术，对于Android 底层源码的理解也十分有帮助。