package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/14.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Obstacle extends BaseObject {

    protected final Paint paint = new Paint();

    public final Bitmap obstacleBitmap;
    public final RectF rect;
    protected Random rnd = new Random();

    protected int yOffset = -40;
    protected int direction = -1;

    Obstacle(Bitmap bmp) {
        obstacleBitmap = bmp;
        int left = 0;
        int top = 500;
        int right = left + bmp.getWidth();
        int bottom = top + bmp.getHeight();
        rect = new RectF(left, top, right, bottom);
    }

    @Override
    public Type getType() {
        return Type.Obstacle;
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != STATE_NORMAL) {
            return;
        }
        canvas.drawBitmap(obstacleBitmap, rect.left, rect.top, paint);
    }

    @Override
    public void move() {
        if (rnd.nextInt(30) > 28) {
            direction = direction==1?-1:1;
        }
        if (direction > 0) {
            rect.offset(5, 5);
        } else {
            rect.offset(5, -5);
        }
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (this.state == STATE_DESTROYED || object.state == STATE_DESTROYED) {
            return false;
        }
        if (!(object.getType() == Type.FighterBullet || object.getType() == Type.EnemyBullet)) {
            return false;
        }
        int x = Math.round(object.xPosition);
        int y = Math.round(object.yPosition);
        return rect.contains(x, y);
    }

    public boolean hitByFighter(BaseObject object) {
        return object.getType() == Type.FighterBullet;
    }
}
