package com.jxut.trafficsafetysystem.UI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jxut.trafficsafetysystem.R;

public class MainActivity extends ActionBarActivity {

    private EditText editText;
    private Button button;
    // 输出流
    private BufferedReader in;
    //输出流
    private OutputStream out;
    // 客户端socket
    private Socket socket;
    // 显示数据
    private TextView textView;
    // 用来存放要传递给客户端的数据
    private static String data;
    //handler发送处理消息
    private Handler handler;
    private String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.send);
        textView = (TextView) findViewById(R.id.textView);
        //设置textView可以滚动
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0x1314) {
                    textView.append("服务器：" + b + '\n');
                    Log.e("--->", "" + b);
                }
                return true;
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // 10.200.30.141.
                    socket = new Socket("10.200.30.141", 5000);
                    Log.e("--->", "已发出链接请求");
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = socket.getOutputStream();
                    while (socket != null) {
                        b = in.readLine();
//							textView.append("服务器：" + b + '\n');
                        handler.sendEmptyMessage(0x1314);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                data = editText.getText().toString();
                textView.append("移动端说：" + data + '\n');
                editText.setText("");
                try {
                    if (socket != null) {
                        out.write((data + '\n').getBytes("GBK"));
                        out.flush();
                    } else
                        Log.e("--->", "服务器未连接，无法获得输出流");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
