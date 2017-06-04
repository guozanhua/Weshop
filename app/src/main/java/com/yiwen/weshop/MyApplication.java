package com.yiwen.weshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.yiwen.weshop.bean.User;
import com.yiwen.weshop.utils.UserLocalData;

import org.xutils.x;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/5/27
 * Time: 20:46
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .build();
        Fresco.initialize(this, config);
        x.Ext.init(this);
        sInstance = this;
        initUser();
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    private User mUser;

    private void initUser() {
        this.mUser = UserLocalData.getUser(this);
    }

    public User getUser() {
        return mUser;
    }


    public void putUser(User user, String token) {
        this.mUser = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUser() {
        this.mUser = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken() {
        return UserLocalData.getToken(this);
    }

    private Intent intent;

    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context) {
        context.startActivity(intent);
        this.intent = null;
    }

}
