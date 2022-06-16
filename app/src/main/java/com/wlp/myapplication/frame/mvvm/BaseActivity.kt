package com.wlp.myapplication.frame.mvvm

import androidx.databinding.ViewDataBinding
//import com.afollestad.materialdialogs.MaterialDialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wlp.myapplication.util.LogUtils
import java.lang.reflect.ParameterizedType


/**
 * Description:Activity基类
 *
 * TODO savedInstanceState / onSaveInstanceState 反射、泛型
 * BaseActivity 与 BaseFragment 抽取
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>>
    : AppCompatActivity(), IBaseView {

    protected var binding: V? = null
    protected var viewModel: VM? = null
    private var viewModelId = 0
//    private var dialog: MaterialDialog? = null
//    private var dialogBuilder: MaterialDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initBeforeOnCreate()
        super.onCreate(savedInstanceState)
        LogUtils.d("onCreate():${this.javaClass.simpleName}")
        //页面接受的参数方法
        initParam()
//        dialogBuilder = MaterialDialog.Builder(this).title("加载中")
//            .progress(true, 0)
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("onDestroy():${this.javaClass.simpleName}")
        //解除ViewModel生命周期感应
        viewModel?.let { lifecycle.removeObserver(it) }
        if (binding != null) {
            binding!!.unbind()
        }
    }

    /**
     * 注入绑定
     * 获取this.父类的泛型参数，得到VM.class；获取VM的父类的泛型参数，得到M.class;这个方法是有这个约束的
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.dataBinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        viewModel = initViewModel()
        //initViewModel()如果没有重写，viewModel使用这个方法创建
        if (viewModel == null) {
            var vmCls: Class<*>? = null
            var modelCls: Class<*>? = null
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                LogUtils.d("activity -> types ", types)
                if (types.size > 1) {
                    vmCls = types[1] as Class<*>
                    val vmType = vmCls!!.genericSuperclass
                    if (vmType is ParameterizedType) {
                        val vmTypes = vmType.actualTypeArguments
                        LogUtils.d("activity -> types", vmTypes)
                        if (vmTypes.size > 0) {
                            modelCls = vmTypes[0] as Class<*>
                        }
                    }
                } else {
                    vmCls = BaseViewModel::class.java
                }
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                vmCls = BaseViewModel::class.java
            }
            viewModel = createViewModel(this, vmCls, modelCls)
        }
        //关联ViewModel
        binding!!.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        viewModel?.let { lifecycle.addObserver(it) }
    }

    //刷新布局
    fun refreshLayout() {
        viewModel.let {
            binding!!.setVariable(viewModelId, viewModel)
        }
    }

    /**
     * =============================================================================================
     */
    //注册ViewModel与View的契约UI回调事件
    protected fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel!!.getUiChangeData()?.showDialogEvent?.observe(this) { title -> showDialog(title) }
        //加载对话框消失
        viewModel!!.getUiChangeData()?.dismissDialogEvent?.observe(this) { dismissDialog() }
        //跳入新页面
        viewModel!!.getUiChangeData()?.startActivityEvent?.observe(this) { params ->
            val clz = params!![BaseViewModel.ParameterField.CLASS] as Class<*>?
            val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle?
            startActivity(clz, bundle)
        }
        //关闭界面
        viewModel!!.getUiChangeData()?.finishEvent?.observe(this) { finish() }
        //关闭上一层
        viewModel!!.getUiChangeData()?.onBackPressedEvent?.observe(this) { onBackPressed() }
    }

    fun showDialog(tip: String?) {
//        if (dialogBuilder == null) dialogBuilder = MaterialDialog.Builder(this).progress(true, 0)
//        dialog = dialogBuilder!!.title(tip!!).canceledOnTouchOutside(true).show()
    }

    fun dismissDialog() {
//        if (dialog != null && dialog!!.isShowing) {
//            dialog!!.dismiss()
//        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * =============================================================================================
     */

    /**
     * 在onCreate()调用前进行的操作
     */
    fun initBeforeOnCreate() {}

    override fun initParam() {}

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    fun initViewModel(): VM? {
        return null
    }

    override fun initData() {}

    override fun initViewObservable() {}

    /**
     * 创建ViewModel，不能直接调用构造函数，不然无法发挥其作用
     *
     * //TODO 添加异常处理,这个方法没写好
     */
    private fun createViewModel(
        activity: AppCompatActivity,
        cls: Class<*>, modelCls: Class<*>?
    ): VM {
        cls as Class<VM>
        modelCls as Class<BaseModel>

        return if (modelCls == null) {
            ViewModelProvider(activity)[cls]
        } else {
            val factory: ViewModelFactory<*> = ViewModelFactory<BaseModel>(activity.application, modelCls)
            ViewModelProvider(activity, factory)[cls]
        }
    }
}