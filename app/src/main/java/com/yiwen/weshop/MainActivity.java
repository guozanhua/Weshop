package com.yiwen.weshop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.yiwen.weshop.activity.BaseActivity;
import com.yiwen.weshop.bean.Tab;
import com.yiwen.weshop.fragments.CartFragment;
import com.yiwen.weshop.fragments.CategoryFragment;
import com.yiwen.weshop.fragments.HomeFragment;
import com.yiwen.weshop.fragments.HotFragment;
import com.yiwen.weshop.fragments.MineFragment;
import com.yiwen.weshop.utils.ToastUtils;
import com.yiwen.weshop.weiget.FragmentTabHost;
import com.yiwen.weshop.weiget.MyToolBar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private FragmentTabHost mTabHost;
    private LayoutInflater  mInflater;
    private List<Tab> mTabs = new ArrayList<Tab>(5);
    private CartFragment mCartFragment;
    private MineFragment mMineFragment;
    private MyToolBar    mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        mInflater = LayoutInflater.from(this);
        //
        //        mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        //
        //        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        ////
        //        View view;
        //        view = mInflater.inflate(R.layout.tab_indica_view, null);
        //        TextView textView = (TextView) view.findViewById(R.id.text_view);
        //        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        //        textView.setText("主页");
        //        imageView.setBackgroundResource(R.mipmap.icon_home);
        //        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("home");
        //        tabSpec.setIndicator(view);
        //        mTabHost.addTab(tabSpec, HomeFragment.class, null);
        mToolBar = (MyToolBar) findViewById(R.id.id_toolbar);
        intitab();
        initSeach();
    }

    private void initSeach() {
        // TODO: 2017/6/4  搜索功能 语音搜索
        mToolBar.getSearchEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: 输入"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //按搜索触发
        mToolBar.getSearchEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH)
                    ToastUtils.show(MainActivity.this,"搜索"+v.getText());
                Log.d(TAG, "onEditorAction: "+String.valueOf(actionId));
                return false;
            }
        });
    }

    private void intitab() {
        Tab hometab = new Tab(R.string.tab_home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab hottab = new Tab(R.string.tab_hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab carttab = new Tab(R.string.tab_cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab categorytab = new Tab(R.string.tab_category, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab minetab = new Tab(R.string.tab_mine, R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(hometab);
        mTabs.add(hottab);
        mTabs.add(categorytab);
        mTabs.add(carttab);
        mTabs.add(minetab);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                String cart_fragment = MainActivity.this.getString(R.string.tab_cart);
                String mine_fragment = MainActivity.this.getString(R.string.tab_mine);
                if (tabId == cart_fragment) {
                    MainActivity.this.setCartFragmentUI(cart_fragment);
                } else if (tabId == mine_fragment) {
                    MainActivity.this.setMineFragmentUi(mine_fragment);
                } else {
                    MainActivity.this.restoreToolbar();
                }
            }
        });
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buitIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);

    }

    private void setMineFragmentUi(String s) {
        if (mMineFragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(s);
            if (fragment != null) {
                mMineFragment = (MineFragment) fragment;
                mMineFragment.resetToolbar(MainActivity.this);
            }
        } else {
            mMineFragment.resetToolbar(MainActivity.this);
        }
    }

    private void restoreToolbar() {
        mToolBar.showSearchview();
        mToolBar.hideRightBtn();
        mToolBar.setTitle("");
    }

    private void setCartFragmentUI(String s) {
        if (mCartFragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(s);
            if (fragment != null) {
                mCartFragment = (CartFragment) fragment;
                mCartFragment.refreshData();
                mCartFragment.resetToolbar(MainActivity.this);
            }
        } else {
            mCartFragment.refreshData();
            mCartFragment.resetToolbar(MainActivity.this);
        }
    }

    private View buitIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indica_view, null);
        TextView textView = (TextView) view.findViewById(R.id.id_tab_tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.id_tab_iv);
        textView.setText(tab.getTitle());
        imageView.setBackgroundResource(tab.getIcon());
        return view;
    }
}
