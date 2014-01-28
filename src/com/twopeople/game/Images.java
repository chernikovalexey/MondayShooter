package com.twopeople.game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by Alexey
 * At 8:18 PM on 1/24/14
 */

public class Images {
    public static SpriteSheet tiles;
    public static SpriteSheet player;

    // Load separately to handle exceptions
    static {
        try {
            tiles = new SpriteSheet("res/tiles.png", 64, 64);
            player = new SpriteSheet("res/player_combine01_fixed.png", 70, 70);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}