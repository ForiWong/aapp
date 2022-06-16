package com.wlp.myapplication.frame.mvvm

import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import com.afollestad.materialdialogs.MaterialDialog
import com.wlp.myapplication.util.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * Description:Fragment基类
 */
abstract class BaseFragment<V : ViewDataBinding?, VM : BaseViewModel<*>?>
    : Fragment(), IBaseView {
    protected var binding: V? = null
    protected var viewModel: VM? = null
    private var viewModelId = 0
//    private var dialog: MaterialDialog? = null
//    private var dialogBuilder: MaterialDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        dialogBuilder = this.context?.let {
//            MaterialDialog.Builder(it).title("加载中").progress(true, 0)
//        }
        initParam()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            initContentView(inflater, container, savedInstanceState),
            container,
            false
        )
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //解除ViewModel生命周期感应
        viewModel?.let{ lifecycle.removeObserver(it)}
        if (binding != null) {
            binding!!.unbind()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
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
//            viewModel = createViewModel(this, vmCls, modelCls)
        }
        binding!!.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        viewModel?.let { lifecycle.addObserver(it) }
    }

    /**
     * =============================================================================================
     */
    //注册ViewModel与View的契约UI回调事件
    protected fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel!!.getUiChangeData()?.showDialogEvent?.observe(viewLifecycleOwner) { title -> showDialog(title)}
        //加载对话框消失
        viewModel!!.getUiChangeData()?.dismissDialogEvent?.observe(viewLifecycleOwner) { dismissDialog() }
        //跳入新页面
        viewModel!!.getUiChangeData()?.startActivityEvent?.observe(viewLifecycleOwner) { params ->
            val clz = params!![BaseViewModel.ParameterField.CLASS] as Class<*>?
            val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle?
            startActivity(clz, bundle)
        }
        //关闭界面
        viewModel!!.getUiChangeData()?.finishEvent?.observe(viewLifecycleOwner) { requireActivity().finish() }
        //关闭上一层
        viewModel!!.getUiChangeData()?.onBackPressedEvent?.observe(viewLifecycleOwner) { requireActivity().onBackPressed() }
    }

    fun showDialog(tip: String?) {
//        if (dialogBuilder == null) dialogBuilder = MaterialDialog.Builder(this.requireContext()).progress(true, 0)
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
        startActivity(Intent(context, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * =============================================================================================
     */
    //刷新布局
    fun refreshLayout() {
        viewModel.let {
            binding!!.setVariable(viewModelId, viewModel)
        }
    }

    override fun initParam() {}

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int

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
    val isBackPressed: Boolean
        get() = false

    /**
     * 创建ViewModel，不能直接调用构造函数，不然无法发挥其作用
     * 多个fragment共享viewModel怎么实现？new ViewModelProvider(requireActivity()).get(cls); 用起来不方便吧
     * @param cls
     * @param <T>
     * @return
    </T> */
//    private fun createViewModel(
//        fragment: Fragment,
//        cls: Class<*>, modelCls: Class<*>?
//    ): VM {
//        cls as Class<VM>
//        modelCls as Class<BaseModel>

//        return if (modelCls == null) {
//            ViewModelProvider(fragment).get(cls)
//        } else {
//            val factory: ViewModelFactory<*> = ViewModelFactory<BaseModel>(fragment.activity?.application, modelCls)
//            ViewModelProvider(fragment, factory).get(cls)
//        }
//    }
}