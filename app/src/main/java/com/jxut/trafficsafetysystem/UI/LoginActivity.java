package com.jxut.trafficsafetysystem.UI;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.jxut.trafficsafetysystem.R;

/**
 * Created by luyufa on 2016/10/10.
 * 登录页面
 */

public class LoginActivity extends BaseActivity {

    private String accent;
    private String password;

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_login);
        final TextInputLayout accentLayout = (TextInputLayout)findViewById(R.id.login_account);
        final TextInputLayout passwordLayout = (TextInputLayout)findViewById(R.id.login_password);
        Button signIn = (Button)findViewById(R.id.login_signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accent = accentLayout.getEditText().getText().toString();
                password = passwordLayout.getEditText().getText().toString();
                if(isSignIn(accent,password)){
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,SettingActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });

    }

    private Boolean isSignIn(String accent,String password){
        return true;
    }
}
