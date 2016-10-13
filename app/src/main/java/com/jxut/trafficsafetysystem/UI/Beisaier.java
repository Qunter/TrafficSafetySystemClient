package com.jxut.trafficsafetysystem.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.animation.Animation;

import com.jxut.trafficsafetysystem.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private int count, newcount;//计数器
    private int[] easyvalue, value2;
    private int num;//圆形曲线的编号
    private BufferedReader reader;//文件数据读取
    private String s;
    private String[] datastr;
    private Path[] paths;


    public Beisaier(Context context) {
        super(context);
        th = new Thread(this);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        rand = new Random();
        previous = new float[8];
        num = -1;
        next = new float[8];
        len = new float[8][5];
        paths = new Path[8];
        radius = 360;
        for (int i = 0; i < 8; i++) {
            next[i] = radius;
        }
        count = 0;
        newcount = 0;
        //读取测试数据
        paths = new Path[8];
        for (int i = 0; i < 8; i++) {
            paths[i] = new Path();
        }
        reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));
        easyvalue = new int[8];
        value2 = new int[8];
        //测试数据结束
        this.setKeepScreenOn(true);// 保持屏幕常亮

    }

    public Beisaier(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Beisaier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        th.start();
    }

    private void draw() {
        try {
            canvas = sfh.lockCanvas(); // 得到一个canvas实例
            canvas.drawColor(Color.BLACK);// 刷屏
            //绘制实时脑电
            try {
                s = reader.readLine();
                if (s.length() > 9) {
                    datastr = s.split("	");
                } else reader.reset();

            } catch (IOException e) {
                e.printStackTrace();
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            for (int i = 0; i < 1; i++) {
                if (newcount == 0) {
                    paths[i].reset();
                    paths[i].moveTo(100, 1400 + i * 60);
                    easyvalue[i] = Integer.parseInt(datastr[i]);
                }
                value2[i] = (Integer.parseInt(datastr[i]) - easyvalue[i]) / 1000;
                paths[i].lineTo(101 + newcount, 1400 + i * 60 + value2[i]);
                canvas.drawPath(paths[i], paint);
            }
            newcount = (newcount + 4) % 1400;
            //获取绘图数据
            if (count % 10 == 0) {
                count = 0;
                for (int i = 0; i < 8; i++) {
                    previous[i] = next[i];
                    int l = rand.nextInt() % radius;
                    //int l=(int)pxx[i];
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

            //画柱状图
            for (int i = 0; i < 4; i++) {
                paint.setColor(Color.argb(200, 255 - 20 * i, (i * 60) % 255, i * 35));
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(1);
                drawcloumn(i, (int) len[i][num]);
            }

            //画坐标轴
            canvas.drawText("  (Hz)  0               10               20               30", 60, 350, paint);

        } catch (Exception ex) {
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交
        }
    }

    public void run() {
        while (true) {
            draw();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void drawcloumn(int n, int length) {//画单个柱状图

        for (int i = 0; i < (length / 10); i++) {
            RectF myrect = new RectF(100 + n * 90, 300 - i * 6, 170 + n * 90, 304 - i * 6);
            canvas.drawRoundRect(myrect, 5, 5, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}   

	
