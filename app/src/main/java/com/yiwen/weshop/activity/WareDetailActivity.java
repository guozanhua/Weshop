package com.yiwen.weshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sharesdk.onekeyshare.OnekeyShare;
import com.yiwen.weshop.Contants;
import com.yiwen.weshop.MyApplication;
import com.yiwen.weshop.R;
import com.yiwen.weshop.bean.User;
import com.yiwen.weshop.bean.Wares;
import com.yiwen.weshop.http.OkHttpHelper;
import com.yiwen.weshop.http.SpotsCallback;
import com.yiwen.weshop.msg.BaseRespMsg;
import com.yiwen.weshop.utils.CartProvider;
import com.yiwen.weshop.utils.ToastUtils;
import com.yiwen.weshop.weiget.MyToolBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

public class WareDetailActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "WareDetailActivity";
    @ViewInject(R.id.id_web_view)
    private WebView mWebView;

    @ViewInject(R.id.id_toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.id_refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;
    private Wares                 mWare;
    private WebAppInterface       mWebAppInterface;
    private CartProvider          mCartProvider;

    private SpotsDialog mDialog;
    private boolean     isCanRefresh;
    private OkHttpHelper mHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        x.view().inject(this);
        mWare = (Wares) getIntent().getSerializableExtra(Contants.API.WARE_ITEM);
        if (mWare == null) {
            this.finish();
        }
        mCartProvider = CartProvider.getInstance(this);
        mDialog = new SpotsDialog(this, "正在加载");
        mDialog.show();
        initToolbar();
        initWebView();
        initRefreshLayout();
        ShareSDK.initSDK(this);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if (isCanRefresh) {
                    mDialog.show();
                    mWebView.loadUrl(Contants.API.WARE_DETAIL);
                }
            }
        });
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        /*使webview能够加载图片*/
        webSettings.setBlockNetworkImage(false);
        webSettings.setAppCacheEnabled(true);

        mWebView.loadUrl(Contants.API.WARE_DETAIL);
        mWebAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mWebAppInterface, "appInterface");
        mWebView.setWebViewClient(new MyWebChromeC());
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.showRightBtn();
        mToolbar.getRightButton().setText("分享");
        mToolbar.setOnRightButtonClickListener(new MyToolBar.OnRightButtonClickListener() {
            @Override
            public void onClick(View v) {
                WareDetailActivity.this.showShare();
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("XX商城");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.XXX.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mWare.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数,优先级大于setImageUrl
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWare.getDescription());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(mWare.getName());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.XXXX.com");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }


    class WebAppInterface {
        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail(" + mWare.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ");
                    addFavorite1();
                }
            });

        }

        @JavascriptInterface
        public void addFavorite(long id) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  //  addFavorite1();
                }
            });

        }

        @JavascriptInterface
        public void addToCart(long id) {
            mCartProvider.put(mWare);
            Toast.makeText(mContext, "成功添加到购物车", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFavorite1() {
        User user = MyApplication.getInstance().getUser();
        if (user==null){
            ToastUtils.show(WareDetailActivity.this,"请登录后收藏");
            Intent intent=new Intent(WareDetailActivity.this,LoginActivity.class);
            startActivity(intent);
            return;
        }
        int userId = user.getId();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", "" + userId);
        params.put("ware_id", "" + mWare.getId());
        Log.d(TAG, "addFavorite1: userId"+userId+"ware_id"+mWare.getId());
//        mHelper.doPost(Contants.API.FAVORITE_CREATE, params, new SpotsCallback<List<Favorite>>
//                (WareDetailActivity.this) {
//            @Override
//            public void onSuccess(Response response, List<Favorite> favorites) {
//                ToastUtils.show(WareDetailActivity.this, "已添加到收藏夹");
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                Log.d(TAG, "onError: addFavorite1");
//            }
//        });
        mHelper.doPost(Contants.API.FAVORITE_CREATE, params,
                new SpotsCallback<BaseRespMsg>(WareDetailActivity.this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                Log.d(TAG, "onSuccess:Status: "+baseRespMsg.getStatus()+"Message:"+baseRespMsg.getMessage());
                if (BaseRespMsg.STATUS_SUCCESS==baseRespMsg.getStatus()){
                    ToastUtils.show(WareDetailActivity.this, "已添加到收藏夹");
                }else if (405==baseRespMsg.getStatus()){
                    ToastUtils.show(WareDetailActivity.this, "服务器不行了。。。");
                }
            }

            @Override
            public void onError(Response response, int errCode, Exception e) {
                ToastUtils.show(WareDetailActivity.this, "添加收藏夹失败");
            }
        });
    }

    class MyWebChromeC extends WebViewClient {
        /*加载网页代码时，逐行回调该方法*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mWebAppInterface.showDetail();
            if (!isCanRefresh)
                isCanRefresh = true;
            else
                mRefreshLayout.finishRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

}
