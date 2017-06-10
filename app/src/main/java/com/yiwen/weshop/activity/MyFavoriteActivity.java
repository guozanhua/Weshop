package com.yiwen.weshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiwen.weshop.Contants;
import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.R;
import com.yiwen.weshop.adapter.BaseAdapter;
import com.yiwen.weshop.adapter.FavoritesAdapter;
import com.yiwen.weshop.adapter.decoration.DividerItemDecoration;
import com.yiwen.weshop.bean.Favorite;
import com.yiwen.weshop.bean.User;
import com.yiwen.weshop.bean.Wares;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.http.SpotsCallback;
import com.yiwen.weshop.msg.BaseRespMsg;
import com.yiwen.weshop.utils.ToastUtils;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class MyFavoriteActivity extends BaseActivity implements FavoritesAdapter.OnFavoriteRemovedListener {
    @ViewInject(R.id.id_toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.id_recycle_view)
    private RecyclerView mRecyclerview;

    private FavoritesAdapter mAdapter;

    private OkHttpHelper mHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        x.view().inject(this);

        init();
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setOnLeftButtonClickListener(new MyToolBar.OnLeftButtonClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        int userId = MyApplication.getInstance().getUser().getId();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", "" + userId);

        mHelper.doGet(Contants.API.FAVORITE_LIST, params, new SpotsCallback<List<Favorite>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorite> data) {
                showData(data);
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {

            }
        });
    }

    private void showData(final List<Favorite> datas) {
        if (mAdapter == null) {
            mAdapter = new FavoritesAdapter(this, datas, this);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    Intent intent = new Intent(MyFavoriteActivity.this, WareDetailActivity.class);
                    intent.putExtra(Contants.API.WARE_ITEM, datas.get(position).getWares());
                    startActivity(intent);
                }
            });
        } else {
            mAdapter.refreshData(datas);
        }
    }

    @Override
    public void onRemove(Wares ware) {
        deleteFavorite(ware);
    }

    private void deleteFavorite(Wares wares) {
        User user = MyApplication.getInstance().getUser();
        int userId = user.getId();

        Map<String, String> params = new HashMap<>();
        params.put("user_id", "" + userId);
        params.put("ware_id", "" + wares.getId());

        mHelper.doPost(Contants.API.FAVORITE_DELETE, params, new SpotsCallback<BaseRespMsg>
                (this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg favorites) {
                ToastUtils.show(MyFavoriteActivity.this, "删除成功");
                init();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
