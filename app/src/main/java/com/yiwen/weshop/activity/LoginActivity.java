package com.yiwen.weshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yiwen.weshop.Contants;
import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.R;
import com.yiwen.weshop.bean.User;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.http.SpotsCallback;
import com.yiwen.weshop.msg.LoginRespMsg;
import com.yiwen.weshop.utils.DESUtil;
import com.yiwen.weshop.utils.ToastUtils;
import com.yiwen.weshop.weiget.ClearEditText;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @ViewInject(R.id.id_et_phone)
    private ClearEditText mEtPhone;
    @ViewInject(R.id.id_et_password)
    private ClearEditText mEtPassWord;
    @ViewInject(R.id.id_btn_login)
    private Button        mBtnLogin;
    @ViewInject(R.id.id_toolbar)
    private MyToolBar     mToolbar;
    @ViewInject(R.id.id_tv_register)
    private TextView      mTvRegister;

    private OkHttpHelper mHelper = OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setOnLeftButtonClickListener(new MyToolBar.OnLeftButtonClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Event(type = View.OnClickListener.class, value = R.id.id_btn_login)
    private void login(View v) {
        String phone = mEtPhone.getText().toString();
        String pwd = mEtPassWord.getText().toString();
        Log.d(TAG, "login: " + phone + ":" + pwd);
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请填写内容");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请输入密码");
        }
        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
        mHelper.doPost(Contants.API.LOGIN, params, new SpotsCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                if (userLoginRespMsg == null) {
                    ToastUtils.show(LoginActivity.this, "用户名或密码错误，请重试");
                    return;
                }
                if (userLoginRespMsg.getData() == null) {
                    ToastUtils.show(LoginActivity.this, "用户名或密码错误，请重试");
                    Log.d(TAG, "onSuccess: " + userLoginRespMsg.getMessage());
                    return;
                }
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                if (application.getIntent() == null) {
                    setResult(RESULT_OK);
                } else {
                    application.jumpToTargetActivity(LoginActivity.this);
                }
                finish();
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {
                ToastUtils.show(LoginActivity.this, "无网络访问，请检查");
            }
        });
    }
    @Event(R.id.id_tv_register)
    private void toRegister(View v) {
        startActivity(new Intent(this, RegActivity.class));
    }

    @Event(R.id.id_tv_forgot)
    private void toForget(View v) {
       ToastUtils.show(this,"忘记密码");
        // TODO: 2017/6/3
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper=null;
    }
}
