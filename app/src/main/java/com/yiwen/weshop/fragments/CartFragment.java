package com.yiwen.weshop.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yiwen.weshop.MainActivity;
import com.yiwen.weshop.R;
import com.yiwen.weshop.activity.CreateOrderActivity;
import com.yiwen.weshop.adapter.CartAdapter;
import com.yiwen.weshop.adapter.decoration.DividerGridItemDecoration;
import com.yiwen.weshop.bean.ShoppingCart;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.utils.CartProvider;
import com.yiwen.weshop.utils.ToastUtils;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;


public class CartFragment extends BaseFragment implements CartAdapter.OnDataUpdateListener,
        View.OnClickListener {
    private static final int ACTION_EDIT     = 1;
    private static final int ACTION_COMPLETE = 2;
    @ViewInject(R.id.id_recycle_view)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.id_cb_all)
    private CheckBox     mCheckBox;
    @ViewInject(R.id.id_tv_total)
    private TextView     mTvTotal;
    @ViewInject(R.id.id_btn_order)
    private Button       mBtnOrder;
    @ViewInject(R.id.id_btn_delete)
    private Button       mBtnDelete;

    private CartAdapter  mAdapter;
    private CartProvider mProvider;

    private MyToolBar mToolbar;
    private OkHttpHelper mHelper = OkHttpHelper.getInstance();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resetToolbar(context);
    }

    public void resetToolbar(final Context context) {
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
        //    mToolbar = (MyToolBar) activity.findViewById(R.id.id_toolbar);
            mToolbar=activity.getMyToolbar();
            if (mToolbar!=null) {
                mToolbar.hideSearchview();
                mToolbar.setTitle(R.string.tab_cart);
                mToolbar.setRightButtonText("编辑");
                mToolbar.showRightBtn();
                mToolbar.getRightButton().setTag(ACTION_EDIT);
                //    mToolbar.setOnRightButtonClickListener(this);
                mToolbar.getRightButton().setOnClickListener(this);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mProvider = CartProvider.getInstance(getActivity());

        return view;
    }

    @Override
    public void init() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showData();
    }

    private void showData() {
        List<ShoppingCart> cars = mProvider.getAll();
        mAdapter = new CartAdapter(getActivity(), cars, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        setTotalPrice();
    }

    public void refreshData() {
        mAdapter.clearAll();
        List<ShoppingCart> carts = mProvider.getAll();
        mAdapter.addData(carts);
        setTotalPrice();
    }

    @Override
    public void onUpdate() {
        setTotalPrice();
    }

    @Override
    public void onChecked(boolean isChecked) {
        mCheckBox.setChecked(isChecked);
    }

    @Event(R.id.id_btn_delete)
    private void deleteCart(View v) {
        mAdapter.deleteCart();
    }

    @Event(R.id.id_btn_order)
    private void toOrder(View v) {
        if (checkCart())
            return;
        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
        startActivity(intent,true);
    }

    private boolean checkCart() {
        if (mAdapter.getDataSize() == 0) {
            ToastUtils.show(getActivity(), "先去购买点物品吧");
            return true;
        }
        if (mProvider.getCheckedWare().size() == 0) {
            ToastUtils.show(getActivity(), "先选择要结算的商品哦");
            return true;
        }
        return false;
    }

    @Event(type = View.OnClickListener.class, value = R.id.id_cb_all)
    private void changeAllStatus(View v) {
        if (mCheckBox.isChecked()) {
            mAdapter.changeItemCheckedStatus(true);
        } else {
            mAdapter.changeItemCheckedStatus(false);
        }
    }

    private void setTotalPrice() {
        mTvTotal.setText("合计 ￥" + mAdapter.getTotalPrice());
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (ACTION_EDIT == action) {
            if (mAdapter.getDataSize() == 0) {
                ToastUtils.show(getActivity(), "先去购买点物品吧");
                return;
            }
            showDelControl();
        } else if (ACTION_COMPLETE == action) {
            hideDelControl();
        }

    }

    private void hideDelControl() {
        mToolbar.setRightButtonText("编辑");
        mTvTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDelete.setVisibility(View.GONE);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mAdapter.changeItemCheckedStatus(true);
        mCheckBox.setChecked(true);
    }

    private void showDelControl() {
        mToolbar.setRightButtonText("完成");
        mTvTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDelete.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);
        mAdapter.changeItemCheckedStatus(false);
        mCheckBox.setChecked(false);
    }
}
