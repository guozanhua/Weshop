package com.yiwen.weshop.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.wallet.core.utils.Md5Utils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.reflect.TypeToken;
import com.yiwen.weshop.Contants;
import com.yiwen.weshop.R;
import com.yiwen.weshop.activity.WareListActivity;
import com.yiwen.weshop.adapter.HomeCategoryAdapter;
import com.yiwen.weshop.adapter.decoration.CardViewtemDecortion;
import com.yiwen.weshop.bean.Banner;
import com.yiwen.weshop.bean.Campaign;
import com.yiwen.weshop.bean.HomeCampaign;
import com.yiwen.weshop.http.BaseCallback;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.http.SpotsCallback;
import com.yiwen.weshop.utils.JSONUtil;
import com.yiwen.weshop.utils.PreferencesUtils;
import com.yiwen.weshop.utils.ToastUtils;

import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/5/1
 * Time: 23:34
 */
public class HomeFragment extends BaseFragment {
    SliderLayout   mSliderLayout;
    PagerIndicator mPagerIndicator;
    RecyclerView   mRecyclerView;
    private OkHttpHelper mHelper = OkHttpHelper.getInstance();
    private List<Banner>        mBanners;
    private List<HomeCampaign>  mHomeCampaigns;
    private HomeCategoryAdapter mAdapter;
    @ViewInject(R.id.id_refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        return view;
    }

    @Override
    public void init() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                requestSlideImage();
                requestRecyecleView();
                mRefreshLayout.finishRefreshing();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                requestSlideImage();
                requestRecyecleView();
                mRefreshLayout.finishRefreshLoadMore();
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checLocalData();

        requestSlideImage();

        requestRecyecleView();
    }

    private void checLocalData() {
        String banners = PreferencesUtils.getString(getActivity(),
                Md5Utils.toMD5 (Contants.API.BANNER_HOME), null);
        String homeCampaigns = PreferencesUtils.getString(getActivity(),
                Md5Utils.toMD5(Contants.API.CAMPAIGN_HOME), null);
        if (!TextUtils.isEmpty(banners) && !TextUtils.isEmpty(homeCampaigns)) {
            mBanners = JSONUtil.fromJson(banners, new TypeToken<List<Banner>>() {
            }.getType());
            initSlider();
            mHomeCampaigns = JSONUtil.fromJson(homeCampaigns, new TypeToken<List<HomeCampaign>>() {
            }.getType());
            initHomeCampaigns();
            return;
        }
    }


    private void requestSlideImage() {

        mHelper.doGet(Contants.API.BANNER_HOME, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners = banners;
                initSlider();
                PreferencesUtils.putString(getActivity(),
                        Md5Utils.toMD5(Contants.API.BANNER_HOME), JSONUtil.toJSON(banners));
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {

            }
        });
    }

    private void initSlider() {
        if (mBanners != null) {
            for (final Banner banner : mBanners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        // TODO: 2017/6/4 跳转活动页面
                        ToastUtils.show(getActivity(), "跳转至" + slider.getDescription());
                    }
                });
                mSliderLayout.addSlider(textSliderView);
                //        mSliderLayout.setCustomIndicator(mIndicator);
                mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mSliderLayout.setCustomAnimation(new DescriptionAnimation());
                mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
                mSliderLayout.setDuration(3000);

                mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        //      Log.d(TAG, "onPageScrolled: ");
                    }

                    @Override
                    public void onPageSelected(int position) {
                        //     Log.d(TAG, "onPageSelected: ");
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        //        Log.d(TAG, "onPageScrollStateChanged: ");
                    }
                });
            }
        }
    }

    private void requestRecyecleView() {
        mHelper.doGet(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

            @Override
            public void onRequestBefor(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                mHomeCampaigns = homeCampaigns;
                initHomeCampaigns();
                PreferencesUtils.putString(getActivity(),
                        Md5Utils.toMD5(Contants.API.CAMPAIGN_HOME), JSONUtil.toJSON(homeCampaigns));

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

    private void initHomeCampaigns() {
        if (mAdapter == null) {
            mAdapter = new HomeCategoryAdapter(mHomeCampaigns, getActivity());
            mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
                @Override
                public void onClick(View view, Campaign campaign) {
                    Intent intent = new Intent(getActivity(), WareListActivity.class);
                    intent.putExtra(Contants.API.CAMPAIGN_ID, campaign.getId());
                    startActivity(intent);
                }
            });

            mRecyclerView.addItemDecoration(new CardViewtemDecortion());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSliderLayout != null) {
            mSliderLayout.stopAutoCycle();
        }
    }
}