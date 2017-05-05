package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/04.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Window;
import android.view.WindowManager;

public class Fighter {
    private final Paint paint = new Paint();

    public final Bitmap fighterBitmap;
    public final RectF rect;
    private int winWidth, winHeight;

    public Fighter(Bitmap bmp, int left) {
        winHeight = GameActivity.winHeight;
        winWidth = GameActivity.winWidth;

        fighterBitmap = bmp;

        int top = winHeight - 300;
        int right = left + bmp.getWidth();
        int bottom = top + bmp.getHeight();
        rect = new RectF(left, top, right, bottom);
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(fighterBitmap, rect.left, rect.top, paint);
    }

    void move(float xOffset) {
        if (rect.left <= 0 ) {
            rect.offset(1, 0);
            return;
        } else if (winWidth <= rect.right) {
            rect.offset(-1, 0);
            return;
        }
        rect.offset(xOffset, 0);
    }
}
