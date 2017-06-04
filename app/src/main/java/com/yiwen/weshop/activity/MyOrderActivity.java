package com.yiwen.weshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiwen.weshop.Contants;
import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.R;
import com.yiwen.weshop.adapter.BaseAdapter;
import com.yiwen.weshop.adapter.MyOrderAdapter;
import com.yiwen.weshop.adapter.decoration.CardViewtemDecortion;
import com.yiwen.weshop.bean.Order;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.http.SpotsCallback;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        MyOrderAdapter.JumpToDeatilActivity{
    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单
    private int status = STATUS_ALL;

    @ViewInject(R.id.id_toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.id_tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private MyOrderAdapter mAdapter;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        x.view().inject(this);

        initToolbar();
        init();
    }

    private void init() {
        TabLayout.Tab tab = mTablayout.newTab();

        tab.setTag(STATUS_ALL);
        tab.setText("全部");
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setTag(STATUS_SUCCESS);
        tab.setText("已支付");
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setTag(STATUS_PAY_WAIT);
        tab.setText("待支付");
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setTag(STATUS_PAY_FAIL);
        tab.setText("支付失败");
        mTablayout.addTab(tab);

        mTablayout.setOnTabSelectedListener(this);
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void getOrders() {
        int userId = MyApplication.getInstance().getUser().getId();

        Map<String, String> params = new HashMap<>();

        params.put("user_id", "" + userId);
        params.put("status", "" + status);

        okHttpHelper.doGet(Contants.API.ORDER_LIST, params, new SpotsCallback<List<Order>>(this) {

            @Override
            public void onSuccess(Response response, List<Order> order) {
                showOrders(order);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showOrders(List<Order> orders) {
        if (mAdapter == null) {
            mAdapter = new MyOrderAdapter(this, orders, this);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());

            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    toDetailActivity(position);
                }
            });
        } else {
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }

    @Override
    public void jump(int position) {
        toDetailActivity(position);
    }

    private void toDetailActivity(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);

        Order order = mAdapter.getItem(position);
        intent.putExtra("order", order);
        startActivity(intent, true);
    }
}
