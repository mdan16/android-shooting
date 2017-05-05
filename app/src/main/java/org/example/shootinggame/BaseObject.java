package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/05.
 */
import android.graphics.Canvas;

public abstract class BaseObject {
    float xPosition;
    float yPosition;

    public abstract void draw(Canvas canvas);

    public boolean isAvailable(int width, int height) {
        if (yPosition < 0 || xPosition < 0 || yPosition > height || xPosition > width) {
            return false;
        }
        return true;
    }

    public abstract void move();
}
