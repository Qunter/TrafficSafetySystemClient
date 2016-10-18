package com.jxut.trafficsafetysystem.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jxut.trafficsafetysystem.R;


/**
 * Created by Administrator on 2016/10/10.
 */

public class ShowActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        final Button select = (Button)findViewById(R.id.show_select);
        final Button exit = (Button)findViewById(R.id.show_exit);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    private void select(){
        Intent intent = new Intent();
        intent.setClass(ShowActivity.this, MusciActivity.class);
        startActivity(intent);
    }
    private void exit(){
        ShowActivity.this.finish();
    }
}
