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

    Bullet(RectF rect) {
        xPosition = rect.centerX();
        yPosition = rect.centerY();

        paint.setColor(Color.RED);
    }

    @Override
    public void move() {
        yPosition -= 1 * MOVE_WEIGHT;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(xPosition, yPosition, SIZE, paint);
    }
}
