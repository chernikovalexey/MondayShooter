package com.twopeople.game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by Alexey
 * At 8:18 PM on 1/24/14
 */

public class Images {
    public static SpriteSheet walls;
    public static SpriteSheet player;
    public static SpriteSheet particles;
    public static SpriteSheet gunshots;

    // Load separately to handle exceptions
    public static void init() {
        try {
            walls = new SpriteSheet("res/walls.png", 128, 126);
            player = new SpriteSheet("res/player_combine01_fixed.png", 70, 70);
            particles = new SpriteSheet("res/particles.png", 8, 8);
            gunshots = new SpriteSheet("res/gunshots.png", 4, 4);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}