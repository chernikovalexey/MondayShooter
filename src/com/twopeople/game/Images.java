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
    public static SpriteSheet bullets;
    public static SpriteSheet concrete_chipping;
    public static SpriteSheet railcar;
    public static SpriteSheet pumphouse;

    // Load separately to handle exceptions
    public static void init() {
        try {
            walls = new SpriteSheet("res/walls.png", 128, 126);
            player = new SpriteSheet("res/player_atlas.png", 64, 64);
            particles = new SpriteSheet("res/particles.png", 8, 8);
            gunshots = new SpriteSheet("res/gunshots.png", 1, 1);
            bullets = new SpriteSheet("res/bullets.png", 5, 5);
            concrete_chipping = new SpriteSheet("res/concrete_chipping.png", 2, 2);
            railcar = new SpriteSheet("res/railcar.png", 70, 70);
            pumphouse = new SpriteSheet("res/pumphouse.png", 240, 240);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}