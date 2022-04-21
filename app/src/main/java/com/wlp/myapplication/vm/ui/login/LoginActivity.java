package com.wlp.myapplication.vm.ui.login;

import android.app.Activity;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wlp.myapplication.R;
import com.wlp.myapplication.databinding.ActivityLoginBinding;
import com.wlp.myapplication.livedata.StockLiveData;
import com.wlp.myapplication.vm.data.model.LoggedInUser;

public class LoginActivity extends AppCompatActivity {

    private MutableLiveData<String> mLiveData;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //databinding 竟然是可以这么关联
        //DataBindingUtil.setContentView()
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /**
         * 创建ViewModel
         * 新 var userModel2=ViewModelProvider(viewModelStore,ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
         * 旧 val userModel1:UserViewModel= ViewModelProviders.of(this).get(UserViewModel::class.java)
         */
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        //liveData基本使用
        mLiveData = new MutableLiveData<>();
        //observe()、observeForever()
        mLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("TAG", "onChanged: "+s);
            }
        });
        Log.i("TAG", "onCreate: ");
        mLiveData.setValue("onCreate");

        loginViewModel.title.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.login.setText(s);
            }
        });

        loginViewModel.title.postValue("login...");

        loginViewModel.userLiveData.observe(this, new Observer<LoggedInUser>() {
            @Override
            public void onChanged(LoggedInUser loggedInUser) {
                //user
            }
        });

        loginViewModel.userIdLiveData.postValue(1L);

        //获取StockLiveData单实例，添加观察者，更新UI
        StockLiveData.get("symbol").observe(this, price -> {
            // Update the UI.
        });

        //Integer类型的liveData1
        MutableLiveData<Integer> liveData1 = new MutableLiveData<>();
        //转换成String类型的liveDataMap
        LiveData<String> liveDataMap = Transformations.map(liveData1, new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                String s = input + " + Transformations.map";
                Log.i("TAG", "apply: " + s);
                return s;
            }
        });
        liveDataMap.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("TAG", "onChanged1: "+s);
            }
        });

        liveData1.setValue(100);

        //两个liveData，由liveDataSwitch决定 返回哪个livaData数据
        MutableLiveData<String> liveData3 = new MutableLiveData<>();
        MutableLiveData<String> liveData4 = new MutableLiveData<>();

        //切换条件LiveData，liveDataSwitch的value 是切换条件
        MutableLiveData<Boolean> liveDataSwitch = new MutableLiveData<>();

        //liveDataSwitchMap由switchMap()方法生成，用于添加观察者
        LiveData<String> liveDataSwitchMap = Transformations.switchMap(liveDataSwitch,
                new Function<Boolean, LiveData<String>>() {
            @Override
            public LiveData<String> apply(Boolean input) {
                //这里是具体切换逻辑：根据liveDataSwitch的value返回哪个liveData
                if (input) {
                    return liveData3;
                }
                return liveData4;
            }
        });

        liveDataSwitchMap.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("TAG", "onChanged2: " + s);
            }
        });

        boolean switchValue = true;
        liveDataSwitch.setValue(switchValue);//设置切换条件值

        liveData3.setValue("liveData3");
        liveData4.setValue("liveData4");



        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
        MutableLiveData<String> liveData5 = new MutableLiveData<>();
        MutableLiveData<String> liveData6 = new MutableLiveData<>();

        //添加 源 LiveData
        mediatorLiveData.addSource(liveData5, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("TAG", "onChanged3: " + s);
                mediatorLiveData.setValue(s);
            }
        });
        //添加 源 LiveData
        mediatorLiveData.addSource(liveData6, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("TAG", "onChanged4: " + s);
                mediatorLiveData.setValue(s);
            }
        });

        //添加观察
        mediatorLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("TAG", "onChanged5: "+s);
                //无论liveData5、liveData6更新，都可以接收到
            }
        });

        liveData5.setValue("liveData5");
        //liveData6.setValue("liveData6");
        //例如，如果界面中有可以从本地数据库或网络更新的 LiveData 对象，则可以向 MediatorLiveData 对象添加以下源：
        //与存储在本地数据库中的数据关联的 liveData5
        //与从网络访问的数据关联的 liveData6

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());//
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}