package com.yiwen.weshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yiwen.weshop.R;
import com.yiwen.weshop.bean.Favorite;
import com.yiwen.weshop.bean.Wares;

import java.util.List;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/6/4
 * Time: 10:20
 */

public class FavoritesAdapter extends SimpleAdapter<Favorite> {
    private OnFavoriteRemovedListener mListener;
    public FavoritesAdapter(Context context, List<Favorite> datas,OnFavoriteRemovedListener listener) {
        super(context, datas, R.layout.template_favorite);
        this.mListener=listener;
    }

    @Override
    public void bindData(BaseViewHolder holder, Favorite favorite) {
        final Wares wares = favorite.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.id_sdv);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.id_tv_title).setText(wares.getName());
        holder.getTextView(R.id.id_tv_price).setText("￥ " + wares.getPrice());

        Button buttonRemove = holder.getButton(R.id.id_btn_remove);
        Button buttonLike = holder.getButton(R.id.id_btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRemove(wares);
                }
            }
        });
    }

    public interface OnFavoriteRemovedListener {
        void onRemove(Wares ware);
    }
}
