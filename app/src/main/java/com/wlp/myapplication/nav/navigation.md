1、Navigation是一个可简化Android导航的库和插件
更确切的来说，Navigation是用来管理Fragment的切换，并且可以通过可视化的方式，看见App的交互流程。这完美的契合了  
Jake Wharton大神单Activity的建议。

优点
处理Fragment的切换（上文已说过）
默认情况下正确处理Fragment的前进和后退
为过渡和动画提供标准化的资源
实现和处理深层连接
可以绑定Toolbar、BottomNavigationView和ActionBar等
SafeArgs（Gradle插件） 数据传递时提供类型安全性
ViewModel支持

2、  
Navigation组件是为了那些使用"单Activity多Fragment"架构模式的app而设计，在这个系列教程里我们只使用Fragment作为目的  
地去管理界面和实现界面跳转

3、使用
（1）project、app根目录下的build.gradle文件设置
（2）创建导航视图 navigation/nav_grap.xml
（3）在Activity里添加NavHost
什么是NavHost？
NavHost是一个接口，实现了NavHost接口的类称为导航宿主。
导航宿主(NavHost)是一个目的地容器

直接使用NavHostFragment
导航宿主必须派生自NavHost接口。在导航组件里有一个默认的实现了NavHost接口的类叫NavHostFragment，这个  
NavHostFragment作为导航宿主，它既作为Fragment目的地的容器，又处理Fragment目的地之间的交换。

总结：当我们只使用Fragment作为目的地时，不需要再写一个类去实现NavHost接口，直接使用NavHostFragment即可满足需求。

（3）添加NavHostFragment到Activity的xml文件中

（4）创建Fragment作为目的地

（5）添加目的地到导航图
通过<fragment>元素添加目的地 nav_grap.xml

include

标签属性 startDestination
    跳转动作 action --> destination
	传递参数 argument  --> name/argType

（6）在Fragment之间传递参数/接收参数


（7）NavOptions
类似Activity，Fragment也有返回栈。我们可以通过NavOptions保存和恢复Fragment状态，灵活地管理返回栈。
NavOptions stores special options for navigate actions
意思是NavOpstions存储导航操作的特殊选项。
在这里解释一下：除了目的地跳转和参数传递，其他都是特殊选项。

（8）深链接DeepLink
In Android, a deep link is a link that takes you directly to a specific destination within an app.
翻译：深链接是一个能够直接到达app中特定目的地的链接。

使用这个库有一个前提条件，就是整个app只有一个activity多个fragment。
如果就像之前是多个activity的话，有必要使用navigation吗？

