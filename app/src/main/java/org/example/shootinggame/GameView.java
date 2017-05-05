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
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.concurrent.atomic.AtomicBoolean;

/*
public class GameView extends View {
    private Fighter fighter;

    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (fighter == null) {
            Bitmap fighterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fighter);
            fighterBitmap = Bitmap.createScaledBitmap(fighterBitmap, 100, 100, false);
            fighter = new Fighter(fighterBitmap, width, height);
        }

        fighter.draw(canvas);
    }
}
*/

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final float ACCEL_WEIGHT = 3f;
    private static final int DRAW_INTERVAL = 1000 / 60;
    private static final float TEXT_SIZE = 40f;

    private final Paint paint = new Paint();
    private final Paint textPaint = new Paint();

    private final Bitmap fighterBitmap;
    private float fighterX;
    private Fighter fighter;

    public GameView(Context context) {
        super(context);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TEXT_SIZE);

        Bitmap fighterBitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.fighter);
        fighterBitmap = Bitmap.createScaledBitmap(fighterBitmapTemp, 100, 100, false);

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

                drawFighter(canvas);

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

    public void drawFighter(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        //canvas.drawBitmap(fighterBitmap, 50, 50, paint);
        //canvas.drawBitmap(fighterBitmap, fighterX, 200, paint);
        if (fighter == null) {
            fighter = new Fighter(fighterBitmap, 0);
        }

        fighter.draw(canvas);
    }
}
