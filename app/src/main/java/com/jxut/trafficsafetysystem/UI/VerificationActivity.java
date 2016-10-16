package com.jxut.trafficsafetysystem.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.jxut.trafficsafetysystem.R;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by luyufa on 2016/10/16.
 */
public class VerificationActivity extends BaseActivity {


    private String ip;
    private Socket socket;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x101) {

                Intent intent = new Intent();
                intent.setClass(VerificationActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void initViews() {
        super.initViews();

        setContentView(R.layout.activity_verification);
        Button connect = (Button) findViewById(R.id.verification_connect);
        connect.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           new Thread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   try {
                                                       socket = new Socket("192.168.1.101", 5000);
                                                       handler.sendEmptyMessage(0x101);
                                                   } catch (IOException e) {
                                                       e.printStackTrace();
                                                   } finally {
                                                       try {
                                                           socket.setKeepAlive(false);
                                                       } catch (IOException e) {
                                                           e.printStackTrace();
                                                       }
                                                   }
                                               }
                                           }

                                           ).start();
                                       }
                                   }

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
        try {
            socket.setKeepAlive(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

