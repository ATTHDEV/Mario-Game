package com.cpe.project.mariogame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Aut-1 on 12/5/2017.
 */

public class Dpad {
    int x, y, w, h;
    Bitmap bitmap;
    Rect src, dst;

    int x2 = 0, y2 = 0, r1 = 50;

    int x3 = 0, y3 = 0, r2 = 50;

    Paint paint;
    Paint paint2;
    Paint paint3;

    int touchSpeed = 5;


    public Dpad(int x, int y, int w, int h, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bitmap = bitmap;
        src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dst = new Rect(x, y, x + w, y + h);
        paint = new Paint();
        paint.setColor(Color.rgb(236, 236, 236));
        paint.setAntiAlias(true);
        paint.setAlpha(127);
        paint2 = new Paint();
        paint2.setColor(Color.rgb(180, 180, 180));
        paint2.setAntiAlias(true);
        paint2.setAlpha(127);
        paint3 =  new Paint();
        paint3.setAntiAlias(true);
        paint3.setAlpha(127);
    }

    void setButtonJump(int x, int y, int r) {
        x2 = x;
        y2 = y;
        r1 = r;
    }

    void setButtonAttack(int x, int y, int r) {
        x3 = x;
        y3 = y;
        r2 = r;
    }

    int distance(int x, int y, int x2, int y2) {
        int dx = x2 - x;
        int dy = y2 - y;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    void pressKey(int tx, int ty, int state) {
        Player p = GameView.player;
        switch (state) {
            case 0:
                if (tx > x && tx < x + w / 3 && ty > y + h / 3 && ty < y + h * 2 / 3)
                    p.moveLeft();
                if (tx > x + w * 2 / 3 && tx < x + w && ty > y + h / 3 && ty < y + h * 2 / 3)
                    p.moveRight();
                if (tx > x + w / 3 && tx < x + w * 2 / 3 && ty > y && ty < y + h / 3)
                    p.moveUp();
                if (tx > x + w / 3 && tx < x + w * 2 / 3 && ty > y + h * 2 / 3 && ty < y + h)
                    p.moveDown();
                if (distance(tx, ty, x2, y2) < r1)
                    p.startJump();
                if (distance(tx, ty, x3, y3) < r2) {
                }
                break;
            case 1:
//                if (tx < dst.left || tx > dst.right || ty < dst.top || ty > dst.bottom) {
//                }
                break;
            case 2:
                if (distance(tx, ty, x2, y2) < r1) {
                    p.endJump();
                } else {
                    p.idle();
                }
                //p.direction = 4;
                break;
        }
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, src, dst, paint3);

        canvas.drawCircle(x2, y2, r1, paint);
        canvas.drawCircle(x2, y2, r1 - 10, paint2);

//        canvas.drawCircle(x3, y3, r2, paint);
//        canvas.drawCircle(x3, y3, r2 - 10, paint2);
    }
}
