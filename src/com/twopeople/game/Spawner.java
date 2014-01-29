package com.twopeople.game;

/**
 * Created by Alexey
 * At 1:59 PM on 1/29/14
 */

public class Spawner {
    private static int serialSpawnerId;

    public int id;
    public float x, y;

    public Spawner(float x, float y) {
        this.x = x;
        this.y = y;
        this.id = ++serialSpawnerId;
    }
}