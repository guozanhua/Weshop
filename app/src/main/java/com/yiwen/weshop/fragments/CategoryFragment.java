package com.yiwen.weshop.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.yiwen.weshop.Contants;
import com.yiwen.weshop.R;
import com.yiwen.weshop.activity.WareDetailActivity;
import com.yiwen.weshop.adapter.BaseAdapter;
import com.yiwen.weshop.adapter.CategoryAdapter;
import com.yiwen.weshop.adapter.WaresAdapter;
import com.yiwen.weshop.adapter.decoration.DividerGridItemDecoration;
import com.yiwen.weshop.adapter.decoration.DividerItemDecoration;
import com.yiwen.weshop.bean.Banner;
import com.yiwen.weshop.bean.Category;
import com.yiwen.weshop.bean.Page;
import com.yiwen.weshop.bean.Wares;
import com.yiwen.weshop.http.BaseCallback;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.http.SpotsCallback;
import com.yiwen.weshop.utils.ToastUtils;

import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


public class CategoryFragment extends BaseFragment {
    private static String TAG = "CategoryFragment";

    @ViewInject(R.id.id_recycle_view)
    private RecyclerView          mCategoryRecycleView;
    @ViewInject(R.id.id_recycle_wares)
    private RecyclerView          mWaresRecycleView;
    @ViewInject(R.id.id_slider)
    private SliderLayout          mSliderLayout;
    @ViewInject(R.id.id_refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter    mWaresAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @Override
    public void init() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestCategoryData();
        requestBannerData();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage < totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "已经没有了哦!", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });

    }

    private void loadMoreData() {
        curPage++;
        state = STATE_MORE;
        requestWares(categoryId);
    }

    private long categoryId = 0;

    private void refreshData() {
        curPage = 1;
        state = STATE_REFRESH;
        requestWares(categoryId);
    }

    private int curPage   = 1;
    private int pageSize  = 10;
    private int totalPage = 1;
    private int state     = STATE_NORMAL;

    private static final int STATE_NORMAL  = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE    = 2;

    private void requestWares(long categoryId) {
        String url = Contants.API.WARES_LIST + "?"
                + "categoryId=" + categoryId + "&"
                + "curPage=" + curPage + "&"
                + "pageSize=" + pageSize;
        ;
        mOkHttpHelper.doGet(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onRequestBefor(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                curPage = waresPage.getCurrentPage();
                pageSize = waresPage.getPageSize();
                totalPage = waresPage.getTotalPage();
                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void showWaresData(List<Wares> wares) {
        switch (state) {
            case STATE_NORMAL:
                if (mWaresAdapter == null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);
                    mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(View v, int position) {
                            Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                            intent.putExtra(Contants.API.WARE_ITEM, mWaresAdapter.getItem(position));startActivity(intent);
                        }
                    });
                    mWaresRecycleView.setAdapter(mWaresAdapter);
                    mWaresRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mWaresRecycleView.setItemAnimator(new DefaultItemAnimator());
                    mWaresRecycleView.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    /*
                    * 加载新的item内容
                    * */
                    //                    mWaresAdapter.clearAll();
                    //                    mWaresAdapter.addData(wares);
                    mWaresAdapter.refreshData(wares);
                    mWaresRecycleView.scrollToPosition(0);
                }

                break;
            case STATE_REFRESH:
                mWaresAdapter.refreshData(wares);
                mWaresRecycleView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWaresAdapter.addData(wares);
                //                mWaresRecycleView.scrollToPosition(mCategoryAdapter.getDataSize());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void requestBannerData() {
        mOkHttpHelper.doGet(Contants.API.BANNER_HOME, new SpotsCallback<List<Banner>>(getActivity()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderView(banners);
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {

            }
        });
    }

    private void showSliderView(List<Banner> banners) {
        if (banners != null) {
            for (Banner banner : banners) {
                DefaultSliderView defaultSliderView = new DefaultSliderView(this.getActivity());
                defaultSliderView.image(banner.getImgUrl());
                defaultSliderView.description(banner.getName());
                defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        // TODO: 2017/6/4 跳转活动页面
                        ToastUtils.show(getActivity(),"跳转至"+slider.getDescription());
                    }
                });
                mSliderLayout.addSlider(defaultSliderView);
                //        mSliderLayout.setCustomIndicator(mIndicator);
                mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mSliderLayout.setCustomAnimation(new DescriptionAnimation());
                mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
                mSliderLayout.setDuration(3000);
            }
        }
    }

    private void requestCategoryData() {
        mOkHttpHelper.doGet(Contants.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                if (categories != null && categories.size() > 0) {
                    categoryId = categories.get(0).getId();
                    requestWares(categoryId);
                }
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {

            }
        });

    }

    private void showCategoryData(List<Category> datas) {
        mCategoryAdapter = new CategoryAdapter(getContext(), datas);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Category category = mCategoryAdapter.getItem(position);
                categoryId = category.getId();
                curPage = 1;
                state = STATE_REFRESH;
                requestWares(categoryId);
            }
        });
        Log.d(TAG, "showCategoryData: " + mCategoryAdapter + "**" + mCategoryRecycleView);
        mCategoryRecycleView.setAdapter(mCategoryAdapter);
        mCategoryRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCategoryRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onStop() {
        if (mSliderLayout != null)
            mSliderLayout.stopAutoCycle();
        super.onStop();
    }



}
