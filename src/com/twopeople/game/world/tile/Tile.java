package com.twopeople.game.world.tile;

import com.twopeople.game.Camera;
import org.newdawn.slick.Graphics;

/**
 * Created by Alexey
 * At 9:02 PM on 1/28/14
 */

public class Tile {
    public int id;
    public float x, y;

    public static float width, height;

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

    public boolean isVisible(Camera camera) {
        return camera.isVisible(x, y, width, height);
    }
}