package com.yiwen.weshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.bean.User;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/6/3
 * Time: 20:20
 */

public class BaseActivity extends AppCompatActivity {
    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) {
            User user = MyApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {
                MyApplication.getInstance().putIntent(intent);
                Intent i = new Intent(this, LoginActivity.class);
                super.startActivity(intent);
            }
        } else {
            super.startActivity(intent);
        }
    }

    public void startActivityForResult(Intent intent, int requestCode, boolean isNeedLogin) {
        if (isNeedLogin) {
            User user = MyApplication.getInstance().getUser();
            if (user != null) {
                super.startActivityForResult(intent, requestCode);
            } else {
                MyApplication.getInstance().putIntent(intent);
                Intent i = new Intent(this, LoginActivity.class);
                super.startActivity(intent);
            }
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }
}
