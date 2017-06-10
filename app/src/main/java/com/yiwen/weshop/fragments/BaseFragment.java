package com.yiwen.weshop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.activity.LoginActivity;
import com.yiwen.weshop.bean.User;
import com.yiwen.weshop.utils.CommonUtils;
import com.yiwen.weshop.utils.ToastUtils;

import org.xutils.x;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        x.view().inject(this, view);
        initToolBar();
        init();
        checkNetword();
        return view;
    }

    private void checkNetword() {
        if (!CommonUtils.isNetworkAvailable(getActivity())){
            ToastUtils.show(getActivity(),"网络未连接，请打开网络");
        }
    }

    public void initToolBar() {

    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) {
            User user = MyApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {
                MyApplication.getInstance().putIntent(intent);
                Intent i = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(i);
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
                Intent i = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(intent);
            }
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }
}
