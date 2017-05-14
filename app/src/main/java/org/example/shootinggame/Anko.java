package org.example.shootinggame;

import android.graphics.Bitmap;

/**
 * Created by mdan on 2017/05/15.
 */

public class Anko extends Obstacle {

    Anko(Bitmap bmp) {
        super(bmp);
    }

    @Override
    public Type getType() {
        return Type.Anko;
    }

    @Override
    public void move() {
        if (rnd.nextInt(30) > 28) {
            direction = direction==1?-1:1;
        }
        if (direction > 0) {
            rect.offset(8, 5);
        } else {
            rect.offset(8, -5);
        }
    }
}
