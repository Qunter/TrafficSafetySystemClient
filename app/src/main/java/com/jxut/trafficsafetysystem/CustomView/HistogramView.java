package com.jxut.trafficsafetysystem.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.animation.Animation;

import java.util.Random;

/**
 * @author peter
 */
public class HistogramView extends SurfaceView implements Callback, Runnable {// 备注1


    private SurfaceHolder sfh;
    private Thread th;
    private Canvas canvas;
    private Paint paint;
    private Random rand;
    private float len[][], previous[], next[];
    private int radius;//园的半径
    private int count;//计数器
    private int num;//圆形曲线的编号

    public HistogramView(Context context) {
        super(context);
        initView();

    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
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
        radius = 360;
        for (int i = 0; i < 8; i++) {
            next[i] = radius;
        }
        count = 0;
        this.setKeepScreenOn(true);// 保持屏幕常亮
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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

    private void draw() {
        try {
            canvas = sfh.lockCanvas(); // 得到一个canvas实例
            canvas.drawColor(Color.WHITE);// 刷屏
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
            //画柱状图
            for (int i = 0; i < 8; i++) {
                paint.setColor(Color.argb(200, 255 - 20 * i, (i * 60) % 255, i * 35));
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(1);
                drawcloumn(i, (int) len[i][num]);
            }
            //画坐标轴
            canvas.drawText("  (Hz)  0              10               20              30              40              60              70              80              90", 25, 450, paint);

        } catch (Exception ex) {
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);  // 将画好的画布提交
        }
    }
    @Override
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
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void drawcloumn(int n, int length) {//画单个柱状图

        for (int i = 0; i < (length / 10); i++) {
            RectF myrect = new RectF(100 + n * 112, 350 - i * 8, 200 + n * 112, 356 - i * 8);
            canvas.drawRect(myrect, paint);
        }
    }

}

