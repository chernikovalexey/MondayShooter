package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Alexey
 * At 7:39 PM on 1/13/14
 */

public class World {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 15;
    public static final int TILES_X = 128;
    public static final int TILES_Y = 128;

    private GameState game;
    private Random random = new Random();

    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private ArrayList<Entity> bullets = new ArrayList<Entity>();

    public World(GameState game) {
        this.game = game;
    }

    public void init() {
        Player player = addPlayer(-1, random.nextInt(TILES_X / 2) * TILE_WIDTH/3, random.nextInt(TILES_Y - TILES_Y / 7) * TILE_HEIGHT/3, false);
        game.getCamera().alignCenterOn(player);
    }

    public void update(GameContainer gameContainer, int delta) {
        synchronized (entities) {
            updateFromIterator(gameContainer, delta, entities.values().iterator());
        }

        synchronized (bullets) {
            updateFromIterator(gameContainer, delta, bullets.iterator());
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
        Camera camera = game.getCamera();

        g.scale(3f, 3f);

        for (int x = 0; x < TILES_X; ++x) {
            for (int y = 0; y < TILES_Y; ++y) {
                int xt = x + (y >> 1) + y & 1;
                int yt = (y >> 1) - x;

                float tileX = x * TILE_WIDTH;
                float tileY = y * TILE_HEIGHT;

                if (game.getCamera().isVisible(tileX, tileY, TILE_WIDTH, TILE_HEIGHT)) {
                    g.drawImage(Images.tiles.getSprite(((yt ^ xt) & 1) == 0 ? 0 : 1, 0), camera.getX(tileX + (y & 1) * TILE_WIDTH / 2), camera.getY(tileY - y * 7));
                }
            }
        }

        synchronized (entities) {
            renderFromIterator(gameContainer, g, entities.values().iterator());
        }

        synchronized (bullets) {
            renderFromIterator(gameContainer, g, bullets.iterator());
        }

        g.scale(0.35f, 0.35f);

        g.setColor(Color.white);
        g.drawString("bullets=" + bullets.size(), 10, 30);
    }

    public void renderFromIterator(GameContainer container, Graphics g, Iterator<Entity> it) {
        while (it.hasNext()) {
            Entity entity = it.next();
            entity.render(container, game.getCamera(), g);
        }
    }

    // =========
    // Utilities

    public Entity addEntity(Entity entity, boolean fromReceiver) {
        synchronized (entities) {
            entities.put(entity.getId(), entity);
        }

        if (!fromReceiver) {
            getGame().getClient().sendEntity(entity);
        }

        return entity;
    }

    public Player addPlayer(int userId, float x, float y, boolean fromReceiver) {
        Player player = new Player(this, x, y);
        if (userId != -1) {
            player.setId(userId);
        }
        addEntity(player, fromReceiver);
        return player;
    }

    public void addBullet(float x, float y, Entity shooter, Vector2f direction, boolean fromReceiver) {
        Bullet bullet = new Bullet(this, x, y, direction);
        bullet.setOwner(shooter.getId());

        synchronized (bullets) {
            bullets.add(bullet);
        }

        if (!fromReceiver) {
            getGame().getClient().shoot(shooter);
        }
    }

    // =======
    // Getters

    public Entity getEntityById(int id) {
        return entities.get(id);
    }

    public void removeEntityById(int id) {
        entities.remove(id);

        synchronized (bullets) {
            Iterator<Entity> it = bullets.iterator();
            while (it.hasNext()) {
                Entity bullet = it.next();
                if (bullet.getOwner() == id) {
                    it.remove();
                }
            }
        }
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public ArrayList<Entity> getBullets() {
        return bullets;
    }

    public GameState getGame() {
        return game;
    }
}