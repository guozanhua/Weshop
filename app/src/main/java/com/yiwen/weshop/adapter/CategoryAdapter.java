package com.yiwen.weshop.adapter;

import android.content.Context;

import com.yiwen.weshop.R;
import com.yiwen.weshop.bean.Category;

import java.util.List;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/6/1
 * Time: 15:54
 */

public class CategoryAdapter extends SimpleAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, datas, R.layout.template_signle_text);
    }

    @Override
    public void bindData(BaseViewHolder holder, Category category) {
        holder.getTextView(R.id.id_text_view).setText(category.getName());

    }

}
