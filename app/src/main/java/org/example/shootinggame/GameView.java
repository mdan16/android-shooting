package org.example.shootinggame;

/**
 * Created by mdan on 2017/05/04.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

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
            fighter = new Fighter(fighterBitmap, width, height);
        }

        fighter.draw(canvas);
    }
}
