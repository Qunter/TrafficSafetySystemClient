package com.jxut.trafficsafetysystem.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jxut.trafficsafetysystem.R;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2016/10/10.
 */

public class VerificationActivity extends BaseActivity {

    ProgressDialog progressDialog;

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_verification);
        Button connect = (Button)findViewById(R.id.verification_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                toNew();
            }
        });
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在验证，请稍等...");
        progressDialog.show();
    }
    private void toNew(){
        Intent intent = new Intent(VerificationActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
