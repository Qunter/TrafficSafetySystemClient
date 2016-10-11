package com.jxut.trafficsafetysystem.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jxut.trafficsafetysystem.R;

/**
 * Created by Administrator on 2016/10/10.
 */

public class VerificationActivity extends BaseActivity {


    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_verification);
        Button connect = (Button)findViewById(R.id.verification_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Intent intent = new Intent(VerificationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showProgressDialog(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在验证，请稍等...");
        progressDialog.show();
    }
}
