package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/04.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.Surface;
import android.view.View;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final float ACCEL_WEIGHT = 3f;
    private static final int DRAW_INTERVAL = 1000 / 60;
    private static final float SCORE_TEXT_SIZE = 60.0f;

    private final Bitmap fighterBitmap;
    private final Bitmap enemyBitmap;
    private Fighter fighter;
    private Fighter enemy;
    private final List<BaseObject> bulletList = new ArrayList<>();

    private final Paint paintScore = new Paint();
    private Random rnd = new Random();

    public GameView(Context context) {
        super(context);

        paintScore.setColor(Color.BLACK);
        paintScore.setTextSize(SCORE_TEXT_SIZE);
        paintScore.setAntiAlias(true);

        Bitmap fighterBitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.fighter);
        fighterBitmap = Bitmap.createScaledBitmap(fighterBitmapTemp, 100, 100, false);

        Bitmap enemyBitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmapTemp, 100, 100, false);


        getHolder().addCallback(this);
    }

    private DrawThread drawThread;

    private class DrawThread extends Thread {
        private final AtomicBoolean isFinished = new AtomicBoolean();

        public void finish() {
            isFinished.set(true);
        }

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();
            while (!isFinished.get()) {
                if (holder.isCreating()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) {
                    continue;
                }

                drawGame(canvas);

                holder.unlockCanvasAndPost(canvas);
                synchronized (this) {
                    try {
                        wait(DRAW_INTERVAL);
                    } catch (InterruptedException e) {

                    }
                }
            }

        }
    }

    public void startDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        drawThread.start();
    }
    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }

        drawThread.finish();
        drawThread = null;
        return true;
    }

    private static final float ALPHA = 0.8f;
    private float[] sensorValues;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorValues = event.values;
            if (sensorValues == null) {
                sensorValues = new float[3];
                sensorValues[0] = event.values[0];
                sensorValues[1] = event.values[1];
                sensorValues[2] = event.values[2];
                return;
            }

            sensorValues[0] = sensorValues[0] * ALPHA + event.values[0] * (1f - ALPHA);
            sensorValues[1] = sensorValues[1] * ALPHA + event.values[1] * (1f - ALPHA);
            sensorValues[2] = sensorValues[2] * ALPHA + event.values[2] * (1f - ALPHA);

            //fighterX += -sensorValues[0] * ACCEL_WEIGHT;
            if (fighter != null) {
                float xOffset = -sensorValues[0] * ACCEL_WEIGHT;
                fighter.move(xOffset);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void startSensor() {
        sensorValues = null;

        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSensor() {
        SensorManager sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }

    public void drawGame(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //canvas.drawBitmap(fighterBitmap, 50, 50, paint);
        //canvas.drawBitmap(fighterBitmap, fighterX, 200, paint);
        if (fighter == null) {
            fighter = new Fighter(fighterBitmap, 0, false);
        }
        if (enemy == null) {
            enemy = new Fighter(enemyBitmap, 200, true);
        }

        drawObjectList(canvas, bulletList, width, height);

        for (int i = 0; i < bulletList.size(); i++) {
            BaseObject bullet = bulletList.get(i);

            if (fighter.isHit(bullet)) {
                bullet.hit();
                fighter.hit();
            } else if (enemy.isHit(bullet)) {
                enemy.hit();
                bullet.hit();
            }

            for (int j = i+1; j < bulletList.size(); j++) {
                BaseObject bullet2 = bulletList.get(j);

                if (bullet2.isHit(bullet)) {
                    bullet.hit();
                    bullet2.hit();
                }
            }
        }

        enemy.enemyMove();
        enemyFire();

        fighter.draw(canvas);
        enemy.draw(canvas);
        canvas.drawText("Fighter HP: " + fighter.getHp(), 0, SCORE_TEXT_SIZE, paintScore);
        canvas.drawText("Enemy HP: " + enemy.getHp(), 0, SCORE_TEXT_SIZE*2, paintScore);
    }

    public static void drawObjectList(Canvas canvas, List<BaseObject> objectList, int width, int height) {
        for (int i = 0; i < objectList.size(); i++) {
            BaseObject object = objectList.get(i);
            if (object.isAvailable(width, height)) {
                object.move();
                object.draw(canvas);
            } else {
                objectList.remove(object);
                i--;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fighterFire();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void fighterFire() {
        Bullet bullet = new Bullet(fighter.rect, false);
        bulletList.add(0, bullet);
    }
    private void enemyFire() {
        if (rnd.nextInt(10) > 8) {
            Bullet bullet = new Bullet(enemy.rect, true);
            bulletList.add(0, bullet);
        }
    }

}
