package com.twopeople.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Alexey
 * At 7:39 PM on 1/13/14
 */

public class World {
    private GameState game;
    private Random random = new Random();

    private HashMap<Integer, Player> players = new HashMap<Integer, Player>();

    public World(GameState game) {
        this.game = game;
    }

    public void init() {
    }

    public void update(GameContainer gameContainer, int i) {}

    public void render(GameContainer gameContainer, Graphics g) {

    }

    // =========
    // Utilities

    public void addPlayer(int userId, float x, float y) {
        Player player = new Player(this, x, y);
    }
}