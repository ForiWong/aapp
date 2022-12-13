## 问题：为啥框架中使用lifecycleviewmodel没有执行ondestroy ，各种交叉引用导致没有回收吗？

官网：LiveData是一个可观察的数据持有者类。与常规observable不同，LiveData是生命周期感知的。
可观察、生命周期感知。简单来说，Google给我们提供了一个可以被观察的，并且拥有生命周期感知能力的类。那有什么用呢？

ViewModel是存储UI相关数据并不会因为旋转而销毁的类。
最为重要的就是ViewModel具有下面的生命周期，这就是ViewModel的最可贵之处：

正因为ViewModel有如此的生命周期，所以ViewModel在MVVM可以作为数据存储区，是连接View和Model重要组件，ViewModel的核心作用如下图所示：

将有生命周期变化的回调到普通操作时，进行解耦。

1、Lifecycle组件是在其他组件的生命周期状态发生改变时，产生相应行为的一种组件。
2、Lifecycle能帮助产生更好组织且更轻量级的代码，便于维护。
3、在不使用Lifecycle时，常规的生命周期处理的缺点是什么?
常规方法是在Activity和Fragment的生命周期方法里面去实现独立组件的行为；
这种模式会导致代码组织性差；
增生更多的错误。

4、Lifecycle的好处是什么?
通过使用可感知生命周期的组件，可以将生命周期方法中关于这些组件的代码，移动到组件的内部
通过android.arch.lifecycle提供的内容，可以让组件主动调节自身的行为，根据activity/fragment当前的生命周期状态进行调整。

5、几乎所有app组件都可以和Lifecycle关联起来，这些都是由操作系统或者运行在程序中的FrameWork层代码进行支持的。
使用Lifecycle能减少内存泄漏和系统崩溃的可能性。

好处：
解耦、代码结构好、易维护。
减少内存泄漏、系统崩溃。
低耦合、高内聚。

旧的使用方法：
使用listener重写回调，在activity的生命周期回调方法中调用listener的对应方法。
常规的处理方法导致onStart()和onStop()等生命周期中会有大量的代码。
特殊情况：还有可能会导致onStart()中的方法在onStop()方法执行后执行


Lifecycle的大致原理
1、Lifecycle类会持有组件(Activity、Fragment)生命周期状态的信息， 并且允许其他对象能监听该状态
2、Lifecycle使用两个主要的枚举对相关的组件的生命周期状态进行追踪

Event
Lifecycle事件由framework和Lifecycle类进行分发;
Event会映射到activity和fragment的回调事件上。

State
Lifecycle对象会追踪组建的当前状态

3、Events和States
States：
INITIALIZED
DESTORYED
CREATED
STARTED
RESUMED

Events：
ON_CREATE
ON_START
ON_RESUME
ON_PAUSE
ON_STOP
ON_DETSORY
ON_ANY //表示所有的事件都会感知。

LifecycleOwner 生命周期观察者
getLifecycle() Lifecycle 生命周期；//LifecycleOwner
Fragment、AppCompatActivity都实现了LifecycleOwner接口；
自定义Application需要自己实现LifecycleOwner接口。

ComponentActivity、AppCompatActivity

要自定义LifecycleOwner需要借助LifecycleRegistry

就是通过LifecycleRegistry的markState()进行状态的切换
自定义Activity

7、LifecycleRegistry是什么?
Lifecycle的实现类
通过makeState()方法进行状态的切换

使用案例
（1）定位app，在前台时提供精细的定位并更新位置，在后台时提供粗糙的定位并更新位置的服务。
可以使用LiveData，作为一种生命周期感知组件，允许在位置改变时自动更新UI。

（2）开始和停止视频的缓冲
使用生命周期感知组件，尽可能快的开始视频缓冲
推迟重放操作，直到app完全启动
在app销毁后，自动停止app缓冲

（3）开始和停止网络的连接
在前台时，开启实时更新网络数据
在后台时，自动暂停人物

（4）暂停和恢复Animated Drawables
后台时暂停动画，前台时恢复动画

总之，Lifecycle能够自动感知其他组件生命周期，能够降低组件间的耦合性。

LifeCycle 是一个可以感知宿主生命周期变化的组件。常见的宿主包括 Activity/Fragment、Service 和 Application。  
LifeCycle 会持有宿主的生命周期状态的信息，当宿主生命周期发生变化时，会通知监听宿主的观察者。

Jetpack 为我们提供了两个接口：

被观察者：LifecycleOwner

观察者：LifecycleObserver

被监听的系统组件需要去实现 LifecycleOwner 接口，观察者需要实现 LifecycleObserver 接口。

//getLifecycle().addObserver(new TestLifeCycle());

LifecycleObserver接口（ Lifecycle观察者）：实现该接口的类，通过注解的方式，可以通过被LifecycleOwner类的addObserver  
(LifecycleObserver o)方法注册,被注册后，LifecycleObserver便可以观察到LifecycleOwner的生命周期事件。

LifecycleOwner接口（Lifecycle持有者\被观察者）：实现该接口的类持有生命周期(Lifecycle对象)，该接口的生命周期(Lifecycle对  
象)的改变会被其注册的观察者LifecycleObserver观察到并触发其对应的事件。

Lifecycle(生命周期)：和LifecycleOwner不同的是，LifecycleOwner本身持有Lifecycle对象，LifecycleOwner通过其Lifecycle  
getLifecycle()的接口获取内部Lifecycle对象。

State(当前生命周期所处状态)。

Event(当前生命周期改变对应的事件)：如图所示，当Lifecycle发生改变，如进入onCreate,会自动发出ON_CREATE事件。

/**
 * 设置当前的状态和通知观察者 @LifecycleRegister
 * Sets the current state and notifies the observers.
 * Note that if the {@code currentState} is the same state as the last call to this method,
 * calling this method has no effect.
 * @param event The event that was received
 */
handleLifecycleEvent(@NonNull Lifecycle.Event event)




Q:什么是LifeCycle
androidx.lifecycle 软件包提供了可用于构建生命周期感知型组件的类和接口,可执行操作来响应另一个组件（如 Activity 和  
 Fragment）的生命周期状态的变化。

Q: 请说说LifeCycle的好处？
（1）我们在Activity的onCreate()中初始化某些成员（比如MVP架构中的Presenter，或者AudioManager、MediaPlayer等），然后在  
onStop中对这些成员进行对应处理，在onDestroy中释放这些资源，最终会有太多的类似调用并且会导致 onCreate() 和 onDestroy()  
方法变的非常臃肿。比如定位组件，在onCreate中定义，在onStart中启动，在onStop中停止，在onDestory中销毁；同样的，录音组件  
也将有这一系列的生命周期，如果一个Activity中包含了若干个这样的组件，则会在生命周期方法中放置大量的代码，这使得它们难以  
维护。
（2）无法保证组件会在 Activity 或 Fragment 停止之前启动。在我们需要执行长时间运行的操作（如 onStart()中的某种配置检查）  
时尤其如此。这可能会导致出现一种竞态条件，在这种条件下，onStop() 方法会在 onStart() 之前结束，这使得组件留存的时间比  
所需的时间要长。

class MyActivity extends AppCompatActivity {
    private MyLocationListener myLocationListener;

    public void onCreate(...) {
        myLocationListener = new MyLocationListener(this, location -> {
            // update UI
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Util.checkUserStatus(result -> {
            // 有可能onStop（）执行完毕了，回调还没有得到执行
            if (result) {
                myLocationListener.start();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        myLocationListener.stop();
    }
}
androidx.lifecycle 软件包提供的类和接口可帮助您以弹性和隔离的方式解决这些问题，它将生命周期的响应分发到各个观察者中去

Q: 说说LifeCycle相关组件所扮演的角色？
-LifecycleOwner： 提供了获取Lifecycle接口实现的方法。通过LifecycleOwner，将Lifecycle的实现和Activity等生命周期组件解耦。  
可以把LifecycleOwner理解为Lifecycle的“工厂”。

Lifecycle：是被观察者，用于存储有关组件（如 Activity 或 Fragment）的生命周期状态的信息，并允许其他对象观察此状态

LifecycleObserver：观察者，可以通过被LifecycleRegistry类通过 addObserver(LifecycleObserver o)方法注册,被注册后，  
LifecycleObserver便可以观察到LifecycleOwner对应的生命周期事件

以上三者的持有关系：

LifecycleOwner -> Lifecycle -> (n) LifecycleObserver
Lifecycle.Event：分派的生命周期事件。这些事件映射到 Activity 和 Fragment 中的回调事件。
Lifecycle.State：Lifecycle组件的当前状态。
LifecycleRegistry: 添加观察者，响应生命周期事件，负责转发生命周期

Q: State和组件（Activity/Fragment）生命周期的对应关系？

INITIALIZED：对应Activity已实例化但在onCreate之前的生命周期
DESTROYED：此后，LifyCycle不再分派生命周期事件。此状态在Activity.onDestroy（）之前
CREATED：对应Activity的onCreate之后和onStop之前的生命周期
STARTED：对应Activity的onStart之后和onPause之前的生命周期
RESUMED：对应Activity的onResume

Q:Lifecycle是怎样感知生命周期的？
AppComponent实现了LifycycleOwer接口声明了mLifecycleRegistry对象，但是没有直接使用其进行生命周期的分发，  
而是被ReportFragment通过activity.getLifecycle()获取使用。

ActivityAppCompatActivity中添加了一个ReportFragment，其生命周期变化时，调用LifecycleRegistry.handleLifecycleEvent()  
方法通知LifecycleRegistry改变状态，LifecycleRegistry内部调用moveToState()改变状态，并调用每个LifecycleObserver.onStateChange()  
方法通知生命周期变化。


Q:ReportFragment是如何被添加到Activity中的呢？
ReportFragment可以通过两种方式添加到Activity中。
1.如果Activity继承自ComponentActivity（AppCompatActivity的间接父类），ComponentActivity的onCreate方法中会调用  
ReportFragment的injectIfNeededIn(Activity)静态方法完成注入。

ReportFragment.injectIfNeededIn(this);

protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ReportFragment.injectIfNeededIn(this);
  }

2.如果Activity没有继承自ComponentActivity，Lifecycle使用LifecycleDispatcher类完成ReportFragment的注入。

LifecycleDispatcher和ProcessLifecycleOwner使用相同的方式跟随进程初始化（后文会提到），LifecycleDispatcher在其静态方法  
init(Context)中，使用Application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks)方法向Application中注册了  
Activity生命周期回调监听器。在监听器的onActivityCreated(Activity, Bundle)回调方法（Activity创建时回调）中，调用  
ReportFragment.injectIfNeededIn(Activity)方法将ReportFragment注入到Activity中。

Q:为什么不直接在SupportActivity的生命周期函数中给Lifecycle分发生命周期事件，而是要加一个Fragment呢？
因为不是所有的页面都继承AppCompatActivity，为了兼容非AppCompatActivity，所以封装一个同样具有生命周期的Fragment来给  
Lifecycle分发生命周期事件。

Q:那我们不继承新版本AppCompatActivity时，Lifecycle是如何通过ReportFragment来分发生命周期事件的呢？
除了ComponentActivity以外，还有两个地方使用到了ReportFragment：LifecycleDispatcher和ProcessLifecycleOwner

LifecycleDispatcher
LifecycleDispatcher是通过注册Application.registerActivityLifecycleCallbacks来监听Activity的生命周期回调的。

在onActivityCreated()中添加ReportFragment，将Activity的生命周期交给ReportFragment去分发给LifecycleRegistry
在onActivityStopped()以及onActivitySaveInstanceState()中，将Activity及其所有子Fragment的State置为CREATED
ProcessLifecycleOwner

ProcessLifecycleOwner是用来监听Application生命周期的，因此它只会分发一次ON_CREATE事件，并且不会分发ON_DESTROY事件
ProcessLifecycleOwner在Activity的onPause和onStop方法中都采用了Handle.postDelayed()方法，是为了处理Activity重建时比如横竖屏幕切换时，不会发送事件。
ProcessLifecycleOwner一般用来判断应用是在前台还是后台。但由于使用了Handle.postDelayed()，因此这个判断不是即时的，有默认700ms的延迟。
ProcessLifecycleOwner与LifecycleDispatcher一样，都是通过注册Application.registerActivityLifecycleCallbacks来监听Activity的生命周  
期回调，来给每个Activity添加ReportFragment的。

Q: ProcessLifecycleOwner和LifecycleDispatcher两个类是在哪里初始化呢？

从源码中我们看到，Lifecycle自动在我们的AndroidManifest.xml中添加了一个ContentProvider，用于初始化  
ProcessLifecycleOwner和LifecycleDispatcher，这么做的好处是，不需要我们在Application中显示调用，不  
需要我们写一行代码。

Q:为什么LifecycleRegistry不是直接分发Event而是先计算State，再反推Event进行分发呢？

LifecycleRegistry的实现充分考虑了各种情况下事件分发的准确性，主要有两类问题：1. 可重入，2. 一致性。
这里的两次转换主要为了解决第2个问题，为了保证不同生命周期添加的观察者都能正确收到状态变换时的各种事件（一致性），  
不会漏掉，LifecycleRegistry通过两次转换实现了重放。

Q: FastSafeIterableMap的特性？
Map，K-V结构存储。
有序，插入顺序决定保存顺序。支持正向、反向遍历。
支持在遍历过程中添加、移除元素。（影响LifecycleRegistry的可重入性）
LifecycleRegistry利用FastSafeIterableMap有序的特性，保证添加的Observer按照插入顺序，State从大到小的顺序排列。所以判断同步完成，  
只需要看第一个Observer和最后一个Observer的状态相等，并且等于LifecycleRegistry的状态。

Q: 如何保证FastSafeIterableMap中存放的ObserverWithState对象的state状态总是先入的会比后入的大？
sync方法因为在提升状态时从前向后遍历，降低状态时从后向前遍历，所以一定能保证执行过程中的状态要求；在有新Observer添加的情况下，  
也可通过防止重入的方式保证在任何时刻mObserverMap中前面的Observer的状态，一定要大于等于后面的Observer的状态。

Q:LifeCycle怎么防止LifeCycleObserver的重入操作？
LifecycleRegistry在处理生命周期事件时，只要涉及到分发事件，都可能出现重入的场景。因为在事件处理方法中，可能添加新的Observer或者  
移除现有的Observer，对Observer集合的修改，就是修改了外部变量。

LifecycleRegistry通过parentState缓存嵌套调用情况下上层调用时的状态，来保证下层调用方法执行时，要同步到的目标状态，不会超过上层  
状态，甚至是当上层的Observer已经被移除的情况（如果没有parentState缓存，后添加的Observer的状态就可能比先添加的Observer的状态更早  
同步到目标状态）。

这里的状态反映到调用者，更多体现在回调方法的调用顺序上。以onStart方法为例，先添加的Observer（A）的onStart方法，一定比后添加的  
Observer（B）的onStart方法先执行完成。即便是B在A的onStart方法中添加的情况下（A.onStart执行完成后，B.onStart方法才会执行）。

Q:LifeCycle使用了哪些设计模式

观察者模式：AppCompatActivity中添加了一个ReportFragment，其生命周期变化时，调用LifecycleRegistry.handleLifecycleEvent()方法通知  
LifecycleRegistry改变状态，LifecycleRegistry内部调用moveToState()改变状态，并调用每个LifecycleObserver.onStateChange()方法通知生  
命周期变化。

适配器模式：在ObserverWithState 静态内部类中， 使用适配器模式对传入的Observer进行封装，通过Lifecycling转为统一的  
LifecycleEventObserver对象，并同时保存对应的mState状态。

Q:LifecycleRegistry 会将外部传入的所有 LifecycleObserver 根据 Lifecycling 包装成 LifecycleEventObserver 对象，这里先来解释下  
为什么需要进行这层包装？

1.LifecycleEventObserver 和 FullLifecycleObserver 都是继承于 LifecycleObserver 的接口，如果开发者自己实现的自定义 Observer 同时  
实现了这两个接口，那按道理来说 LifecycleRegistry 就必须在有事件触发的情况下同时回调这两个接口的所有方法

2.如果开发者自己实现的自定义 Observer 仅实现了 LifecycleEventObserver 和 FullLifecycleObserver 这两个接口当中的一个，那么也需要  
在有事件触发的情况下调用相应接口的对应方法

3.除了通过以上两个接口来实现回调外，Google 也提供了通过注解的方法来声明生命周期回调函数，此时就只能通过反射来进行回调

基于以上三点现状，如果在 LifecycleRegistry 中直接对外部传入的 Observer 来进行类型判断、接口回调、反射调用等一系列操作的话，那势  
必会使得 LifecycleRegistry 整个类非常的臃肿，所以 Lifecycling 的作用就是来将这一系列的逻辑给封装起来，仅仅开放一个 onStateChanged  
 方法即可让 LifecycleRegistry 完成整个事件分发，从而使得整个流程会更加清晰明了且职责分明。

Android-Lifecycle超能解析-生命周期的那些事儿
https://www.jianshu.com/p/2c9bcbf092bc