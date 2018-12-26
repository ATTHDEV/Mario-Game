package com.cpe.project.mariogame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Aut-1 on 12/5/2017.
 */

class Player {

    private float x, y;
    private float w, h;

    private Bitmap playerBitmap;
    private Rect src;
    private RectF dst;
    private Paint paint;

    private int countTime = 0;
    private boolean wingame = false;

    private int[] frameX = {0, 55, 105, 160};
    private int frameColum = 0;
    private int[] frameY = {30, 147, 255, 0};
    private int frameRow = 2;
    private int srcOffsetY = 50;

    private float vx = 0, vy = 0;
    private float ax = 0, ay = 0;
    private final int SPEED = 5;
    private final int jumpUpForce = 18;
    private final int jumpDownForce = 10;


    int gravityDirection = 0;
    private boolean swap = true;

    private boolean onGround = false;
    private boolean onHorizon = false;
    private boolean isJump = false;
    private boolean idle = true;
    private boolean isDie = false;
    private boolean isJumpingUp = false;
    private boolean isBlink = false;
    private boolean shortJump = false;

    private boolean isSwap = false;
    private GameMap map;

    private int coin = 0;
    private int life = 3;

    Player(float x, float y, float w, float h, Bitmap playerBitmap) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.playerBitmap = playerBitmap;
        src = new Rect(frameX[0], frameY[frameRow], frameX[0] + 50, frameY[frameRow] + srcOffsetY);
        dst = new RectF(x, y, x + w, y + h - 20);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        map = GameView.gameMap;
    }

    int getX() {
        return (int) this.x;
    }

    int getY() {
        return (int) this.y;
    }

    int getWidth() {
        return (int) this.w;
    }

    int getHeight() {
        return (int) this.h;
    }

    boolean isFalling() {
        return !isJumpingUp && !onGround;
    }

    void setGravity(float ax, float ay) {
        this.ax = ax;
        this.ay = ay;
    }

    void draw(Canvas canvas) {
        if (isDie) return;
        if (isSwap) {
            if (swap) {
                canvas.drawBitmap(playerBitmap, src, dst, paint);
            } else {
                canvas.drawRect(dst, paint);
            }
            swap = !swap;
        } else {
            canvas.drawBitmap(playerBitmap, src, dst, paint);
        }

    }

    private void updateFrame(double time) {
        if (!idle) {
            countTime += time;
            int frameTime = 100;
            if (countTime > frameTime) {
                frameColum++;
                if (frameColum > 3) frameColum = 0;
                countTime = 0;
            }
        }
        src = new Rect(frameX[frameColum], frameY[frameRow], frameX[frameColum] + 50, frameY[frameRow] + srcOffsetY);
    }

    void moveLeft() {
        if (isJump) {
            frameColum = 3;
        } else {
            idle = false;
            frameColum = 0;
        }
        onHorizon = true;
        frameRow = 1;
        vx = -SPEED;
    }

    void moveRight() {
        if (isJump) {
            frameColum = 3;
        } else {
            idle = false;
            frameColum = 0;
        }
        onHorizon = true;
        frameRow = 2;
        vx = SPEED;
    }

    void moveUp() {
        if(!shortJump) {
            vy = -SPEED;
            shortJump = true;
        }
    }

    void moveDown() {
        vy = SPEED;
    }

    void idle() {
        frameColum = 0;
        idle = true;
        vx = 0;
        if(onGround)vy =0;
        onHorizon = false;
    }

    void startJump() {
        if (!onGround) return;
        MainActivity.playSound(2);
        if (onHorizon)
            frameColum = 3;
        else
            frameColum = 0;
        isJump = true;
        idle = true;
        vy -= jumpUpForce;
        onGround = false;
        isJumpingUp = true;
    }

    void endJump() {
        if (vy < -jumpDownForce) {
            vy = -jumpDownForce;
        }
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

    void update(double time) {
        if (isDie) return;
        updateFrame(time);

        move();
        if (onGround && x > GameMap.mapWIDTH - 200 && !wingame) {
            MainActivity.playSound(6);
            wingame = true;
        }

        dst.set(x, y, getRight(), getBottom());
    }

    public boolean isWingame() {
        return this.wingame;
    }

    private void addAcceleretion() {
        vx += ax;
        vy += ay;
    }

    private void move() {
        addAcceleretion();
        if (!onGround && onHorizon) {
            idle = true;
            frameColum = 3;
        }

        float oldx = x;
        x = x + vx;
        boolean colided = map.constrainPlayer(this);
        if (colided) {
            vx = 0;
            x = oldx;
        }

        if (x < 0) {
            x = 0;
        } else if (x + w > GameMap.mapWIDTH) {
            x = GameMap.mapWIDTH - w;
        }

        float oldy = y;
        y = y + vy;
        if (y - oldy > 0) {
            isJumpingUp = false;
            onGround = false;
        }
        colided = map.constrainPlayer(this);
        if (colided) {
            if (onHorizon) {
                idle = false;
            }
            isJump = false;
            onGround = true;
            shortJump = false;
            vy = 0;
            y = oldy;
        }

        if (y > GameMap.mapHEIGHT) {
            life = 0;
            MainActivity.playSound(0);
            isDie = true;
        }
    }

    public boolean onJumpingUp() {
        return this.isJumpingUp;
    }

    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    public void setBlick() {
        if (isBlink) {
            paint.setAlpha(100);
        } else {
            paint.setAlpha(255);
        }
        isBlink = !isBlink;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setLife(int life) {
        this.life += life;
        if (this.life <= 0) {
            if (!isDie)
                MainActivity.playSound(0);
            isDie = true;
        }
    }

    public void getCoin(int coin) {
        MainActivity.playSound(1);
        this.coin += coin;
        //Log.d("game", "coin = " + this.coin);
    }

    public boolean isNotDie() {
        return !this.isDie;
    }

    public int getCoinStatus() {
        return this.coin;
    }

    public int getLife() {
        return this.life;
    }
}
