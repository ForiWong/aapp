package com.wlp.myapplication.vm.ui.login;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.wlp.myapplication.R;
import com.wlp.myapplication.vm.data.LoginRepository;
import com.wlp.myapplication.vm.data.Result;
import com.wlp.myapplication.vm.data.model.LoggedInUser;

/**
 * 1、LiveData是生命周期感知的，在当前的LifecycleOwner不处于活动状态（例如onPause()、onStop()）时，LiveData
 * 是不会回调observe()的，因为没有意义。
 * 2、如果LiveData没有被observe()，那么此时你调用这个LiveData的postValue(...)/value=...，是没有任何作用。
 *
 */
public class LoginViewModel extends ViewModel {

    //MutableLiveData 可变的liveData,它暴露出#setValue(T)、#postValue(T)两个方法
    //被activity(observer 观察者) （observe 观察） liveData 的变化
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public MutableLiveData<String> title = new MutableLiveData<>();

    public MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
    // MutableLiveData的子类，它是一个比较强大的LiveData，我们的map()、switchMap()都是基于它进行实现的。
    // 最大的特点是可以同时监听多个LiveData。

    //map()使用
    MutableLiveData<Long> userIdLiveData = new MutableLiveData<Long>();
    // 伪码:当userIdLiveData发生变化时，userLiveData中的map就会调用，那么我们就可以得到最新的id
    LiveData<LoggedInUser> userLiveData = Transformations.map(userIdLiveData, new Function<Long, LoggedInUser>() {
        @Override
        public LoggedInUser apply(Long input) {
            return null;
        }
    });

    SavedStateHandle savedStateHandle;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}