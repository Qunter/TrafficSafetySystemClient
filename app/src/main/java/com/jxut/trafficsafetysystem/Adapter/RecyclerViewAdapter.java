package com.jxut.trafficsafetysystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jxut.trafficsafetysystem.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luyufa on 2016/10/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<HashMap<String, Object>> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerViewAdapter(List list, Context context){
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_recycler,null);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final HashMap<String,Object> map = list.get(position);
        holder.title.setText((String)map.get("musicTitle"));
        holder.url.setText((String) map.get("musicFileUrl"));
        if(onItemClickListener!=null){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
