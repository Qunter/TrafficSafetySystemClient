package com.jxut.trafficsafetysystem.UI;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jxut.trafficsafetysystem.Adapter.RecyclerViewAdapter;
import com.jxut.trafficsafetysystem.R;
import com.jxut.trafficsafetysystem.Util.GetMusic;
import com.jxut.trafficsafetysystem.Util.SPManger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyufa on 2016/10/16.
 *
 */
public class MusciActivity  extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout swipeRefreshLayou;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x101){
                swipeRefreshLayou.setRefreshing(true);
            }
        }
    };


    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_music);
        swipeRefreshLayou = (SwipeRefreshLayout)findViewById(R.id.music_swipeRefresh);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.music_recycler);
        swipeRefreshLayou.setOnRefreshListener(this);

        ArrayList<HashMap<String, Object>> musicList = new GetMusic().scanAllAudioFiles(this);

        final List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        for(int i = 0;i<musicList.size();i++){
            Map<String,Object> musicMap = musicList.get(i);
            HashMap<String,Object> map = new HashMap<String ,Object>();
            map.put("musicTitle",(String)musicMap.get("musicTitle"));
            map.put("musicFileUrl",(String)musicMap.get("musicFileUrl"));
            list.add(map);
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, this);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SPManger spManger = new SPManger(MusciActivity.this,"MusicWarring");
                spManger.put("music",(String)list.get(position).get("musicFileUrl"));
                Snackbar.make(view,"已经将" + (String)list.get(position).get("musicTitle") + "设置为提醒音乐",Snackbar.LENGTH_SHORT).setAction(" 确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayou.setRefreshing(false);
        handler.sendEmptyMessage(0x101);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}
