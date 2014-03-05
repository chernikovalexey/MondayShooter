package com.twopeople.game;

/**
 * Created by Alexey
 * At 9:34 PM on 2/27/14
 */

public class Team {
    public final static int NEUTRAL = 0;
    public final static int REBELS = 1;
    public final static int MERCENARIES = 2;

    public int id;

    public Team(int id) {
        this.id = id;
    }
}