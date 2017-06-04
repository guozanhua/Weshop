package com.yiwen.weshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yiwen.weshop.R;
import com.yiwen.weshop.bean.Wares;

import java.util.List;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/6/1
 * Time: 16:12
 */

public class WaresAdapter extends SimpleAdapter<Wares> {
    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_grid_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.id_drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.id_tv_title).setText(wares.getName());
        holder.getTextView(R.id.id_tv_price).setText("￥" + wares.getPrice());
    }
}
