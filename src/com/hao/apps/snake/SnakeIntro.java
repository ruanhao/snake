package com.hao.apps.snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SnakeIntro extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setBackgroundDrawableResource(R.drawable.intro);
        setContentView(new IntroView(this));
        subThread subThread = new subThread(this);
        subThread.start();
    }
}

class subThread extends Thread {
    public Activity ctx;
    
    public subThread (Activity ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {

        super.run();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setClass(ctx, Snake.class);
        ctx.startActivity(intent);
        ctx.finish();
    }
}

class IntroView extends View {
    // private Context ctx = null;
    Bitmap bitmap = null;
    Bitmap shapedBitmap = null;
    
    public IntroView(Context context) {
        super(context);
        // this.ctx = context;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.intro);
        Matrix matrix = new Matrix();
        matrix.preScale(1f, 1f);
        shapedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        setBackgroundColor(Color.WHITE);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        int picWidth = shapedBitmap.getWidth();
        int picHeight = shapedBitmap.getHeight();
        int left = (viewWidth - picWidth) / 2;
        int top = (viewHeight - picHeight) / 2;
        System.out.println(viewWidth + "    " + viewHeight);
        canvas.drawBitmap(shapedBitmap, left, top, new Paint());
        super.onDraw(canvas);
    }
    
}
