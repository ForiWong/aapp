package com.wlp.myapplication.frame.mvvm;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wlp.myapplication.util.LogUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Description:ViewModel工厂
 */
public class ViewModelFactory<M extends BaseModel> extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Class<M> modelCls;//对应的Model的类

    public ViewModelFactory(Application application, Class<M> cls) {
        mApplication = application;
        modelCls = cls;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> cls) {
        try {
            LogUtils.d("创建vm: " + Application.class.toString() + " : " + modelCls.toString());
            LogUtils.d("构造函数：" , cls.getConstructors());
            return cls.getConstructor(Application.class, modelCls).newInstance(mApplication, modelCls.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}