package com.twopeople.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Alexey
 * At 9:02 PM on 1/28/14
 */

public class Tile {
    public int id;
    public float x, y;

    private static float width, height;

    public static void setSize(float w, float h) {
        width = w;
        height = h;
    }

    public Tile(float x, float y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void render(Camera camera, Graphics g) {
        g.drawImage(TileList.getTile(id), camera.getX(x), camera.getY(y));
    }

    public Shape[] getSkeleton() {
        return new Shape[]{
                new Polygon(new float[]{x, y + height / 2, x + width / 2, y + 3, x + width, y + height / 2}),
                new Polygon(new float[]{x, y + height / 2, x + width, y + height / 2, x + width / 2, y + height}),
        };
    }

    public boolean isVisible(Camera camera) {
        return camera.isVisible(x, y, width, height);
    }
}