package com.yiwen.weshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yiwen.weshop.R;
import com.yiwen.weshop.bean.Wares;
import com.yiwen.weshop.utils.CartProvider;

import java.util.List;




public class HotWaresAdapter extends SimpleAdapter<Wares> {

    private CartProvider mProvider;

    public HotWaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_hot_wares);
        mProvider = CartProvider.getInstance(context);
    }

    @Override
    public void bindData(BaseViewHolder holder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.id_drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.id_tv_title).setText(wares.getName());
        holder.getTextView(R.id.id_tv_price).setText("￥" + wares.getPrice());
        Button button = holder.getButton(R.id.id_btn_add);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProvider.put(wares);
                    Toast.makeText(mContext, "已经添加到购物车", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
