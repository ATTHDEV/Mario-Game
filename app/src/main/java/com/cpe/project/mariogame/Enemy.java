package com.cpe.project.mariogame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Aut-1 on 12/7/2017.
 */

public class Enemy {

    private float x, y;
    private float w, h;
    private float vx = 5, vy = 5, ax, ay;
    private Rect src;
    private RectF dst;
    private Bitmap bitmap;
    private boolean isLeft = true;
    private Player p;
    boolean hitFlag = true;
    int state = 0;
    boolean isDie = false;
    boolean isRemove = false;
    double dieTime = 1000;
    double countDieTime = 0;


    private GameMap map;

    public Enemy(float x, float y, float w, float h, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bitmap = bitmap;
        src = new Rect(0, 5, 25, 30);
        dst = new RectF(x, y, x + w, y + h);
        map = GameView.gameMap;
        p = GameView.player;
    }

    int getLeft() {
        return (int) x;
    }

    int getRight() {
        return (int) (x + w);
    }

    int getTop() {
        return (int) y;
    }

    int getBottom() {
        return (int) (y + h);
    }

    void setGravity(float ax, float ay) {
        this.ax = ax;
        this.ay = ay;
    }

    private void addAcceleretion() {
        vx += ax;
        vy += ay;
    }

    void update(double time) {
        if (isDie) {
            countDieTime += time;
            if (countDieTime > dieTime) {
                vy = 5;
                y += vy;
                if (y > GameMap.mapHEIGHT) {
                    GameView.enemies.remove(this);
                }

            }
        } else {
            addAcceleretion();

            if (state == 1) {
                float oldx = x;
                x = x + vx;
                if (map.constrainEmemy(this)) {
                    vx *= -1;
                    hitFlag = true;
                    x = oldx;
                }
            }

            if (state == 0) {
                float oldy = y;
                y = y + vy;
                if (map.constrainEmemy(this)) {
                    vy = 0;
                    y = oldy;
                    state = 1;
                }
            }

            isHit();
        }
        dst.set(x, y, getRight(), getBottom());
    }

    void isHit() {

        if (p.getLeft() < getRight() && p.getRight() > getLeft() && p.getTop() < getBottom() && p.getBottom() > getTop()) {
            if (p.isOnGround()) {
                p.setBlick();
            }
            if (p.isFalling()) {
                MainActivity.playSound(3);
                isDie = true;
                src.set(90, 5, 115, 30);
            } else {
                if (hitFlag) {
                    if(p.isNotDie() && !p.isWingame()) {
                        MainActivity.playSound(5);
                        p.setLife(-1);
                    }
                    hitFlag = false;
                }
            }
        } else {
            p.setAlpha(255);
        }
    }

    void draw(Canvas canvas) {
        //canvas.drawRect(dst,new Paint());
        canvas.drawBitmap(bitmap, src, dst, null);
    }
}
