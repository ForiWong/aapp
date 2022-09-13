https://zhuanlan.zhihu.com/p/26100298
Android系统架构开篇
android系統的5层架构
5层各自的介绍
通信方式
系统启动、核心服务、内核技术、runtime
调试技巧、异常原理

————————————————
版权声明：本文为CSDN博主「Zhang  Jun」的原创文章
原文链接：https://blog.csdn.net/johnWcheung/article/details/79983001
————————————————
Android系统一共分为5层：

Application(系统应用)
FrameWork（Java API 框架）
Native Libraries（原生 C/C++ 库 ）+Android Runtime（ART虚拟机+Core Libraries(Android核心库)）
硬件抽象层 (HAL)
Linux Kernel

其中Framework在第二层，他是把一些基本的的或者和设备打交道的服务抽象起来作API给Application应用调用。
这里是用Android中最底层的java代码。里面通过JNI调用c、c++（Library）来和设备打交道，最终调用到Linux内核。

Android Framework主要包含以下模块：

ViewSystem：丰富、可扩展的视图系统，可用以构建应用的 UI，包括列表、网格、文本框、按钮甚至可嵌入的网络浏览器
ResoureManager：资源管理器，用于访问非代码资源，例如本地化的字符串、图形和布局文件
NotificationManager：通知管理器，可让所有应用在状态栏中显示自定义提醒
ActivityManager（AMS【ActivityManagerService】的代理）：Activity管理器，用于管理应用的生命周期，提供常见的导航返回栈
ComtentProvider：内容提供程序，可让应用访问其他应用（例如“联系人”应用）中的数据或者共享其自己的数据
WindowManager：它是手机屏幕的的管理者，管理着屏幕的详细情况，所有对屏幕的操作最终都是通过它，控制着屏幕的显示、隐藏和
层次处理.
PackageManger：管理者，包信息的管理
TelephoneyManager：通信管理者
LocationManager：位置管理者
XmppManager：推送管理者

FrameWork三大核心：
View.java 负责布局的排列，绘制，测量和事件分发，按键事件。
ActivityManagerService.java 管理所有应用程序的Activity等
WindowManagerService.java 给所有应用程序分配窗口，并管理这些窗口。

问题来了，FrameWork的层为什么不直接用C/C++写呢？

C/C++过于底层，开发者需要花费比较多时间去弄清楚，比如C/C++的内存机制，稍微不注意就忘记释放内存。而java虚拟机的GC会自动
处理这些问题。开发者可以省去这些时间，专注于自己的业务。

————————————————————————————————
掉帧监控，函数插桩，慢函数检测，ANR 监控，启动监控，都需要对 Framework 有比较深入的了解，才能知道怎么去监控，
利用什么机制监控，函数插桩插到哪里，反射调用该反射哪个类哪个方法哪个属性……

linux内存管理
Binder四层源码分析，Binder机制，Binder进程通信
Handler消息通信
Zygote进程
增量更新项目
AMS
PMS
WMS
Activity、Fragment、Service解析

《Android系统源代码情景分析》，罗升阳。

《深入理解Android》系列书籍，I、II，邓凡平老师写的系列。
《深入理解Android卷III》，作者: 张大伟。

《Android框架揭秘》，底层架构的一本好书，作者: (韩)金泰延 / 宋亨周 / 朴知勋 / 李白 / 林起永，424。

《Android系统级深入开发--移植和调试》，作者: 韩超//梁泉

深入理解Android内核设计思想.上、下
作 者 :  林学森著
ISBN :  978-7-115-45263-4

rom的封装
手机ROM
手机、车机、智能电视
主要是linux kernel,native,framework等问题的分析定位解决以及开发，

————————————————————————————————
BSP全称 Board Support Package 介于主板硬件和操作系统中驱动层程序之间的一层，一般认为它属于操作系统一部分，主要是实现对
操作系统的支持，为上层的驱动程序提供访问硬件设备寄存器的函数包，使之能够更好的运行于硬件主板以前的Windows Mobile及现在  
的Android都有BSP。均要进行驱动层的开发，以使操作系统能正常调用主板所有功能。

一般的，可能会有存储驱动，显示驱动，蓝牙驱动，Wifi驱动，通讯模块驱动，keypad驱动，电源管理驱动等。

BSP是板级支持包，是介于主板硬件和操作系统之间的一层，应该说是属于操作系统的一部分,主要目的是为了支持操作系统，使之能够更好  
的运行于硬件主板。BSP是相对于操作系统而言的，不同的操作系统对应于不同定义形式的BSP,例如VxWorks的BSP和Linux的BSP相对于某一  
CPU来说尽管实现的功能一样，可是写法和接口定义是完全不同的，所以写BSP一定要按照该系统BSP的定义形式来写（BSP的编程过程大多数  
是在某一个成型的BSP模板上进行修改）。这样才能与上层OS保持正确的接口，良好的支持上层OS。

Board Support Package, 板级支持包，你可以理解为移植必须做的事情。  
BSP里面包含驱动，一般做移植都需要修改里面的驱动和配置。

（porting 移植）

从linux驱动转行至Android驱动开发大半年了，一开始就产生了一个很纠结目标和问题，就是不停的google如何porting android！  
这个问题得到的结果对于初出茅庐的我，感到迷惘。随着工作的需要，自己的经验也就慢慢的增加，这些迷雾也慢慢解开，这里要  
多谢我自己的努力和老大高工的精心栽培。

言归正传，将android移植到特定硬件平台上，其核心是bsp的搭建工作，bsp是板级支持包，并不是特定某个文件，而是从功能上理解  
的一种硬件适配软件包，它的核心就是：

1. linux内核硬件相关部分(主要是linux device drivers);

2. android用户空间的硬件抽象层。(HAL，hardware abstract layer).

linux驱动程序工作在内核空间，android的HAL工作在用户空间，有了这两个部分的结合，就可以让庞大的android系统运行在特定的  
硬件平台上。在具有了特定的硬件平台之后，为了适应不同版本的android系统，其BSP部分代码通常需要重写或者修改，此时设备驱  
动程序可以重用，硬件抽象层则需要修改。
BSP工作和核心应该是原始的硬件底层。例如蓝牙耳机，蓝牙传输文件，蓝牙聊天等程序最终依赖的硬件是蓝牙；照相机、摄像机、条形  
码识别器等程序都依赖于底层的摄像头；自动转屏，晃动屏幕控制的各种游戏、都同样依赖于加速度传感器。

目前一般的处理器或者硬件平台的BSP(board support package)都是由芯片厂商统一完成的，并且已经趋于成熟。因此开发者的主要工作  
不再是构建完整的BSP，而是调试和修改现有的BSP。其实每个芯片厂家都会有一个硬件平台的参考设计，如PMU，EMMC,WIFI，CODEC，CTP等。  
如果没有太大的改动，原厂的BSP一般都是可以跑起来的，针对某一块的硬件变化修改驱动和HAL就可以了，对于新增加的硬件，编写相关的  
驱动程序，然后提供给JAVA的本地框架层的接口就可以了。对于一些简单的设备驱动，可以不用写HAL的代码，实际上很多时候也不用去写，  
一种常见的情况是由JNI的部分代码直接调用驱动程序的设备节点或者使用sys文件系统。也可以直接把/sys/的属性文件(可以通过cat和echo  
读写)的文件接口直接提供给java层代码调用。

android的主要驱动有：

1. 显示驱动 display driver：常用于基于linux的帧缓冲frame buffer 驱动程序。

2. flash内存驱动flash memory driver :基于MTD的flash驱动程序。

3. 照相机驱动camera driver :基于linux的v4l video for linux驱动。

4. 音频驱动 audio driver ：基于ALSA advanced linux sound architechure驱动。

5. wifi驱动：基于IEEE801.31标准的驱动程序。

6. 键盘驱动keyboard driver：作为输入设备的键盘驱动。

7. 蓝牙驱动 bluetooth diver :基于IEEE801.35.1标准的无限传输技术。

8. binder IPC驱动：android一个特殊的驱动程序，具有单独的设备节点，提供进程间通信的功能。

9. power management能源管理：管理电池电量等信息。

android**主要的库**有：

1. C库，基于linux系统调用实现的库，C语言标准库，也是系统最底层的库。

2. 多媒体框架 media framwork

3. SGL:2D图像引擎

4. SSL secure socket layer:为数据通信提供安全支持

5. open GL:对3D提供支持

6. 界面管理工具 surface management

7. SQLite：一种通用的嵌入式数据库

8. Webkit：网络浏览器的核心

9. free type位图和矢量字体的功能

android主要framework有：

1. active 活动

2. boardcast Intent Receiver广播意图接收者

3. content Provider 内容提供者

4. Intent and Intent filter 意图和意图过滤器

android的application framework是为APP开发提供的API，实际上是一个应用程序框架，有谷歌规定好的API，JAVA开发人员直接使用。  
这一层提供的首先包涵UI程序所需要的控件，如View(视图控件)，其中要包涵list,grid,text,box,button等，甚至一个嵌入式的web浏览器。

硬件术语bring up的意思是“使......启动、调试、唤醒、让......跑起来”。
硬件术语bring up：
1、bring up作为计算机专业术语的解释如下：cause to load(an operating system) and start the initial processes。  
因此它的意思是“使......启动、调试、唤醒、让......跑起来”。
bring up：加载启动、调试。

Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的镜像中，然后发布到任何流行的  
Linux或Windows操作系统的机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口。

