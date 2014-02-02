package com.twopeople.game.world.tile;

import org.newdawn.slick.Image;

import java.util.HashMap;

/**
 * Created by Alexey
 * At 9:03 PM on 1/28/14
 */

public class TileList {
    // type - image
    public static HashMap<Integer, Image> tiles = new HashMap<Integer, Image>();

    public static final int TILE = 1;
    public static final int STONE = 2;

    public static void addTile(int id, Image image) {
        tiles.put(id, image);
    }

    public static Image getTile(int id) {
        return tiles.get(id);
    }
}