package com.cpe.project.mariogame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Aut-1 on 12/6/2017.
 */

public class Tile {
    Rect dst, src;
    Paint p;

    boolean visible = true;

    enum TYPE {
        BOX_NULL, BRICK_BOX, WOOD_BOX, STEEL_BOX, SKY_BOX, COIN_BOX, ITEM_BOX, PREFEBS_BOX;
    }

    TYPE type;


    public Tile(Rect dst, Rect src, TYPE type) {
        this.dst = dst;
        this.src = src;
        this.type = type;
        p = new Paint();
        p.setColor(Color.RED);
    }

    int getLeft() {
        return dst.left;
    }

    int getRight() {
        return dst.right;
    }

    int getTop() {
        return dst.top;
    }

    int getBottom() {
        return dst.bottom;
    }

    void setVisible(boolean visible) {
        this.visible = visible;
    }

    boolean getVisible(){
        return this.visible;
    }

    void setType(TYPE type) {
        this.type = type;
    }

    TYPE getType() {
        return this.type;
    }

    void draw(Canvas canvas) {
        if (!visible) return;
        if (type != TYPE.BOX_NULL) {
            canvas.drawBitmap(GameMap.bitmap, src, dst, null);
        }
    }
}
