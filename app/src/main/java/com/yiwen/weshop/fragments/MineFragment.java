package com.yiwen.weshop.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yiwen.weshop.Contants;
import com.yiwen.weshop.MainActivity;
import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.R;
import com.yiwen.weshop.activity.AddressListActivity;
import com.yiwen.weshop.activity.LoginActivity;
import com.yiwen.weshop.activity.MyFavoriteActivity;
import com.yiwen.weshop.activity.MyOrderActivity;
import com.yiwen.weshop.bean.User;
import com.yiwen.weshop.utils.ToastUtils;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.hdodenhof.circleimageview.CircleImageView;


public class MineFragment extends BaseFragment {

    @ViewInject(R.id.id_img_header)
    private CircleImageView mCivHeader;
    @ViewInject(R.id.id_tv_username)
    private TextView        mTvUserName;
    @ViewInject(R.id.id_btn_logout)
    private Button          mBtnLogout;

    @ViewInject(R.id.id_tv_my_address)
    private TextView mTvMyAddress;

    @ViewInject(R.id.id_tv_my_order)
    private TextView mTvMyOrder;

    @ViewInject(R.id.id_tv_my_favorites)
    private TextView mTvMyFavorites;

    private MyToolBar mToolbar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resetToolbar(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    @Override
    public void init() {

    }

    private void showUser(User user) {
        if (user != null) {
            loginUI(user);
        } else {
            logoutUI();
        }
    }

    private void logoutUI() {
        mTvUserName.setText(getString(R.string.to_login));
        mCivHeader.setImageDrawable(getResources().getDrawable(R.drawable.default_head, null));
        mBtnLogout.setVisibility(View.GONE);
    }

    private void loginUI(User user) {
        mTvUserName.setText(user.getUsername());
        if (TextUtils.isEmpty(user.getLogo_url()))
            Picasso.with(getActivity()).load(R.drawable.default_head).into(mCivHeader);
        Picasso.with(getActivity()).load(user.getLogo_url()).into(mCivHeader);
        mBtnLogout.setVisibility(View.VISIBLE);
    }

    public void resetToolbar(Context context) {
        if (context instanceof MainActivity) {
            MainActivity mActivity = (MainActivity) context;
            mToolbar = (MyToolBar) mActivity.findViewById(R.id.id_toolbar);
            mToolbar.hideSearchview();
            mToolbar.hideRightBtn();
            mToolbar.setTitle(R.string.tab_mine);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }

    // TODO: 2017/6/4 更换头像
    @Event(type = View.OnClickListener.class, value = {R.id.id_tv_username, R.id.id_img_header})
    private void toLogin(View v) {
        if (MyApplication.getInstance().getUser() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE, false);
        }
    }

    @Event(type = View.OnClickListener.class, value = R.id.id_btn_logout)
    private void logout(View v) {
        MyApplication.getInstance().clearUser();
        logoutUI();
        ToastUtils.show(getActivity(), "您已退出，可重新登录");
    }

    @Event(R.id.id_tv_my_address)
    private void toMyAddress(View v) {
        startActivity(new Intent(getActivity(), AddressListActivity.class),true);
    }

    @Event(R.id.id_tv_my_order)
    private void toMyOrder(View v) {
        startActivity(new Intent(getActivity(), MyOrderActivity.class),true);
    }

    @Event(R.id.id_tv_my_favorites)
    private void toMyFavorites(View v) {
        startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);
    }

    @Event(R.id.id_tv_my_msg)
    private void toMyMassage(View v) {
        ToastUtils.show(getActivity(),"click 我的消息");
    }
}
