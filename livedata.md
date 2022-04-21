LiveData详解
作者：竖起大拇指
链接：https://www.jianshu.com/p/fa60246d87c0

1、LiveData是Jetpack AAC的重要组件，同时已有一个同名抽象类。
LiveData，原意是活着的数据。数据还能有生命？先来看下官方的定义：

LiveData是一种可观察的数据存储器类。它与常规的可观察类不同，LiveData具有生命周期感知能力，意指它遵循其他应用组件  
(如Activity/Fragment)的生命周期。这种感知能力可确保LiveData仅更新处于活跃生命周期状态的应用组件观察者。

拆解开来：
1.LiveData是一个数据持有者，给源数据包装一层。
2.源数据使用LiveData包装后，可以被observer观察，数据有更新时observer可感知。
3.但observer的感知，只发生在(Activity/Fragment)活跃生命周期状态(STARTED,RESUMED)。

也就是说，LiveData使得数据的更新能以观察者模式被observer感知，且此感知只发生在LifecycleOwner的活跃生命周期状态。

2、使用 LiveData 具有以下优势：

确保界面符合数据状态，当生命周期状态变化时，LiveData通知Observer，可以在observer中更新界面。观察者可以在生命周期状态  
更改时刷新界面，而不是在每次数据变化时刷新界面。
不会发生内存泄漏，observer会在LifecycleOwner状态变为DESTROYED后自动remove。
不会因 Activity 停止而导致崩溃，如果LifecycleOwner生命周期处于非活跃状态，则它不会接收任何 LiveData事件。
不需要手动解除观察，开发者不需要在onPause或onDestroy方法中解除对LiveData的观察，因为LiveData能感知生命周期状态变化，所  
以会自动管理所有这些操作。
数据始终保持最新状态，数据更新时，若LifecycleOwner为非活跃状态，那么会在变为活跃时接收最新数据。例如，曾经在后台的  
Activity 会在返回前台后，observer立即接收最新的数据。

3.LiveData的使用
1.创建LiveData实例，指定源数据类型
2.创建Observer实例，实现onChanged()方法，用于接收源数据变化并刷新UI
3.LiveData实例使用observe()方法添加观察者，并传入LifecycleOwner
4.LiveData实例使用setValue()/postValue()更新源数据 （子线程要postValue()）

4、扩展使用
扩展包括两点：
1.自定义LiveData，本身回调方法的覆写：onActive(),onInactive().

2.使用LiveData为单列模式，便于在多个Activity,Fragment之间共享数据。
共享资源
您可以使用单例模式扩展 LiveData 对象以封装系统服务，以便在应用中共享它们。LiveData 对象连接到系统服务一次，然后  
需要相应资源的任何观察者只需观察 LiveData 对象。如需了解详情，请参阅扩展 LiveData。

3.如果希望在将 LiveData 对象分派给观察者之前对存储在其中的值进行更改，或者需要根据另一个实例的值返回不同的  
 LiveData 实例，可以使用LiveData中提供的Transformations类。

4.数据切换-Transformations.switchMap
如果想要根据某个值 切换观察不同LiveData数据，则可以使用Transformations.switchMap()方法。
## 使用的地方？

5.观察多个数据-MediatorLiveData
MediatorLiveData 是 LiveData 的子类，允许合并多个 LiveData 源。只要任何原始的 LiveData 源对象发生更改，就会触发  
MediatorLiveData 对象的观察者。


