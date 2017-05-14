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

import java.util.Random;

public class Fighter extends BaseObject {
    private final Paint paint = new Paint();

    public final Bitmap fighterBitmap;
    public final RectF rect;
    private int winWidth, winHeight;
    public boolean enemy;

    private String name;
    private int hp = 500;
    private int bulletNum = 20;

    private Random rnd = new Random();
    private static int enemySpeed = 10;

    public Fighter(Bitmap bmp, int left, boolean enemy) {
        this.enemy = enemy;
        winHeight = GameActivity.winHeight;
        winWidth = GameActivity.winWidth;

        fighterBitmap = bmp;

        name = "Fighter";
        int top = winHeight - 300;
        if (enemy) {
            top = 0;
            name = "Enemy";
        }
        int right = left + bmp.getWidth();
        int bottom = top + bmp.getHeight();
        rect = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != STATE_NORMAL) {
            return;
        }
        canvas.drawBitmap(fighterBitmap, rect.left, rect.top, paint);
    }

    @Override
    public void move() {
        move(0);
    }
    public void move(float xOffset) {
        if (rect.left <= 0 ) {
            rect.offset(1, 0);
            return;
        } else if (winWidth <= rect.right) {
            rect.offset(-1, 0);
            return;
        }
        rect.offset(xOffset, 0);
    }

    public void enemyMove(Fighter fighter) {
        if (this.getType() == Type.Fighter) {
            return;
        }

        if (rnd.nextInt(100) > 95) {
            enemySpeed = -1 * enemySpeed;
        }

        if (rnd.nextInt(100) > 95) {
            if (rect.left > fighter.rect.left) {
                enemySpeed = -10;
            } else {
                enemySpeed = 10;
            }
        }
        move(enemySpeed);
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.state == STATE_DESTROYED) {
            return false;
        }
        if (!(this.getType() == Type.Fighter && object.getType() == Type.EnemyBullet) &&
                !(this.getType() == Type.Enemy && object.getType() == Type.FighterBullet)) {
            return false;
        }

        int x = Math.round(object.xPosition);
        int y = Math.round(object.yPosition);
        return rect.contains(x, y);
    }

    @Override
    public void hit() {
        hp -= 10;
        return;
    }

    public void redHp(int reduceNum) {
        hp -= reduceNum;
        return;
    }

    public void incHp(int incNum) {
        hp += incNum;
        return;
    }

    public void incBullet(int incNum) {
        bulletNum += incNum;
        return;
    }

    public int getHp() {
        return hp;
    }
    public String getName() {
        return name;
    }
    public int getBulletNum() {
        return bulletNum;
    }
    public int fire() {
        bulletNum--;
        return getBulletNum();
    }
    public int store() {
        bulletNum+=10;
        return getBulletNum();
    }

    @Override
    public Type getType() {
        if (!enemy) {
            return Type.Fighter;
        } else {
            return Type.Enemy;
        }
    }
}
