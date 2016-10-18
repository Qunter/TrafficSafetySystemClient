package com.jxut.trafficsafetysystem.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.animation.Animation;

import com.jxut.trafficsafetysystem.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * @author peter
 */
public class Beisaier extends SurfaceView implements Callback, Runnable {// 备注1


    private SurfaceHolder sfh;
    private Thread th;
    private Canvas canvas;
    private Paint paint;
    private Random rand;
    private float len[][], previous[], next[];
    private int radius;//园的半径
    private int count, newcount, index;//计数器
    private int num;//圆形曲线的编号
    private Path paths1;
    private Path paths2;
    private BufferedReader in;
    // 客户端socket
    private Socket socket;
    //handler发送处理消息
    private String b;
    private String text;
    private int ScreenW;
    private int ScreenH;
    private Context context;
    private MediaPlayer player;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x1314) {
                draw(b);
            }
            return true;
        }
    });

    public Beisaier(Context context) {
        super(context);
        init(context);
    }


    public Beisaier(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Beisaier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        th = new Thread(this);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        b = null;
        text = "0";
        newcount = 0;
        paths1 = new Path();
        paths2 = new Path();
        rand = new Random();
        previous = new float[8];
        num = -1;
        next = new float[8];
        len = new float[8][5];
        radius = 360;
        for (int i = 0; i < 8; i++) {
            next[i] = radius;
        }
        count = 0;
        index = 0;
        this.setKeepScreenOn(true);// 保持屏幕常亮
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ScreenW = this.getWidth();// 获取屏幕宽度
        ScreenH = this.getHeight();  // 获取屏幕高度
        Log.e("------>>>>", "width" + ScreenW);
        Log.e("------>>>>", "height" + ScreenH);
        try {
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

    private void draw(String str) {
        try {
            String x;
            String y;
            canvas = sfh.lockCanvas(); // 得到一个canvas实例
            canvas.drawColor(Color.WHITE);// 刷屏
            //绘制实时脑电
            if (!str.equals(null) & str.length() > 23) {
                x = str.substring(0, 6).trim();
                y = str.substring(13, 18).trim();
                Log.e("--->", "已得到数据x:" + x + "和y:" + y);
            } else {
                return;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(5);
            //绘制波形图
            if (newcount == 0) {
                paths1.reset();
                paths1.moveTo(100, 100);
            }
            paths1.lineTo(101 + newcount, 101 + (Integer.parseInt(x) % 1000) % 100);
            canvas.drawPath(paths1, paint);
            if (newcount == 0) {
                paths2.reset();
                paths2.moveTo(100, 250);
            }
            paths2.lineTo(101 + newcount, 251 + ((Integer.parseInt(y) % 10000) % 1000) % 100);
            canvas.drawPath(paths2, paint);
            newcount = (newcount + 4) % 900;
            //获取绘制矩形数据
            if (count % 10 == 0) {
                count = 0;
                for (int i = 0; i < 8; i++) {
                    previous[i] = next[i];
                    int l = rand.nextInt() % radius;
                    if (l < 0) l = -l;
                    l = l + 20;
                    next[i] = l;
                }
            } else {
                num = (num + 1) % 5;
                for (int i = 0; i < 8; i++) {
                    len[i][num] = previous[i] + count * (next[i] - previous[i]) / 10;
                }
            }
            count++;
            paint.setTextSize(24);

            //绘制矩形
            for (int i = 0; i < 8; i++) {
                paint.setColor(Color.argb(200, 255 - 20 * i, (i * 60) % 255, i * 35));
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(1);
                drawcloumn(i, (int) len[i][num]);
            }

            //画坐标轴
            canvas.drawText("  (Hz)  0              10               20              30              40              50              60              70              80", 25, 840, paint);
            if (index == 10) {
                text = rand.nextInt(100) + "";
                index = 0;
            } else {
                ++index;
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.GREEN);
            paint.setTextSize(180);
            canvas.drawText(text, 450, 1200, paint);
            if (Integer.parseInt(text)>=90){
                play();
            }

        } catch (Exception ex) {
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交
        }
    }

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.1.101", 5000);
            Log.e("--->", "已发出链接请求");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            if (socket != null) {
                try {
                    b = in.readLine();
                    if (b.equals("") || b.equals(null)) {
                        b = "  0000.0000  0000.0000";
                    }
                    handler.sendEmptyMessage(0x1314);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (b.equals("") || b.equals(null)) {
                    b = "  0000.0000  0000.0000";
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        player.release();
        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawcloumn(int n, int length) {//画单个柱状图

        for (int i = 0; i < (length / 10); i++) {
            RectF myrect = new RectF(100 + n * 112, 780 - i * 8, 200 + n * 112, 786 - i * 8);
            canvas.drawRect(myrect, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    private void play(){
        if (player == null){
            player = MediaPlayer.create(context, R.raw.happy);
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();

    }
}   