package com.yiwen.weshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.yiwen.weshop.Contants;
import com.yiwen.weshop.R;
import com.yiwen.weshop.adapter.HotWaresAdapter;
import com.yiwen.weshop.adapter.SimpleAdapter;
import com.yiwen.weshop.adapter.WaresAdapter;
import com.yiwen.weshop.adapter.decoration.DividerGridItemDecoration;
import com.yiwen.weshop.adapter.decoration.DividerItemDecoration;
import com.yiwen.weshop.bean.Page;
import com.yiwen.weshop.bean.Wares;
import com.yiwen.weshop.utils.PageUtils;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class WareListActivity extends BaseActivity implements SimpleAdapter.OnItemClickListener{
    private static final String TAG = "WareListActivity";

    @ViewInject(R.id.id_refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.id_tab_layout)
    private TabLayout             mTabLayout;
    @ViewInject(R.id.id_tv_summary)
    private TextView              mTvSummary;
    @ViewInject(R.id.id_recycle_view)
    private RecyclerView          mRvWares;
    @ViewInject(R.id.id_toolbar)
    private MyToolBar             mToolbar;

    private int orderBy    = 0;
    private int campaignId = 0;
    private HotWaresAdapter mListWaresAdapter;
    private WaresAdapter    mGridWaresAdapter;
    private PageUtils       mPageUtils;

    private static final int TAG_DEFAULT = 0;
    private static final int TAG_PRICE   = 1;
    private static final int TAG_SALE    = 2;

    private static final int TAG_LIST = 5;
    private static final int TAG_GRID = 6;

    private static int CURRENT_STATE = TAG_LIST;
    private List mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        x.view().inject(this);
        campaignId = getIntent().getIntExtra(Contants.API.CAMPAIGN_ID, 0);
        initToolbar();
        initTab();
        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPageUtils = null;
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });
        mToolbar.setRightButtonIconDrawable(getDrawable(R.mipmap.icon_grid_32));
        mToolbar.showRightBtn();
        mToolbar.getRightButton().setTag(TAG_LIST);
        mToolbar.setOnRightButtonClickListener(new MyToolBar.OnRightButtonClickListener() {
            @Override
            public void onClick(View v) {
                int action = (int) v.getTag();
                if (action == TAG_LIST) {
                    mToolbar.setRightButtonIconDrawable(getDrawable(R.mipmap.icon_list_32));
                    CURRENT_STATE = TAG_GRID;
                    mToolbar.getRightButton().setTag(CURRENT_STATE);
                    loadGridUI(mDatas);
                } else if (action == TAG_GRID) {
                    mToolbar.setRightButtonIconDrawable(getDrawable(R.mipmap.icon_grid_32));
                    CURRENT_STATE = TAG_LIST;
                    mToolbar.getRightButton().setTag(CURRENT_STATE);
                    loadListUI(mDatas);
                }
            }
        });
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                orderBy = (int) tab.getTag();
                mPageUtils.putParam("orderBy", orderBy);
                mPageUtils.request();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void getData() {
        if (mPageUtils == null) {
            mPageUtils = new PageUtils.Builder(this)
                    .setUrl(Contants.API.WARE_CAMPAIN_LIST)
                    .putParam("campaignId", campaignId)
                    .putParam("orderBy", orderBy)
                    .setLoadMore(true)
                    .setRefreshLayout(mRefreshLayout)
                    .setOnPageChangeListener(new PageUtils.OnPageListener() {
                        @Override
                        public void load(List data, int totalPage, int totalCount) {
                            mDatas = data;
                            loadFinished(totalCount);
                        }

                        @Override
                        public void refresh(List data, int totalPage, int totalCount) {
                            mDatas = data;
                            refreshFinished(data);
                        }

                        @Override
                        public void loadMore(List data, int totalPage, int totalCount) {
                            mDatas = data;
                            loadMoreFinished(data);
                        }
                    })
                    .setType(new TypeToken<Page<Wares>>() {
                    }.getType())
                    .build();
        }
        mPageUtils.request();

    }

    private void loadMoreFinished(List data) {
        if (CURRENT_STATE == TAG_LIST) {
            mListWaresAdapter.loadMoreData(data);
        } else if (CURRENT_STATE == TAG_GRID) {
            mGridWaresAdapter.loadMoreData(data);
        }

    }

    private void refreshFinished(List data) {
        if (CURRENT_STATE == TAG_LIST) {
            mListWaresAdapter.refreshData(data);
        } else if (CURRENT_STATE == TAG_GRID) {
            mGridWaresAdapter.refreshData(data);
        }
        mRvWares.scrollToPosition(0);
    }

    private void loadFinished(int totalCount) {
        mTvSummary.setText("共有" + totalCount + "商品");
        if (CURRENT_STATE == TAG_LIST) {
            loadListUI(mDatas);
        } else if (CURRENT_STATE == TAG_GRID) {
            loadGridUI(mDatas);
        }
    }

    private void loadGridUI(List data) {
        if (mGridWaresAdapter == null) {
            mGridWaresAdapter = new WaresAdapter(WareListActivity.this, data);
            mRvWares.addItemDecoration(new DividerGridItemDecoration(this));
            mGridWaresAdapter.setOnItemClickListener(this);
        } else {
            mGridWaresAdapter.refreshData(data);
        }

        mRvWares.setLayoutManager(new GridLayoutManager(this, 2));
        mRvWares.setAdapter(mGridWaresAdapter);
    }

    private void loadListUI(List data) {
        if (mListWaresAdapter == null) {
            mListWaresAdapter = new HotWaresAdapter(WareListActivity.this, data);
            mRvWares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mListWaresAdapter.setOnItemClickListener(this);
        } else {
            mListWaresAdapter.refreshData(data);
        }
        mRvWares.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvWares.setAdapter(mListWaresAdapter);

    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);
        if (CURRENT_STATE == TAG_LIST) {
            intent.putExtra(Contants.API.WARE_ITEM, mListWaresAdapter.getItem(position));
        } else if (CURRENT_STATE == TAG_GRID) {
            intent.putExtra(Contants.API.WARE_ITEM, mGridWaresAdapter.getItem(position));
        }
        startActivity(intent);
    }
}
