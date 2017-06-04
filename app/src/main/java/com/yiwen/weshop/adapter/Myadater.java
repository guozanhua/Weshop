package com.yiwen.weshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yiwen (https://github.com/yiwent)
 * Date:2017/5/27
 * Time: 18:32
 */

public class Myadater extends RecyclerView.Adapter<Myadater.viewHoder> {


    public Myadater() {

    }

    @Override
    public viewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(viewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class viewHoder extends RecyclerView.ViewHolder{

        public viewHoder(View itemView) {
            super(itemView);
        }
    }
}
