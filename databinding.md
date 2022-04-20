https://www.jianshu.com/p/bd9016418af2
Android DataBinding 从入门到进阶
Fork https://github.com/leavesCZY/DataBindingSamples

1、build.gradle中添加：
android {
    ....
    dataBinding {
        enabled = true
    }
}

2、布局文件使用标签<layout>、<data>

<data>
	<variable name="user" type="com.example.User"/>
</data>

android:text="@{user.name}"

3、数据绑定
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
   User user = new User("Test", "User");
   binding.setUser(user);
}

同样的，fragment、listView、RecyclerView也是可以数据绑定的。

4、事件绑定
<TextView android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{user.firstName}"
    android:onLongClick="@{handlers::onLongClickFriend}"
    android:onClick="@{handlers::onClickFriend}"/>

5、响应类的import
<data>
    <import type="com.example.MyStringUtils"/>
    <import type="android.view.View"/>
    <variable name="user" type="com.example.User"/>
</data>

alias 可以指定别名

6、Databinding支持表达式：
可以在layout中 像Java一样使用如下的一些表达式：
数学表达式 + – / * %
字符串链接 +
逻辑操作符 && ||
二元操作符 & | ^
一元操作符 + – ! ~
Shift >> >>> <<
比较 == > < >= <=
instanceof
Grouping ()
Literals – character, String, numeric, null
Cast
函数调用
值域引用（Field access）
通过[]访问数组里面的对象
三元操作符 ?:

7、observable
databinding是通过在数据变化时给出相应的通知来告知Ui层进行更新的。共有三种数据变化的机制，Observable objects, observable fields, and observable collections.
当他们中的任何一个与UI进行绑定，并且其数据属性发生变化时，都会通知到UI进行更新。

BaseObservable 提供了 notifyChange() 和 notifyPropertyChanged() 两个方法，前者会刷新所有的值域，后者则只更新对应 BR 的 flag，该 BR 的生成通过注释 @Bindable 生成，可以通过 BR notify 特定属性关联的视图

8、双向数据绑定
双向绑定的意思即为当数据改变时同时使视图刷新，而视图改变时也可以同时改变数据。
当 EditText 的输入内容改变时，会同时同步到变量 goods,绑定变量的方式比单向绑定多了一个等号：android:text="@={goods.name}"

9、自定义绑定 @BindingAdapter{()}
给一些View添加了自定义的方法或者属性（官方不支持的方法），该如何进行绑定呢？是的，databinding当然也是支持自定义进行绑定。
我们先找一个官方支持的属性看时如何实现的，比如android:paddingLeft，是可以单独进行设置的，但是View的方法中，是没有setPaddingLeft的方法的，只有setPadding(left, top, right, bottom)方法。那么此时单独绑定paddingLeft就如下：
@BindingAdapter("android:paddingLeft")
public static void setPaddingLeft(View view, int padding) {
   view.setPadding(padding,
                   view.getPaddingTop(),
                   view.getPaddingRight(),
                   view.getPaddingBottom());
}

@BindingAdapter({"bind:imageUrl", "bind:error"})
public static void loadImage(ImageView view, String url, Drawable error) {
   Picasso.with(view.getContext()).load(url).error(error).into(view);
}

10、其他
（1）布局中使用一个静态方法
（2）避免空指针异常，DataBinding 也会自动帮助我们避免空指针异常
如果 "@{userInfo.password}" 中 userInfo 为 null 的话，userInfo.password 会被赋值为默认值 null，而不会抛出空指针异常
（3）支持使用数组、List、Set和Map的使用
（4）资源引用，尺寸和字符串类资源的访问
（5）集合include、viewSub标签的使用
