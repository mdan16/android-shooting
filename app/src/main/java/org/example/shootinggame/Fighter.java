package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/04.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Fighter {
    private final Paint paint = new Paint();

    public final Bitmap bitmap;
    public final Rect rect;

    public Fighter(Bitmap bitmap, int width, int height) {
        this.bitmap = bitmap;

        int left = (width - bitmap.getWidth()) / 2;
        int top = (height - bitmap.getHeight()) / 2;
        int right = left + bitmap.getWidth();
        int bottom = top + bitmap.getHeight();
        rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, rect.left, rect.top, paint);
    }
}
