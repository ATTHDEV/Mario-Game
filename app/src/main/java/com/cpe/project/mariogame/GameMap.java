package com.cpe.project.mariogame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Aut-1 on 12/5/2017.
 */

public class GameMap {

    static int TILE_SIZE = 60;
    Paint paint;
    static Bitmap bitmap;
    static SparseArray<Bitmap> tileBitmap;
    Rect src, dst;

    int tileWidth = 90;
    int tileHeight = 12;
    static int mapWIDTH = 90 * TILE_SIZE;
    static int mapHEIGHT = 12 * TILE_SIZE;
    Tile[][] tiles;

    String[] mapData = {
            "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            "00000000tyu000tyu0000000000000000000000000000qq000000000000000qqqq000000000000000000000000",
            "0000c000ghj000ghj00000000000cc000000000000000qq00000c000000000qqqq00000qqq00qqqq00qqqq0000",
            "0022322000000000000000000002332000000222000000000002320000000000000000q00000q00q00q0000000",
            "0000000000000002200000000000000000000000000000000000000000000000000002q02000qqqq00qqqq0022",
            "0000000000000000000000000000000000000000000000000000000000440000000000q00000q00200q0002000",
            "02200000000000000000000000000000000000000000000000000000044400000000000qqq00q00000qqqq0000",
            "000000000111000000004004000000000440040000000000000000004444000000000000220000000000000000",
            "bn00000001110000000440044000000044400440000000bn000000044444000000044000000000000000000000",
            "vm0000000111000000444ss44400000444400444000000vm000000444444000000044400000000000000000020",
            "111111111111111111111111111111111110011111001111111111111111000bn0011111111111111111111111",
            "111111111111111111111111111111111110011111001111111111111111000vm0011111111111111111111111",
    };

    public GameMap(View view) {
        paint = new Paint();
        paint.setColor(Color.RED);
        tileBitmap = new SparseArray<>();
        bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.tileset);

        tiles = new Tile[tileHeight][tileWidth];
        for (int i = 0; i < tileHeight; i++) {
            tiles[i] = new Tile[tileWidth];
            for (int j = 0; j < tileWidth; j++) {
                Tile.TYPE type = Tile.TYPE.BOX_NULL;
                switch (mapData[i].charAt(j)) {
                    case '1':
                        src = new Rect(0, 0, 23, 23);
                        type = Tile.TYPE.WOOD_BOX;
                        break;
                    case '2':
                        src = new Rect(23, 0, 23 * 2, 23);
                        type = Tile.TYPE.BRICK_BOX;
                        break;
                    case '3':
                        src = new Rect(23 * 25 + 2, 0, 23 * 26, 23);
                        type = Tile.TYPE.COIN_BOX;
                        break;
                    case '4':
                        src = new Rect(0, 24, 23, 24 * 2);
                        type = Tile.TYPE.STEEL_BOX;
                        break;
                    case '6':
                        src = new Rect(23 * 25 + 2, 0, 23 * 26, 23);
                        type = Tile.TYPE.ITEM_BOX;
                        break;
                    case 'c':
                        src = new Rect(23 * 25 + 2, 26, 23 * 26, 23 * 2 + 1);
                        type = Tile.TYPE.ITEM_BOX;
                        break;
                    case 'q':
                        src = new Rect(23 * 25 + 2, 26, 23 * 26, 23 * 2 + 1);
                        type = Tile.TYPE.ITEM_BOX;
                        break;
                    case 't':
                        src = new Rect(0, 23 * 27, 23, 23 * 28);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 'y':
                        src = new Rect(23 * 1, 23 * 27, 23 * 2, 23 * 28);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 'u':
                        src = new Rect(23 * 2, 23 * 27, 23 * 3, 23 * 28);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 'g':
                        src = new Rect(0, 23 * 28, 23, 23 * 29);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 'h':
                        src = new Rect(23 * 1, 23 * 28, 23 * 2, 23 * 29);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 'j':
                        src = new Rect(23 * 2, 23 * 28, 23 * 3, 23 * 29);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 's':
                        src = new Rect(23 * 12 + 10, 22 * 10 - 4, 23 * 14 - 10, 22 * 11 - 2);
                        type = Tile.TYPE.SKY_BOX;
                        break;
                    case 'p':
                        src = new Rect(23 * 17 - 6, 0, 23 * 18 - 7, 23);
                        type = Tile.TYPE.PREFEBS_BOX;
                        break;
                    case 'v':
                        src = new Rect(0, 23 * 9+7, 23, 23 * 10);
                        type = Tile.TYPE.STEEL_BOX;
                        break;
                    case 'b':
                        src = new Rect(0, 23 * 8+8, 23+2, 23 * 9);
                        type = Tile.TYPE.STEEL_BOX;
                        break;
                    case 'n':
                        src = new Rect(23, 23 * 8+8, 23 * 2+2, 23 * 9);
                        type = Tile.TYPE.STEEL_BOX;
                        break;
                    case 'm':
                        src = new Rect(23, 23 * 9+7, 23 * 2, 23 * 10);
                        type = Tile.TYPE.STEEL_BOX;
                        break;
                }
                dst = new Rect(j * TILE_SIZE, i * TILE_SIZE,
                        (j * TILE_SIZE) + TILE_SIZE, (i * TILE_SIZE) + TILE_SIZE);
                tiles[i][j] = new Tile(dst, src, type);
                if (mapData[i].charAt(j) == 'c') {
                    tiles[i][j].setVisible(false);
                }
            }
        }

        src = new Rect(0, 0, 23, 23);
    }

    boolean isOverlap(int l1, int r1, int t1, int b1, int l2, int r2, int t2, int b2) {
        if ((l1 < r2) && (r1 > l2) && (t1 < b2) && (b1 > t2)) {
            return true;
        }
        return false;
    }

    boolean constrainPlayer(Player p) {
        for (int i = 0; i < tileHeight; i++) {
            for (int j = 0; j < tileWidth; j++) {
                Tile t = tiles[i][j];
                if (!t.getVisible()) continue;
                switch (t.getType()) {
                    case COIN_BOX:
                        if (isOverlap(p.getLeft(), p.getRight(), p.getTop(), p.getBottom(), t.getLeft(), t.getRight(), t.getTop(), t.getBottom())) {
                            if (p.onJumpingUp() && p.getTop() > t.getTop()) {
                                if (!tiles[i - 1][j].getVisible()) {
                                    tiles[i - 1][j].setVisible(true);
                                    t.setVisible(false);
                                }
                            }
                            return true;
                        }
                        break;
                    case BRICK_BOX:
                        if (isOverlap(p.getLeft(), p.getRight(), p.getTop(), p.getBottom(), t.getLeft(), t.getRight(), t.getTop(), t.getBottom())) {
                            if (p.onJumpingUp() && p.getTop() > t.getTop()) {
                                t.setVisible(false);
                                MainActivity.playSound(4);
                            }
                            return true;
                        }
                        break;
                    case WOOD_BOX:
                        if (isOverlap(p.getLeft(), p.getRight(), p.getTop(), p.getBottom(), t.getLeft(), t.getRight(), t.getTop(), t.getBottom())) {
                            return true;
                        }
                        break;
                    case STEEL_BOX:
                        if (isOverlap(p.getLeft(), p.getRight(), p.getTop(), p.getBottom(), t.getLeft(), t.getRight(), t.getTop(), t.getBottom())) {
                            return true;
                        }
                        break;
                    case ITEM_BOX:
                        if (isOverlap(p.getLeft(), p.getRight(), p.getTop(), p.getBottom(), t.getLeft(), t.getRight(), t.getTop(), t.getBottom())) {
                            if (t.getVisible()) {
                                t.setVisible(false);
                                p.getCoin(10);
                            }
                            return false;
                        }
                        break;
                }
            }
        }
        return false;
    }

    boolean constrainEmemy(Enemy p) {
        for (int i = 0; i < tileHeight; i++) {
            for (int j = 0; j < tileWidth; j++) {
                Tile t = tiles[i][j];
                if (!t.getVisible()) continue;
                if (!t.getType().equals(Tile.TYPE.BOX_NULL) && !t.getType().equals(Tile.TYPE.SKY_BOX)) {
                    if (isOverlap(p.getLeft(), p.getRight(), p.getTop(), p.getBottom(), t.getLeft(), t.getRight(), t.getTop(), t.getBottom())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void draw(Canvas canvas) {
        for (int i = 0; i < tileHeight; i++) {
            for (int j = 0; j < tileWidth; j++) {
                Tile tile = tiles[i][j];
                tile.draw(canvas);
            }
        }
    }
}
