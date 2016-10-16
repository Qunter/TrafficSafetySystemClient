package com.jxut.trafficsafetysystem.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Administrator on 2016/10/14.
 */

public class ReatView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder sfh;
    private Thread th;
    private Canvas canvas;
    private Paint paint;
    private int newcount;
    private Path paths1;
    private Path paths2;
    // 输出流
    private BufferedReader in;
    // 客户端socket
    private Socket socket;
    //handler发送处理消息
    private String b;
    private int ScreenW;
    private int ScreenH;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x1314) {
                draw(b);
            }
            return true;
        }
    });

    public ReatView(Context context) {
        super(context);
        initViews();
    }

    public ReatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ReatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        th = new Thread(this);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        b = null;
        newcount = 0;
        paths1 = new Path();
        paths2 = new Path();
        this.setKeepScreenOn(true);// 保持屏幕常亮
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ScreenW = this.getWidth();// 获取屏幕宽度
        ScreenH = this.getHeight();  // 获取屏幕高度
        try{
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.WHITE);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交
        }
        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            socket.setKeepAlive(false);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.1.101", 5000);
            Log.e("--->", "已发出链接请求");
            if (!socket.getKeepAlive()){
                socket.setKeepAlive(true);
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (socket != null) {
                try {
                    b = in.readLine();
                    if (b.equals("")||b.equals(null)){
                        b = "  0099.0000  0099.0000";
                    }
                    handler.sendEmptyMessage(0x1314);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private void draw(String str) {
        try {
            String x;
            String y;
            canvas = sfh.lockCanvas(); // 得到一个canvas实例
            canvas.drawColor(Color.WHITE);// 刷屏
            //绘制实时脑电
            if (!str.equals(null)&str.length() > 23) {
                x = str.substring(0, 6).trim();
                y = str.substring(13,18).trim();
                Log.e("--->", "已得到数据x:" + x + "和y:" + y);
            } else {
                return;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            //绘制波形图
            if (newcount == 0) {
                paths1.reset();
                paths1.moveTo(100, 100);
            }
            paths1.lineTo(101 + newcount,101 + (Integer.parseInt(x)%1000) % 100);
            canvas.drawPath(paths1, paint);
            if(newcount == 0){
                paths2.reset();
                paths2.moveTo(100, 250);
            }
            paths2.lineTo(101 + newcount,251 + ((Integer.parseInt(y)%10000)%1000)%100);
            canvas.drawPath(paths2,paint);
            newcount = (newcount + 4) % 900;


        } catch (Exception ex) {
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交
        }
    }
}
