package com.jxut.trafficsafetysystem.UI;

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
    private Path paths;
    // 输出流
    private BufferedReader in;
    // 客户端socket
    private Socket socket;
    //handler发送处理消息
    private String b;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x1314) {
                if (!b.equals(null)) {
                    draw(b);
                }
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
        newcount = 0;
        paths = new Path();
        this.setKeepScreenOn(true);// 保持屏幕常亮
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
            socket = new Socket("10.200.10.108", 5000);
            Log.e("--->", "已发出链接请求");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (socket != null) {
                try {
                    b = in.readLine();
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
            if (str.length() > 23) {
                x = str.substring(0, 6).trim();
                y = str.substring(6,12).trim();
                Log.e("--->", "已得到数据x:" + x + "和y:" + y);
            } else {
                return;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            //绘制波形图
            if (newcount == 0) {
                paths.reset();
                paths.moveTo(100, 200);
            }
            paths.lineTo(101 + Integer.parseInt(x)-9000, 201 + Integer.parseInt(y)-8000);
            canvas.drawPath(paths, paint);
            newcount = (newcount + 4) % 900;
        } catch (Exception ex) {
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交
        }
    }
}
