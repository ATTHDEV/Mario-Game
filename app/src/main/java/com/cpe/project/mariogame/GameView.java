package com.cpe.project.mariogame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Aut-1 on 12/5/2017.
 */

public class GameView extends View implements Runnable {

    static GameMap gameMap;
    static Player player;
    static ArrayList<Enemy> enemies;
    static Dpad dpad;
    Bitmap bitmap;
    float gravity = 0.5f;
    float offsetX = 0.4f;

    boolean gameIsRun = true;
    Thread thread = null;
    Object paseLock;
    boolean isPause = false;

    SparseArray<Point> sparsePoint;
    int state = 1;
    Bitmap prasart;
    Rect praSRC;
    Rect praDst;
    static int WIDTH, HEIGHT;
    private Paint textPaint , gameoverPaint;

    public GameView(Context context) {
        super(context);
        enemies = new ArrayList<>();
        sparsePoint = new SparseArray<>();
        gameMap = new GameMap(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mario2);
        player = new Player(450  , 0, 50, 80, bitmap );
        player.setGravity(0, gravity);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smb_enemies_sheet);
        Enemy enemy = new Enemy(750, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        enemy = new Enemy(1000, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        enemy = new Enemy(1600, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        enemies.add(enemy);
        enemy = new Enemy(3000, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        enemy = new Enemy(4200, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        enemy = new Enemy(4800, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        enemy = new Enemy(4600, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3, 50, GameMap.TILE_SIZE, bitmap);
        enemy.setGravity(0, gravity);
        enemies.add(enemy);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dpad3);
        prasart = BitmapFactory.decodeResource(getResources(), R.drawable.prasart);
        praSRC = new Rect(0, 0, prasart.getWidth(), prasart.getHeight());
        praDst = new Rect(GameMap.mapWIDTH - 250, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3 - 150, GameMap.mapWIDTH - 50, GameMap.mapHEIGHT - GameMap.TILE_SIZE * 3 + 60);
        textPaint = new Paint();
        textPaint.setTextSize(30);
        textPaint.setColor(Color.WHITE);
        gameoverPaint = new Paint();
        gameoverPaint.setTextSize(50);
        gameoverPaint.setColor(Color.WHITE);
        paseLock = new Object();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Point p = new Point();
                p.x = (int) event.getX(pointerIndex);
                p.y = (int) event.getY(pointerIndex);
                sparsePoint.put(pointerId, p);
                dpad.pressKey(p.x, p.y, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Point point = sparsePoint.get(pointerId);
                dpad.pressKey(point.x, point.y, 2);
                sparsePoint.remove(pointerId);
                break;
            }
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dpad = new Dpad(100, getHeight() - 250, 150, 150, bitmap);
        //dpad.setButtonAttack(getWidth() - 150, getHeight() - 220, 30);
        dpad.setButtonJump(getWidth() - 250, getHeight() - 180, 50);
        WIDTH = w;
        HEIGHT = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(107, 140, 255));
        if (player.getRight() > GameView.WIDTH * offsetX && player.getRight() - GameView.WIDTH * offsetX < GameMap.mapWIDTH - GameView.WIDTH) {
            canvas.save();
            canvas.translate(GameView.WIDTH * offsetX - player.getRight(), 0);
            gameMap.draw(canvas);
            renderMyName(canvas);
            canvas.drawBitmap(prasart, praSRC, praDst, null);
            for (Enemy enemy : enemies) {
                enemy.draw(canvas);
            }
            if (player.isNotDie()) {
                player.draw(canvas);
            }
            canvas.restore();
        } else if (player.getRight() - GameView.WIDTH * offsetX >= GameMap.mapWIDTH - GameView.WIDTH) {
            canvas.save();
            canvas.translate(GameView.WIDTH - GameMap.mapWIDTH, 0);
            gameMap.draw(canvas);
            renderMyName(canvas);
            canvas.drawBitmap(prasart, praSRC, praDst, null);
            for (Enemy enemy : enemies) {
                enemy.draw(canvas);
            }
            if (player.isNotDie()) {
                player.draw(canvas);
            }
            canvas.restore();
        } else {
            gameMap.draw(canvas);
            renderMyName(canvas);
            canvas.drawBitmap(prasart, praSRC, praDst, null);
            for (Enemy enemy : enemies) {
                enemy.draw(canvas);
            }
            if (player.isNotDie()) {
                player.draw(canvas);
            }

        }
        dpad.draw(canvas);
        renderTextCoin(canvas);
        renderTextLife(canvas);
        if(player.isWingame()){
            renderWinText(canvas);
        }
    }

    void update(double time) {
        if (player.isNotDie() && !player.isWingame()) {
            player.update(time);
        }
        try {
            for (Enemy enemy : enemies) {
                enemy.update(time);
            }
        } catch (ConcurrentModificationException ex) {
        }
        postInvalidate();
    }

    @Override
    public void run() {
        double time = 0;
        double prevTime = System.currentTimeMillis();
        double currentTime = 0;
        while (gameIsRun) {
            try {
                currentTime = System.currentTimeMillis();
                time = currentTime - prevTime;
                update(time);
                prevTime = currentTime;

                Thread.sleep(1000 / 60);

                synchronized (paseLock) {
                    while (isPause) {
                        paseLock.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void pause() {
        synchronized (paseLock) {
            isPause = true;
            state = 10;
        }
    }

    void resume() {
        synchronized (paseLock) {
            isPause = false;
            paseLock.notifyAll();
        }
    }

    void destroy() {
        gameIsRun = false;
    }

    void renderTextCoin(Canvas canvas) {
        canvas.drawText("Coin = " + player.getCoinStatus(), GameView.WIDTH - 130, 50, textPaint);
    }

    void renderTextLife(Canvas canvas) {
        if (player.getLife() > 0)
            canvas.drawText("Life = " + player.getLife(), GameView.WIDTH - 130, 100, textPaint);
        else
            renderGameOver(canvas);
    }

    void renderGameOver(Canvas canvas) {
        canvas.drawText("Game Over !!!", GameView.WIDTH / 2-100, GameView.HEIGHT / 2 - 10, gameoverPaint);
    }
    void renderWinText(Canvas canvas){
        canvas.drawText("You Win !!!", GameView.WIDTH / 2-100, GameView.HEIGHT / 2 - 10, gameoverPaint);
    }
    void renderMyName(Canvas canvas){
        textPaint.setTextSize(20);
        canvas.drawText("พัฒนาโดย อรรถวุฒิ พ่วงศิริ", GameMap.mapWIDTH-400, GameView.HEIGHT-50, textPaint);
        canvas.drawText("วิชา CPEN2436 Mobile Computing and Wireless Communication", GameMap.mapWIDTH-600, GameView.HEIGHT-25, textPaint);
    }
}
