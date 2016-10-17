package com.jxut.trafficsafetysystem.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxut.trafficsafetysystem.R;

/**
 * Created by luyufa on 2016/10/16.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    TextView title;
    TextView url;
    LinearLayout cardView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.item_title);
        url = (TextView)itemView.findViewById(R.id.item_url);
        cardView = (LinearLayout)itemView.findViewById(R.id.item_text);
    }
}
