package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Alexey
 * At 7:39 PM on 1/13/14
 */

public class World {
    private GameState game;
    private Random random = new Random();

    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private HashMap<Integer, Entity> bullets = new HashMap<Integer, Entity>();

    public World(GameState game) {
        this.game = game;
    }

    public void init() {
        addPlayer(-1, random.nextInt(600), random.nextInt(400), false);
    }

    public void update(GameContainer gameContainer, int delta) {
        synchronized (entities) {
            updateFromIterator(gameContainer, delta, entities.values().iterator());
        }

        synchronized (bullets) {
            updateFromIterator(gameContainer, delta, bullets.values().iterator());
        }
    }

    public void updateFromIterator(GameContainer container, int delta, Iterator<Entity> it) {
        while (it.hasNext()) {
            Entity entity = it.next();
            entity.update(container, delta);
            if (entity.seeksForRemoval()) {
                it.remove();
            }
        }
    }

    public void render(GameContainer gameContainer, Graphics g) {
        synchronized (entities) {
            renderFromIterator(gameContainer, g, entities.values().iterator());
        }

        synchronized (bullets) {
            renderFromIterator(gameContainer, g, bullets.values().iterator());
        }

        g.setColor(Color.white);
        g.drawString("bullets=" + bullets.size(), 10, 30);
    }

    public void renderFromIterator(GameContainer container, Graphics g, Iterator<Entity> it) {
        while (it.hasNext()) {
            Entity entity = it.next();
            entity.render(container, g);
        }
    }

    // =========
    // Utilities

    public void addEntity(Entity entity, boolean fromReceiver) {
        entities.put(entity.getId(), entity);
        if (!fromReceiver) {
            //send
        }
        System.out.println("Added entity(id=" + entity.getId() + ").");
    }

    public void addPlayer(int userId, float x, float y, boolean fromReceiver) {
        Player player = new Player(this, x, y);
        if (userId != -1) {
            player.setId(userId);
        }
        addEntity(player, fromReceiver);
    }

    public void addBullet(float x, float y, int owner, Vector2f direction, boolean fromReceiver) {
        Bullet bullet = new Bullet(this, x, y, direction);
        bullet.setOwner(owner);
        bullets.put(bullet.getId(), bullet);

        if (!fromReceiver) {
            getGame().getClient().shoot(bullet);
        }
    }

    // =======
    // Getters

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public HashMap<Integer, Entity> getBullets() {
        return bullets;
    }

    public GameState getGame() {
        return game;
    }
}