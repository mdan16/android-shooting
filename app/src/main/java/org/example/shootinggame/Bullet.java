package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/05.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Bullet extends BaseObject {
    private static final float MOVE_WEIGHT = 12.0f;

    private final Paint paint = new Paint();

    private static final float SIZE = 15f;
    private boolean enemy;

    Bullet(RectF rect, boolean enemy) {
        xPosition = rect.centerX();
        yPosition = rect.centerY();

        paint.setColor(Color.RED);
        this.enemy = enemy;
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (this.state == STATE_DESTROYED || object.state == STATE_DESTROYED) {
            return false;
        }
        if ((this.getType() == Type.FighterBullet && object.getType() == Type.Fighter) ||
                (this.getType() == Type.EnemyBullet && object.getType() == Type.Enemy)) {
            return false;
        }
        return (calcDistance(this, object) < SIZE);
    }

    @Override
    public Type getType() {
        if (!enemy) {
            return Type.FighterBullet;
        } else {
            return Type.EnemyBullet;
        }
    }

    @Override
    public void move() {
        if (!enemy) {
            yPosition -= 1 * MOVE_WEIGHT;
        } else {
            yPosition += 1 * MOVE_WEIGHT;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != STATE_NORMAL) {
            return;
        }
        canvas.drawCircle(xPosition, yPosition, SIZE, paint);
    }
}
