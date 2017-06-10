package com.yiwen.weshop.weiget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiwen.weshop.R;

/**
 * User: Yiwen(https://github.com/yiwent)
 * Date: 2017-05-02
 * Time: 11:00
 *
 */
public class MyToolBar extends Toolbar {

    private LayoutInflater mInflater;
    private View           mView;
    private TextView       toolbar_title;
    private EditText       toolbar_searchview;
    private ImageView      toolbar_leftButton;
    private Button         toolbar_rightButton;
    private boolean        showSearchView;
    private Drawable       left_button_icon;
    private Drawable       right_button_icon;
    private String         title;


    public MyToolBar(Context context) {
        this(context, null);
    }

    public MyToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview();
        if (attrs != null) {

            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MyToolBar, defStyleAttr, 0);
            showSearchView = a.getBoolean(R.styleable.MyToolBar_showSearchView, false);
            left_button_icon = a.getDrawable(R.styleable.MyToolBar_leftButtonIcon);
            right_button_icon = a.getDrawable(R.styleable.MyToolBar_rightButtonIcon);
            title = a.getString(R.styleable.MyToolBar_myTitle);
            String rbText = a.getString(R.styleable.MyToolBar_rightButtonText);
            if (!TextUtils.isEmpty(rbText)) {
                setRightButtonText(rbText);
            }

            a.recycle();
        }

        isShouw();

        setContentInsetsRelative(10, 10);

        initListener();

    }

    private void initListener() {
        toolbar_leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftButtonClickListener != null) {
                    onLeftButtonClickListener.onClick(v);
                }
            }
        });

        toolbar_rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightButtonClickListener != null) {
                    onRightButtonClickListener.onClick(v);
                }
            }
        });

    }

    public void isShouw() {
        if (showSearchView) {
            showSearchview();
            hideTitle();
        } else {
            hideSearchview();
            showTitle();
            if (title != null) {
                toolbar_title.setText(title);
            }
        }
       // Log.d("left_button_icon", "initview:"+left_button_icon);
        if (left_button_icon != null) {
            toolbar_leftButton.setVisibility(VISIBLE);
            toolbar_leftButton.setBackground(left_button_icon);
        }

        if (right_button_icon != null) {
            toolbar_rightButton.setVisibility(VISIBLE);
            toolbar_rightButton.setBackground(right_button_icon);
        }

    }

    public interface OnLeftButtonClickListener {
        void onClick(View v);
    }

    public interface OnRightButtonClickListener {
        void onClick(View v);
    }

    private OnLeftButtonClickListener  onLeftButtonClickListener;
    private OnRightButtonClickListener onRightButtonClickListener;

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listener) {
        onLeftButtonClickListener = listener;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listener) {
        onRightButtonClickListener = listener;
    }

    private void initview() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            toolbar_rightButton = (Button) mView.findViewById(R.id.id_btn_right);
            toolbar_title = (TextView) mView.findViewById(R.id.id_tv_title);
            toolbar_searchview = (EditText) mView.findViewById(R.id.id_et_search);
            toolbar_leftButton = (ImageView) mView.findViewById(R.id.id_ib_navigation);
            ActionBar.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(mView, lp);
//            if (showSearchView) {
//                showSearchview();
//                hideTitle();
//            } else {
//                hideSearchview();
//                showTitle();
//                if (title != null) {
//                    toolbar_title.setText(title);
//                }
//            }
//            Log.d("left_button_icon", "initview:5"+left_button_icon);
//            if (left_button_icon != null) {
//
//                toolbar_leftButton.setBackground(left_button_icon);
//                toolbar_leftButton.setVisibility(VISIBLE);
//            }
//
//            if (right_button_icon != null) {
//                toolbar_rightButton.setImageDrawable(right_button_icon);
//                toolbar_rightButton.setVisibility(VISIBLE);
//            }

        }

    }

    @Override
    public void setTitle(@StringRes int resId) {

        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initview();
        if (toolbar_title != null) {
            toolbar_title.setText(title);
            showTitle();
        }
    }

    public void showSearchview() {
        if (toolbar_searchview != null) {
            toolbar_searchview.setVisibility(VISIBLE);
        }
    }

    public void hideSearchview() {
        if (toolbar_searchview != null) {
            toolbar_searchview.setVisibility(GONE);
        }
    }

    public void showTitle() {
        if (toolbar_title != null) {
            toolbar_title.setVisibility(VISIBLE);
        }
    }

    public void hideTitle() {
        if (toolbar_title != null) {
            toolbar_title.setVisibility(GONE);
        }
    }

    /**
     * 设置左右按钮的图标
     *
     * @param d
     */
    public void setLeftButtonIconDrawable(Drawable d) {
        toolbar_leftButton.setImageDrawable(d);
        toolbar_leftButton.setVisibility(VISIBLE);
    }

    public void setRightButtonIconDrawable(Drawable d) {
        toolbar_rightButton.setBackground(d);
        toolbar_rightButton.setVisibility(VISIBLE);
    }

    /**
     * 标题与搜索框的切换
     */
    public void setShowSearchView() {
        hideTitle();
        showSearchview();
    }

    public void setShowTitleView(String title) {
        hideSearchview();
        showTitle();
        toolbar_title.setText(title);
    }

    public void showRightBtn() {
        if (toolbar_rightButton != null) {
            toolbar_rightButton.setVisibility(VISIBLE);
        }
    }

    public void hideRightBtn() {
        if (toolbar_rightButton != null) {
            toolbar_rightButton.setVisibility(GONE);
        }
    }

    public void setRightButtonText(String s) {
        if (toolbar_rightButton != null) {
            showRightBtn();
            toolbar_rightButton.setText(s);
        }
    }
    public Button getRightButton(){
        if (toolbar_rightButton!=null)
        return toolbar_rightButton;
        return null;
    }
    public ImageView getLeftImageView(){
        if (toolbar_leftButton!=null)
            return toolbar_leftButton;
        return null;
    }

    public EditText getSearchEditText(){
        if (toolbar_searchview!=null){
        return toolbar_searchview;
        }
        return null;
    }

}
