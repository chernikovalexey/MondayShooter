package com.twopeople.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Alexey
 * At 7:39 PM on 1/13/14
 */

public class World {
    private GameState game;
    private Random random = new Random();

    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private HashMap<Integer, Bullet> bullets = new HashMap<Integer, Bullet>();

    public World(GameState game) {
        this.game = game;

        addPlayer(1, 300f, 200f);
    }

    public void init() {
    }

    public void update(GameContainer gameContainer, int i) {
        synchronized (entities) {
            for (Entity entity : entities.values()) {
                entity.update(gameContainer, i);
            }
        }

        synchronized (bullets) {
            for (Bullet bullet : bullets.values()) {
                bullet.update(gameContainer, i);
            }
        }
    }

    public void render(GameContainer gameContainer, Graphics g) {
        synchronized (entities) {
            for (Entity entity : entities.values()) {
                entity.render(gameContainer, g);
            }
        }

        synchronized (bullets) {
            for (Bullet bullet : bullets.values()) {
                bullet.render(gameContainer, g);
            }
        }
    }

    // =========
    // Utilities

    public void addPlayer(int userId, float x, float y) {
        Player player = new Player(this, x, y);
        entities.put(player.getId(), player);

    }

    public void addBullet(float x, float y, int owner, Vector2f direction) {
        Bullet bullet = new Bullet(this, x, y, direction);
        bullet.setOwner(owner);
        bullets.put(bullet.getId(), bullet);
    }
}