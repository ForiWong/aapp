总结：
1、协程是什么？
官方文档，轻量级的线程。
协程就是提供的一套线程封装的API，使得我们在写代码时，不需要关注多线程就可以轻松写出并发操作。
可以很方便使用，同步的代码写法实现异步操作。
比如，两个请求，拿到结果后更新UI。

launch 函数加上实现在 {} 中具体的逻辑，就构成了一个协程。
launch {
…….
}
这种写法是kotlin的闭包语法糖。

coroutineScope.launch(Dispatchers.Main) {
...
withContext(Dispatchers.IO) {
...
}
...
withContext(Dispatchers.IO) {
...
}
...
}

一个方便使用的线程框架，协作式的例程。

2、创建协程的三种方法
3、suspend，挂起函数，非阻塞式挂起
协程等取消  CoroutineScope  CoroutineContext

----------------------------------------------------------------------------------------------------
调度器是协程上下文中众多元素中最重要的一个，通过CoroutineDispatcher定义，它控制了协程以何种策略分配到哪些线程上运行。
这里介绍几种常见的调度器
Dispatcher.Default 默认调度器。它使用JVM的共享线程池，该调度器的最大并发度是CPU的核心数，默认为2

Dispatcher.Unconfined 非受限调度器，它不会将操作限制在任何线程上执行——在发起协程的线程上执行第一个挂起点之前的操作，
在挂起点恢复后由对应的挂起函数决定接下来在哪个线程上执行。

Dispathcer.IO IO调度器，他将阻塞的IO任务分流到一个共享的线程池中，使得不阻塞当前线程。该线程池大小为环境变量kotlinx.coroutines.io.parallelism的值，
默认是64或核心数的较大者。该调度器和Dispatchers.Default共享线程，因此使用withContext(Dispatchers.IO)创建新的协程不一定会导致线程的切换。

Dispathcer.Main 该调度器限制所有执行都在UI主线程，它是专门用于UI的，并且会随着平台的不同而不同
注意，由于上下文具有继承关系，因此启动子协程时不显式指定调度器时，子协程和父协程是使用相同调度器的。


//async 与 withContext()
如果想要并发请求，我们必须要用async，如果是顺序执行，我们则可以使用withContext
//并发执行的正确写法
val task1 = async(Dispatcher.io){ doSomeStuff}
val task2 = async(Dispatcher.io){ doSomeStuff}
task1.await()
task2.await()

//顺序执行的常规写法
val task1 = withContext(Dispatcher.io){ doSomeStuff}
val task2 = withContext(Dispatcher.io){ doSomeStuff}
task1.await()
task2.await()
//或者
val task1 = async(Dispatcher.io){ doSomeStuff}.await()
val task2 = async(Dispatcher.io){ doSomeStuff}.await()
val total=  task1+task2

async 和 launch 创建协程的区别：
相同点：它们都可以用来启动一个协程，返回的都是 Coroutine，我们这里不需要纠结具体是返回哪个类。
不同点：async 返回的 Coroutine 多实现了 Deferred 接口。
关于 Deferred 更深入的知识就不在这里过多阐述，它的意思就是延迟，也就是结果稍后才能拿到。
我们调用 Deferred.await() 就可以得到结果了。

----------------------------------------------------------------------------------------------------
「挂起」的本质
1、协程中「挂起」的对象到底是什么？挂起的对象是协程。
启动一个协程可以使用 launch 或者 async 函数，协程其实就是这两个函数中闭包的代码块。
launch ，async 或者其他函数创建的协程，在执行到某一个 suspend 函数的时候，这个协程会被「suspend」，也就是被挂起。

那此时又是从哪里挂起？从当前线程挂起。换句话说，就是这个协程从正在执行它的线程上脱离。
线程执行到了 suspend 函数这里的时候，就暂时不再执行剩余的协程代码，跳出协程的代码块。
当这个协程被挂起的时候，就是主线程 post 的这个 Runnable 提前结束，然后继续执行它界面刷新的任务。
关于线程，我们就看完了。

这个时候你可能会有一个疑问，那 launch 包裹的剩下代码怎么办？
线程的代码在到达 suspend 函数的时候被掐断，接下来协程会从这个 suspend 函数开始继续往下执行，不过是在指定的线程。
谁指定的？是 suspend 函数指定的，比如我们这个例子中，函数内部的 withContext 传入的 Dispatchers.IO 所指定的 IO 线程。

常用的 Dispatchers ，有以下三种：
Dispatchers.Main：Android 中的主线程
Dispatchers.IO：针对磁盘和网络 IO 进行了优化，适合 IO 密集型的任务，比如：读写文件，操作数据库以及网络请求
Dispatchers.Default：适合 CPU 密集型的任务，比如计算

回到我们的协程，它从 suspend 函数开始脱离启动它的线程，继续执行在 Dispatchers 所指定的 IO 线程。
紧接着在 suspend 函数执行完成之后，协程为我们做的最爽的事就来了：会自动帮我们把线程再切回来。

这个「切回来」是什么意思？
我们的协程原本是运行在主线程的，当代码遇到 suspend 函数的时候，发生线程切换，根据 Dispatchers 切换到了 IO 线程；
当这个函数执行完毕后，线程又切了回来，「切回来」也就是协程会帮我再 post 一个 Runnable，让我剩下的代码继续回到主线程去执行。

我们从线程和协程的两个角度都分析完成后，终于可以对协程的「挂起」suspend 做一个解释：
协程在执行到有 suspend 标记的函数的时候，会被 suspend 也就是被挂起，而所谓的被挂起，就是切个线程；
不过区别在于，挂起函数在执行完成之后，协程会重新切回它原先的线程。
##再简单来讲，在 Kotlin 中所谓的挂起，就是一个稍后会被自动切回来的线程调度操作。

2、怎么自定义 suspend 函数
（1）什么时候需要自定义 suspend 函数？
如果你的某个函数比较耗时，也就是要等的操作，那就把它写成 suspend 函数。这就是原则。
耗时操作一般分为两类：I/O 操作和 CPU 计算工作。比如文件的读写、网络交互、图片的模糊处理，都是耗时的，通通可以把它们写进 suspend 函数里。
另外这个「耗时」还有一种特殊情况，就是这件事本身做起来并不慢，但它需要等待，比如 5 秒钟之后再做这个操作。这种也是 suspend 函数的应用场景。

（2）具体该怎么写
给函数加上 suspend 关键字，然后在 withContext 把函数的内容包住就可以了。
提到用 withContext是因为它在挂起函数里功能最简单直接：把线程自动切走和切回。
当然并不是只有 withContext 这一个函数来辅助我们实现自定义的 suspend 函数。

----------------------------------------------------------------------------------------------------
协程就是也一个使用线程的框架。
非阻塞式挂起：协程可以使用看起来是阻塞的代码写出非阻塞式的操作。
协程的挂起本质是切线程，
本质还是使用线程，所以并没有说的协程是轻量级的线程。
----------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------

https://rengwuxian.com/kotlin-coroutines-1/
Kotlin 的协程是它非常特别的一块地方：宣扬它的人都在说协程多么好多么棒，但多数人不管是看了协程的官方文档还是一些网络文章之后又都觉得完全看不懂。
而且这个「不懂」和 RxJava 是属于一类的：由于协程在概念上对于 Java 开发者来说就是个新东西，所以对于大多数人来说，别说怎么用了，我连它是个什么
东西都没看明白。

所以今天，我就先从「协程是什么」说起。首先还是看视频。
https://www.bilibili.com/video/BV164411C7FK?zw

如果你看不到上面的哔哩哔哩视频，可以点击 这里 去 YouTube 看。

这期内容主要是讲一个概念：什么是协程。因为这个概念有点难（其实看完视频你会发现它超级简单），所以专门花一期来讲解。后面的内容会更劲爆，如果你喜欢我
的视频，别忘了去 原视频 点个赞投个币，以及关注订阅一下，不错过我的任何新视频！

码上开学 Kotlin 系列的文章，协程已经是第五期了，这里简单讲一下我们（扔物线和即刻 Android 团队）出品的 Kotlin 上手指南系列文章的一些考量：

官方文档有指定的格式，因为它是官方的，必须面面俱到，写作顺序不是由浅入深，不管你懂不懂，它都得讲。
网上的文章大都是从作者自身的角度出发，真正从读者的需求出发的少之又少，无法抓住读者的痛点，能够读完已属不易。

疲劳度是这一系列的一个重要的衡量指标，文章中如果连续出现大段代码，疲劳度会急剧上升，不容易集中精神，甚至中途放弃。
我们期许基于上述的考量和原则，把技术文章写得更加轻松易读，激发读者学习的兴趣，真正实现「上手」。

协程在 Kotlin 中是非常特别的一部分，和 Java 相比，它是一个新颖的概念。宣扬它的人都在说协程是多么好用，但就目前而言不管是官方文档还是网络上的一些
文章都让人难以读懂。

造成这种「不懂」的原因和大多数人在初学 RxJava 时所遇到的问题其实是一致的：对于 Java 开发者来说这是一个新东西。下面我们从「协程是什么」开始说起。

协程是什么
协程并不是 Kotlin 提出来的新概念，其他的一些编程语言，例如：Go、Python 等都可以在语言层面上实现协程，甚至是 Java，也可以通过使用扩展库来间接地
支持协程。 当在网上搜索协程时，我们会看到：

Kotlin 官方文档说「本质上，协程是轻量级的线程」。
很多博客提到「不需要从用户态切换到内核态」、「是协作式的」等等。
作为 Kotlin 协程的初学者，这些概念并不是那么容易让人理解。这些往往是作者根据自己的经验总结出来的，只看结果，而不管过程就不容易理解协程。

「协程 Coroutines」源自 Simula 和 Modula-2 语言，这个术语早在 1958 年就被 Melvin Edward Conway 发明并用于构建汇编程序，说明协程是
一种编程思想，并不局限于特定的语言。

Go 语言也有协程，叫 Goroutines，从英文拼写就知道它和 Coroutines 还是有些差别的（设计思想上是有关系的），否则 Kotlin 的协程完全可以叫 Koroutines 了。

因此，对一个新术语，我们需要知道什么是「标准」术语，什么是变种。

当我们讨论协程和线程的关系时，很容易陷入中文的误区，两者都有一个「程」字，就觉得有关系，其实就英文而言，Coroutines 和 Threads 就是两个概念。

从 Android 开发者的角度去理解它们的关系：

我们所有的代码都是跑在线程中的，而线程是跑在进程中的。
协程没有直接和操作系统关联，但它不是空中楼阁，它也是跑在线程中的，可以是单线程，也可以是多线程。
单线程中的协程总的执行时间并不会比不用协程少。
Android 系统上，如果在主线程进行网络请求，会抛出 NetworkOnMainThreadException，对于在主线程上的协程也不例外，这种场景使用协程还是要切线程的。
协程设计的初衷是为了解决并发问题，让 「协作式多任务」 实现起来更加方便。这里就先不展开「协作式多任务」的概念，等我们学会了怎么用再讲。

##协程就是提供等一套线程封装等API，使得我们在写代码时，不需要关注多线程就可以轻松写出并发操作。
视频里讲到，协程就是 Kotlin 提供的一套线程封装的 API，但并不是说协程就是为线程而生的。
不过，我们学习 Kotlin 中的协程，一开始确实可以从线程控制的角度来切入。因为在 Kotlin 中，协程的一个典型的使用场景就是线程控制。就像 Java 中的 Executor 
和 Android 中的 AsyncTask，Kotlin 中的协程也有对 Thread API 的封装，让我们可以在写代码时，不用关注多线程就能够很方便地写出并发操作。

在 Java 中要实现并发操作通常需要开启一个 Thread ：

☕️
new Thread(new Runnable() {
@Override
public void run() {
...
}
}).start();
Java
这里仅仅只是开启了一个新线程，至于它何时结束、执行结果怎么样，我们在主线程中是无法直接知道的。

Kotlin 中同样可以通过线程的方式去写：

🏝️
Thread({
...
}).start()
Kotlin
可以看到，和 Java 一样也摆脱不了直接使用 Thread 的那些困难和不方便：

线程什么时候执行结束
线程间的相互通信
多个线程的管理
我们可以用 Java 的 Executor 线程池来进行线程管理：

🏝️
val executor = Executors.newCachedThreadPool()
executor.execute({
...
})
Kotlin
用 Android 的 AsyncTask 来解决线程间通信：

🏝️
object : AsyncTask&lt;T0, T1, T2&gt; {
override fun doInBackground(vararg args: T0): String { ... }
override fun onProgressUpdate(vararg args: T1) { ... }
override fun onPostExecute(t3: T3) { ... }
}
Kotlin
AsyncTask 是 Android 对线程池 Executor 的封装，但它的缺点也很明显：

需要处理很多回调，如果业务多则容易陷入「回调地狱」。
硬是把业务拆分成了前台、中间更新、后台三个函数。
看到这里你很自然想到使用 RxJava 解决回调地狱，它确实可以很方便地解决上面的问题。

RxJava，准确来讲是 ReactiveX 在 Java 上的实现，是一种响应式程序框架，我们通过它提供的「Observable」的编程范式进行链式调用，可以很好地消除回调。

使用协程，同样可以像 Rx 那样有效地消除回调地狱，不过无论是设计理念，还是代码风格，两者是有很大区别的，协程在写法上和普通的顺序代码类似。

这里并不会比较 RxJava 和协程哪个好，或者讨论谁取代谁的问题，我这里只给出一个建议，你最好都去了解下，因为协程和 Rx 的设计思想本来就不同。
下面的例子是使用协程进行网络请求获取用户信息并显示到 UI 控件上：

🏝️
launch({
val user = api.getUser() // 👈 网络请求（IO 线程）
nameTv.text = user.name  // 👈 更新 UI（主线程）
})
Kotlin
这里只是展示了一个代码片段，launch 并不是一个顶层函数，它必须在一个对象中使用，我们之后再讲，这里只关心它内部业务逻辑的写法。

##launch 函数加上实现在 {} 中具体的逻辑，就构成了一个协程。
launch 函数加上实现在 {} 中具体的逻辑，就构成了一个协程。

通常我们做网络请求，要不就传一个 callback，要不就是在 IO 线程里进行阻塞式的同步调用，
而在这段代码中，上下两个语句分别工作在两个线程里，但写法上看起来和普通的单线程代码一样。

这里的 api.getUser 是一个挂起函数，所以能够保证 nameTv.text 的正确赋值，这就涉及到了协程中最著名的「非阻塞式挂起」。这个名词看起来
不是那么容易理解，我们后续的文章会专门对这个概念进行讲解。现在先把这个概念放下，只需要记住协程就是这样写的就行了。
##挂起函数， 非阻塞式挂起

这种「用同步的方式写异步的代码」看起来很方便吧，那么我们来看看协程具体好在哪。

协程好在哪

开始之前

在讲之前，我们需要先了解一下「闭包」这个概念，调用 Kotlin 协程中的 API，经常会用到闭包写法。
##闭包写法
其实闭包并不是 Kotlin 中的新概念，在 Java 8 中就已经支持。

我们先以 Thread 为例，来看看什么是闭包：

🏝️
// 创建一个 Thread 的完整写法
Thread(object : Runnable {
override fun run() {
...
}
})

// 满足 SAM，先简化为
Thread({
...
})

// 使用闭包，再简化为
Thread {
...
}
Kotlin
形如 Thread {...} 这样的结构中 {} 就是一个闭包。

##闭包语法糖
在 Kotlin 中有这样一个语法糖：当函数的最后一个参数是 lambda 表达式时，可以将 lambda 写在括号外。这就是它的闭包原则。

在这里需要一个类型为 Runnable 的参数，而 Runnable 是一个接口，且只定义了一个函数 run，这种情况满足了 Kotlin 的 SAM，可以转换成传递一个
lambda 表达式（第二段），因为是最后一个参数，根据闭包原则我们就可以直接写成 Thread {...}（第三段） 的形式。

对于上文所使用的 launch 函数，可以通过闭包来进行简化 ：

🏝️
launch {
...
}
Kotlin
基本使用

前面提到，launch 函数不是顶层函数，是不能直接用的，可以使用下面三种方法来创建协程：
## 创建协程
🏝️
// 方法一，使用 runBlocking 顶层函数
runBlocking {
getImage(imageId)
}

// 方法二，使用 GlobalScope 单例对象
//            👇 可以直接调用 launch 开启协程
GlobalScope.launch {
getImage(imageId)
}

// 方法三，自行通过 CoroutineContext 创建一个 CoroutineScope 对象
//                                    👇 需要一个类型为 CoroutineContext 的参数
val coroutineScope = CoroutineScope(context)
coroutineScope.launch {
getImage(imageId)
}
Kotlin
方法一通常适用于单元测试的场景，而业务开发中不会用到这种方法，因为它是线程阻塞的。
方法二和使用 runBlocking 的区别在于不会阻塞线程。但在 Android 开发中同样不推荐这种用法，因为它的生命周期会和 app 一致，
且不能取消（什么是协程的取消后面的文章会讲）。
##协程等取消  CoroutineScope  CoroutineContext
方法三是比较推荐的使用方法，我们可以通过 context 参数去管理和控制协程的生命周期（这里的 context 和 Android 里的不是一个东西，
是一个更通用的概念，会有一个 Android 平台的封装来配合使用）。
关于 CoroutineScope 和 CoroutineContext 的更多内容后面的文章再讲。

##
协程最常用的功能是并发，而并发的典型场景就是多线程。可以使用 Dispatchers.IO 参数把任务切到 IO 线程执行：

🏝️
coroutineScope.launch(Dispatchers.IO) {
...
}
Kotlin
也可以使用 Dispatchers.Main 参数切换到主线程：

🏝️
coroutineScope.launch(Dispatchers.Main) {
...
}
Kotlin
所以在「协程是什么」一节中讲到的异步请求的例子完整写出来是这样的：

🏝️
coroutineScope.launch(Dispatchers.Main) {   // 在主线程开启协程
val user = api.getUser() // IO 线程执行网络请求
nameTv.text = user.name  // 主线程更新 UI
}
Kotlin
而通过 Java 实现以上逻辑，我们通常需要这样写：

☕️
api.getUser(new Callback&lt;User&gt;() {
@Override
public void success(User user) {
runOnUiThread(new Runnable() {
@Override
public void run() {
nameTv.setText(user.name);
}
})
}

    @Override
    public void failure(Exception e) {
        ...
    }
});
Java
##
这种回调式的写法，打破了代码的顺序结构和完整性，读起来相当难受。

协程的「1 到 0」

对于回调式的写法，如果并发场景再复杂一些，代码的嵌套可能会更多，这样的话维护起来就非常麻烦。但如果你使用了 Kotlin 协程，多层网络请求只需要这么写：

🏝️
coroutineScope.launch(Dispatchers.Main) {       // 开始协程：主线程
val token = api.getToken()                  // 网络请求：IO 线程
val user = api.getUser(token)               // 网络请求：IO 线程
nameTv.text = user.name                     // 更新 UI：主线程
}
Kotlin
如果遇到的场景是多个网络请求需要等待所有请求结束之后再对 UI 进行更新。比如以下两个请求：

🏝️
api.getAvatar(user, callback)
api.getCompanyLogo(user, callback)
Kotlin
如果使用回调式的写法，那么代码可能写起来既困难又别扭。于是我们可能会选择妥协，通过先后请求代替同时请求：

🏝️
api.getAvatar(user) { avatar -&gt;
api.getCompanyLogo(user) { logo -&gt;
show(merge(avatar, logo))
}
}
Kotlin
在实际开发中如果这样写，本来能够并行处理的请求被强制通过串行的方式去实现，可能会导致等待时间长了一倍，也就是性能差了一倍。

而如果使用协程，可以直接把两个并行请求写成上下两行，最后再把结果进行合并即可：
##两个请求合并后更新UI
🏝️
coroutineScope.launch(Dispatchers.Main) {
//            👇  async 函数之后再讲
val avatar = async { api.getAvatar(user) }    // 获取用户头像
val logo = async { api.getCompanyLogo(user) } // 获取用户所在公司的 logo
val merged = suspendingMerge(avatar, logo)    // 合并结果
//                  👆
show(merged) // 更新 UI
}
Kotlin
可以看到，即便是比较复杂的并行网络请求，也能够通过协程写出结构清晰的代码。需要注意的是 suspendingMerge 并不是协程 API 中提供的方法，
而是我们自定义的一个可「挂起」的结果合并方法。至于挂起具体是什么，可以看下一篇文章。
## 挂起？
##
让复杂的并发代码，写起来变得简单且清晰，是协程的优势。

这里，两个没有相关性的后台任务，因为用了协程，被安排得明明白白，互相之间配合得很好，也就是我们之前说的「协作式任务」。

本来需要回调，现在直接没有回调了，这种从 1 到 0 的设计思想真的妙哉。

在了解了协程的作用和优势之后，我们再来看看协程是怎么使用的。

协程怎么用

在项目中配置对 Kotlin 协程的支持

在使用协程之前，我们需要在 build.gradle 文件中增加对 Kotlin 协程的依赖：

项目根目录下的 build.gradle :
buildscript {
...
// 👇
ext.kotlin_coroutines = '1.3.1'
...
}
Groovy
Module 下的 build.gradle :
dependencies {
...
//                                       👇 依赖协程核心库
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines"
//                                       👇 依赖当前平台所对应的平台库
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines"
...
}
Groovy
Kotlin 协程是以官方扩展库的形式进行支持的。而且，我们所使用的「核心库」和 「平台库」的版本应该保持一致。

核心库中包含的代码主要是协程的公共 API 部分。有了这一层公共代码，才使得协程在各个平台上的接口得到统一。
平台库中包含的代码主要是协程框架在具体平台的具体实现方式。因为多线程在各个平台的实现方式是有所差异的。
完成了以上的准备工作就可以开始使用协程了。

开始使用协程

协程最简单的使用方法，其实在前面章节就已经看到了。我们可以通过一个 launch 函数实现线程切换的功能：

🏝️
//               👇
coroutineScope.launch(Dispatchers.IO) {
...
}
Kotlin
这个 launch 函数，它具体的含义是：我要创建一个新的协程，并在指定的线程上运行它。这个被创建、被运行的所谓「协程」是谁？就
是你传给 launch 的那些代码，这一段连续代码叫做一个「协程」。

所以，什么时候用协程？当你需要切线程或者指定线程的时候。你要在后台执行任务？切！

🏝️
launch(Dispatchers.IO) {
val image = getImage(imageId)
}
Kotlin
然后需要在前台更新界面？再切！

🏝️
coroutineScope.launch(Dispatchers.IO) {
val image = getImage(imageId)
launch(Dispatchers.Main) {
avatarIv.setImageBitmap(image)
}
}
Kotlin
好像有点不对劲？这不还是有嵌套嘛。

如果只是使用 launch 函数，协程并不能比线程做更多的事。不过协程中却有一个很实用的函数：withContext 。这个函数可以切换到
指定的线程，并在闭包内的逻辑执行结束之后，自动把线程切回去继续执行。那么可以将上面的代码写成这样：

🏝️
coroutineScope.launch(Dispatchers.Main) {      // 👈 在 UI 线程开始
val image = withContext(Dispatchers.IO) {  // 👈 切换到 IO 线程，并在执行完成后切回 UI 线程
getImage(imageId)                      // 👈 将会运行在 IO 线程
}
avatarIv.setImageBitmap(image)             // 👈 回到 UI 线程更新 UI
}
Kotlin
这种写法看上去好像和刚才那种区别不大，但如果你需要频繁地进行线程切换，这种写法的优势就会体现出来。可以参考下面的对比：

🏝️
// 第一种写法
coroutineScope.launch(Dispatchers.IO) {
...
launch(Dispatchers.Main){
...
launch(Dispatchers.IO) {
...
launch(Dispatchers.Main) {
...
}
}
}
}

// 通过第二种写法来实现相同的逻辑
coroutineScope.launch(Dispatchers.Main) {
...
withContext(Dispatchers.IO) {
...
}
...
withContext(Dispatchers.IO) {
...
}
...
}
Kotlin
由于可以"自动切回来"，消除了并发代码在协作时的嵌套。由于消除了嵌套关系，我们甚至可以把 withContext 放进一个单独的函数里面：
##
🏝️
launch(Dispatchers.Main) {              // 👈 在 UI 线程开始
val image = getImage(imageId)
avatarIv.setImageBitmap(image)     // 👈 执行结束后，自动切换回 UI 线程
}
//                               👇
fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
...
}
Kotlin
这就是之前说的「用同步的方式写异步的代码」了。

不过如果只是这样写，编译器是会报错的：

🏝️
fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
// IDE 报错 Suspend function'withContext' should be called only from a coroutine or another suspend funcion
}
Kotlin
意思是说，withContext 是一个 suspend 函数，它需要在协程或者是另一个 suspend 函数中调用。

suspend

suspend 是 Kotlin 协程最核心的关键字，几乎所有介绍 Kotlin 协程的文章和演讲都会提到它。它的中文意思是「暂停」或者「可挂起」。
如果你去看一些技术博客或官方文档的时候，大概可以了解到：「代码执行到 suspend 函数的时候会『挂起』，并且这个『挂起』是非阻塞式的，它不会阻塞你当前的线程。」

上面报错的代码，其实只需要在前面加一个 suspend 就能够编译通过：

🏝️
//👇
suspend fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
...
}
Kotlin
本篇文章到此结束，而 suspend 具体是什么，「非阻塞式」又是怎么回事，函数怎么被挂起，这些疑问的答案，将在下一篇文章全部揭晓。




Kotlin 协程的挂起好神奇好难懂？今天我把它的皮给扒了
大家好，我是扔物线朱凯，我回来啦。今天我们接着讲协程。

在上一期里，我介绍了 Kotlin 的协程到底是什么——它就是个线程框架。没什么说不清的，就这么简单，它就是个线程框架，只不过这个线程框架比较方便
——另外呢，上期也讲了一下协程的基本用法，但到最后也留下了一个大问号：协程最核心的那个「非阻塞式」的「挂起」到底是怎么回事？今天，我们的核心
内容就是来说一说这个「挂起」。

老规矩，全国最硬核的 Android 视频播主为你带来最硬核的视频：

上期回顾

在协程上一期中我们知道了下面知识点：

协程究竟是什么
协程到底好在哪里
协程具体怎么用
大部分情况下，我们都是用 launch 函数来创建协程，其实还有其他两个函数也可以用来创建协程：

runBlocking
async
runBlocking 通常适用于单元测试的场景，而业务开发中不会用到这个函数，因为它是线程阻塞的。

接下来我们主要来对比 launch 与 async 这两个函数。

相同点：它们都可以用来启动一个协程，返回的都是 Coroutine，我们这里不需要纠结具体是返回哪个类。

不同点：async 返回的 Coroutine 多实现了 Deferred 接口。

##
关于 Deferred 更深入的知识就不在这里过多阐述，它的意思就是延迟，也就是结果稍后才能拿到。

我们调用 Deferred.await() 就可以得到结果了。

接下来我们继续看看 async 是如何使用的，先回忆一下上期中获取头像的场景：

🏝️
coroutineScope.launch(Dispatchers.Main) {
//                      👇  async 函数启动新的协程
val avatar: Deferred = async { api.getAvatar(user) }    // 获取用户头像
val logo: Deferred = async { api.getCompanyLogo(user) } // 获取用户所在公司的 logo
//            👇          👇 获取返回值
show(avatar.await(), logo.await())                     // 更新 UI
}
Kotlin
可以看到 avatar 和 logo 的类型可以声明为 Deferred ，通过 await 获取结果并且更新到 UI 上显示。

await 函数签名如下：

🏝️
public suspend fun await(): T
Kotlin
前面有个关键字是之前没有见过的 —— suspend，这个关键字就对应了上期最后我们留下的一个问号：协程最核心的那个「非阻塞式」的「挂起」到底是怎么回事？

所以接下来，我们的核心内容就是来好好说一说这个「挂起」。

「挂起」的本质

协程中「挂起」的对象到底是什么？挂起线程，还是挂起函数？都不对，我们挂起的对象是协程。
##挂起的对象是协程

还记得协程是什么吗？启动一个协程可以使用 launch 或者 async 函数，协程其实就是这两个函数中闭包的代码块。

launch ，async 或者其他函数创建的协程，在执行到某一个 suspend 函数的时候，这个协程会被「suspend」，也就是被挂起。

那此时又是从哪里挂起？从当前线程挂起。换句话说，就是这个协程从正在执行它的线程上脱离。

注意，不是这个协程停下来了！是脱离，当前线程不再管这个协程要去做什么了。

suspend 是有暂停的意思，但我们在协程中应该理解为：当线程执行到协程的 suspend 函数的时候，暂时不继续执行协程代码了。

我们先让时间静止，然后兵分两路，分别看看这两个互相脱离的线程和协程接下来将会发生什么事情：

线程：

前面我们提到，挂起会让协程从正在执行它的线程上脱离，具体到代码其实是：

协程的代码块中，线程执行到了 suspend 函数这里的时候，就暂时不再执行剩余的协程代码，跳出协程的代码块。

那线程接下来会做什么呢？

如果它是一个后台线程：

要么无事可做，被系统回收
要么继续执行别的后台任务
跟 Java 线程池里的线程在工作结束之后是完全一样的：回收或者再利用。

如果这个线程它是 Android 的主线程，那它接下来就会继续回去工作：也就是一秒钟 60 次的界面刷新任务。

一个常见的场景是，获取一个图片，然后显示出来：

🏝️
// 主线程中
GlobalScope.launch(Dispatchers.Main) {
val image = suspendingGetImage(imageId)  // 获取图片
avatarIv.setImageBitmap(image)           // 显示出来
}

suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {
...
}
Kotlin
这段执行在主线程的协程，它实质上会往你的主线程 post 一个 Runnable，这个 Runnable 就是你的协程代码：

🏝️
handler.post {
val image = suspendingGetImage(imageId)
avatarIv.setImageBitmap(image)
}
Kotlin
当这个协程被挂起的时候，就是主线程 post 的这个 Runnable 提前结束，然后继续执行它界面刷新的任务。

关于线程，我们就看完了。
这个时候你可能会有一个疑问，那 launch 包裹的剩下代码怎么办？

所以接下来，我们来看看协程这一边。

协程：

线程的代码在到达 suspend 函数的时候被掐断，接下来协程会从这个 suspend 函数开始继续往下执行，不过是在指定的线程。

谁指定的？是 suspend 函数指定的，比如我们这个例子中，函数内部的 withContext 传入的 Dispatchers.IO 所指定的 IO 线程。

Dispatchers 调度器，它可以将协程限制在一个特定的线程执行，或者将它分派到一个线程池，或者让它不受限制地运行，关于 Dispatchers 这里先不展开了。

那我们平日里常用到的调度器有哪些？

常用的 Dispatchers ，有以下三种：

Dispatchers.Main：Android 中的主线程
Dispatchers.IO：针对磁盘和网络 IO 进行了优化，适合 IO 密集型的任务，比如：读写文件，操作数据库以及网络请求
Dispatchers.Default：适合 CPU 密集型的任务，比如计算
回到我们的协程，它从 suspend 函数开始脱离启动它的线程，继续执行在 Dispatchers 所指定的 IO 线程。

紧接着在 suspend 函数执行完成之后，协程为我们做的最爽的事就来了：会自动帮我们把线程再切回来。

这个「切回来」是什么意思？

我们的协程原本是运行在主线程的，当代码遇到 suspend 函数的时候，发生线程切换，根据 Dispatchers 切换到了 IO 线程；

当这个函数执行完毕后，线程又切了回来，「切回来」也就是协程会帮我再 post 一个 Runnable，让我剩下的代码继续回到主线程去执行。

我们从线程和协程的两个角度都分析完成后，终于可以对协程的「挂起」suspend 做一个解释：

协程在执行到有 suspend 标记的函数的时候，会被 suspend 也就是被挂起，而所谓的被挂起，就是切个线程；

不过区别在于，挂起函数在执行完成之后，协程会重新切回它原先的线程。
##
再简单来讲，在 Kotlin 中所谓的挂起，就是一个稍后会被自动切回来的线程调度操作。

这个「切回来」的动作，在 Kotlin 里叫做 resume，恢复。
通过刚才的分析我们知道：挂起之后是需要恢复。

而恢复这个功能是协程的，如果你不在协程里面调用，恢复这个功能没法实现，所以也就回答了这个问题：为什么挂起函数必须在协程或者另一个挂起函数里被调用。

再细想下这个逻辑：一个挂起函数要么在协程里被调用，要么在另一个挂起函数里被调用，那么它其实直接或者间接地，总是会在一个协程里被调用的。

所以，要求 suspend 函数只能在协程里或者另一个 suspend 函数里被调用，还是为了要让协程能够在 suspend 函数切换线程之后再切回来。

怎么就「挂起」了？

我们了解到了什么是「挂起」后，再接着看看这个「挂起」是怎么做到的。

先随便写一个自定义的 suspend 函数：

🏝️
suspend fun suspendingPrint() {
println(&quot;Thread: ${Thread.currentThread().name}&quot;)
}

I/System.out: Thread: main
Kotlin
输出的结果还是在主线程。

为什么没切换线程？因为它不知道往哪切，需要我们告诉它。

对比之前例子中 suspendingGetImage 函数代码：

🏝️
//                                               👇
suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {
...
}
Kotlin
我们可以发现不同之处其实在于 withContext 函数。

其实通过 withContext 源码可以知道，它本身就是一个挂起函数，它接收一个 Dispatcher 参数，依赖这个 Dispatcher 参数的指示，你的协程被挂起，然后切到别的线程。

所以这个 suspend，其实并不是起到把任何把协程挂起，或者说切换线程的作用。

真正挂起协程这件事，是 Kotlin 的协程框架帮我们做的。

所以我们想要自己写一个挂起函数，仅仅只加上 suspend 关键字是不行的，还需要函数内部直接或间接地调用到 Kotlin 协程框架自带的 suspend 函数才行。

suspend 的意义？

这个 suspend 关键字，既然它并不是真正实现挂起，那它的作用是什么？

它其实是一个提醒。

函数的创建者对函数的使用者的提醒：我是一个耗时函数，我被我的创建者用挂起的方式放在后台运行，所以请在协程里调用我。

为什么 suspend 关键字并没有实际去操作挂起，但 Kotlin 却把它提供出来？

因为它本来就不是用来操作挂起的。

挂起的操作 —— 也就是切线程，依赖的是挂起函数里面的实际代码，而不是这个关键字。

所以这个关键字，只是一个提醒。

还记得刚才我们尝试自定义挂起函数的方法吗？

🏝️
// 👇 redundant suspend modifier
suspend fun suspendingPrint() {
println(&quot;Thread: ${Thread.currentThread().name}&quot;)
}
Kotlin
如果你创建一个 suspend 函数但它内部不包含真正的挂起逻辑，编译器会给你一个提醒：redundant suspend modifier，告诉你这个 suspend 是多余的。

因为你这个函数实质上并没有发生挂起，那你这个 suspend 关键字只有一个效果：就是限制这个函数只能在协程里被调用，如果在非协程的代码中调用，就会编译不通过。

所以，创建一个 suspend 函数，为了让它包含真正挂起的逻辑，要在它内部直接或间接调用 Kotlin 自带的 suspend 函数，你的这个 suspend 才是有意义的。

怎么自定义 suspend 函数？

在了解了 suspend 关键字的来龙去脉之后，我们就可以进入下一个话题了：怎么自定义 suspend 函数。

这个「怎么自定义」其实分为两个问题：

什么时候需要自定义 suspend 函数？
具体该怎么写呢？
什么时候需要自定义 suspend 函数

如果你的某个函数比较耗时，也就是要等的操作，那就把它写成 suspend 函数。这就是原则。

耗时操作一般分为两类：I/O 操作和 CPU 计算工作。比如文件的读写、网络交互、图片的模糊处理，都是耗时的，通通可以把它们写进 suspend 函数里。

另外这个「耗时」还有一种特殊情况，就是这件事本身做起来并不慢，但它需要等待，比如 5 秒钟之后再做这个操作。这种也是 suspend 函数的应用场景。

具体该怎么写

给函数加上 suspend 关键字，然后在 withContext 把函数的内容包住就可以了。

提到用 withContext是因为它在挂起函数里功能最简单直接：把线程自动切走和切回。

当然并不是只有 withContext 这一个函数来辅助我们实现自定义的 suspend 函数，比如还有一个挂起函数叫 delay，它的作用是等待一段时间后再继续往下执行代码。

使用它就可以实现刚才提到的等待类型的耗时操作：

🏝️
suspend fun suspendUntilDone() {
while (!done) {
delay(5)
}
}
Kotlin
这些东西，在我们初步使用协程的时候不用立马接触，可以先把协程最基本的方法和概念理清楚。

总结

我们今天整个文章其实就在理清一个概念：什么是挂起？挂起，就是一个稍后会被自动切回来的线程调度操作。

好，关于协程中的「挂起」我们就解释到这里。

可能你心中还会存在一些疑惑：

协程中挂起的「非阻塞式」到底是怎么回事？
协程和 RxJava 在切换线程方面功能是一样的，都能让你写出避免嵌套回调的复杂并发代码，那协程还有哪些优势，或者让开发者使用协程的理由？
这些疑惑的答案，我们都会在下一篇中全部揭晓。

