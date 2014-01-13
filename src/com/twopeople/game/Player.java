package com.twopeople.game;

/**
 * Created by Alexey
 * At 7:40 PM on 1/13/14
 */

public class Player extends Entity {
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;

    public Player() {
    }

    public Player(World world, float x, float y) {
        super(world, x, y, WIDTH, HEIGHT);
    }
}