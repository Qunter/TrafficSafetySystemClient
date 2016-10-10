package com.jxut.trafficsafetysystem.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jxut.trafficsafetysystem.CustomView.TasksCompletedView;
import com.jxut.trafficsafetysystem.R;
import com.jxut.trafficsafetysystem.UI.BaseActivity;

/**
 * Created by Administrator on 2016/10/10.
 */

public class VerificationActivity extends BaseActivity {

    @Override
    public void initVariables() {
        super.initVariables();
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_verification);
        Button connect = (Button)findViewById(R.id.verification_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(VerificationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    private void showProgressDialog(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在验证，请稍等...");
        progressDialog.show();
    }
}
