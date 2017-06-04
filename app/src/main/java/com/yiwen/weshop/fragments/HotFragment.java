package com.yiwen.weshop.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.yiwen.weshop.Contants;
import com.yiwen.weshop.R;
import com.yiwen.weshop.activity.WareDetailActivity;
import com.yiwen.weshop.adapter.BaseAdapter;
import com.yiwen.weshop.adapter.HotWaresAdapter;
import com.yiwen.weshop.adapter.decoration.DividerItemDecoration;
import com.yiwen.weshop.bean.Page;
import com.yiwen.weshop.bean.Wares;
import com.yiwen.weshop.utils.PageUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

public class HotFragment extends BaseFragment {

    private HotWaresAdapter mAdapter;
    @ViewInject(R.id.recyclerview_hot)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.refresh_hot)
    private MaterialRefreshLayout mRefreshLayout;

    private List<Wares> mDatas;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        return view;
    }

    @Override
    public void init() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAndShowData();
    }
    private void getAndShowData() {
        PageUtils pageUtils = PageUtils.newBuilder(getContext())
                .setUrl(Contants.API.HOT_WARES)
                .setLoadMore(true)
                .setOnPageChangeListener(new PageUtils.OnPageListener() {
                    @Override
                    public void load(final List data, int totalPage, int totalCount) {
                        mDatas = data;
                        loadFinished();
                    }

                    @Override
                    public void refresh(final List data, int totalPage, int totalCount) {
                        mDatas = data;
                        refreshFinished();

                    }

                    @Override
                    public void loadMore(final List data, int totalPage, int totalCount) {
                        mDatas = data;
                        loadMoreFinished();
                    }
                })
                .setPageSize(10)
                .setRefreshLayout(mRefreshLayout)
                .setType(new TypeToken<Page<Wares>>() {}.getType())
                .build();
        pageUtils.request();
    }

    private void loadFinished() {
        mAdapter = new HotWaresAdapter(getContext(), mDatas);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Wares item = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.API.WARE_ITEM, item);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void refreshFinished() {
        mAdapter.refreshData(mDatas);
        mRecyclerView.scrollToPosition(0);
    }

    private void loadMoreFinished() {
        mAdapter.loadMoreData(mDatas);
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
    }

}
