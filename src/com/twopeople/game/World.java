package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Alexey
 * At 7:39 PM on 1/13/14
 */

public class World {
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 30;
    public static final int TILES_X = 64;
    public static final int TILES_Y = 64;

    private GameState game;
    private Random random = new Random();

    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private ArrayList<Entity> bullets = new ArrayList<Entity>();

    private Comparator<Entity> entitySorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity entity1, Entity entity2) {
            if (entity1.getY() + entity1.getHeight() >= entity2.getY() + entity2.getHeight() / 2) {
                return 1;
            } else if (entity1.getY() + entity1.getHeight() < entity2.getY() + entity2.getHeight() / 2) {
                return -1;
            }

            if (entity1.getLayer() > entity2.getLayer()) {
                return 1;
            } else if (entity1.getLayer() < entity2.getLayer()) {
                return -1;
            }
            return 0;
        }
    };

    public World(GameState game) {
        this.game = game;
    }

    public void init() {
        Player player = addPlayer(-1, random.nextInt(TILES_X / 2) * TILE_WIDTH / 3, random.nextInt(TILES_Y - TILES_Y / 7) * TILE_HEIGHT / 3, false);
        game.getCamera().alignCenterOn(player);

        //

        Wall wall1 = new Wall(140, 140);
        addEntity(wall1, false);

        Wall wall2 = new Wall(240, 140);
        addEntity(wall2, false);

        Wall wall3 = new Wall(340, 140);
        addEntity(wall3, false);

        Wall wall4 = new Wall(40, 240);
        addEntity(wall4, false);

        Wall wall5 = new Wall(140, 340);
        addEntity(wall5, false);

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

        for (int x = 0; x < TILES_X; ++x) {
            for (int y = 0; y < TILES_Y + (TILES_Y - 1) * TILE_HEIGHT / 7; ++y) {
                int xt = x + (y >> 1) + y & 1;
                int yt = (y >> 1) - x;

                float tileX = x * TILE_WIDTH;
                float tileY = y * TILE_HEIGHT - y * 14;

                if (game.getCamera().isVisible(tileX, tileY, TILE_WIDTH, TILE_HEIGHT)) {
                    g.drawImage(Images.tiles.getSprite(((yt ^ xt) & 1) == 0 ? 0 : 1, 0).getSubImage(0, 34, TILE_WIDTH, TILE_HEIGHT), camera.getX(tileX + (y & 1) * TILE_WIDTH / 2), camera.getY(tileY));
                }
            }
        }

        g.setColor(Color.red);
        g.drawRect(camera.getX(0), camera.getY(0), TILES_X * TILE_WIDTH, TILES_Y * TILE_HEIGHT);

        synchronized (entities) {
            ArrayList<Entity> sorted = new ArrayList<Entity>();
            Iterator<Entity> it = entities.values().iterator();
            while (it.hasNext()) {
                sorted.add(it.next());
            }
            Collections.sort(sorted, entitySorter);
            renderFromIterator(gameContainer, g, sorted.iterator());
        }

        synchronized (bullets) {
            renderFromIterator(gameContainer, g, bullets.iterator());
        }

        g.setColor(Color.white);
        g.drawString("entities=" + entities.size(), 10, 50);
        g.drawString("bullets=" + bullets.size(), 10, 70);
    }

    public void renderFromIterator(GameContainer container, Graphics g, Iterator<Entity> it) {
        while (it.hasNext()) {
            Entity entity = it.next();
            entity.render(container, game.getCamera(), g);
        }
    }

    // =========
    // Utilities

    public float getWidth() {
        return TILES_X * TILE_WIDTH;
    }

    public float getHeight() {
        return TILES_Y * TILE_HEIGHT;
    }

    public boolean ranOut(Entity entity) {
        return entity.getX() < 0 || entity.getY() < 0 || entity.getX() > getWidth() || entity.getY() > getHeight();
    }

    public Entity addEntity(Entity entity, boolean fromReceiver) {
        if (!entity.hasWorld()) {
            entity.setWorld(this);
        }

        synchronized (entities) {
            entities.put(entity.getId(), entity);
        }

        if (!fromReceiver) {
            getGame().getClient().sendEntity(entity);
        }

        return entity;
    }

    public Player addPlayer(int userId, float x, float y, boolean fromReceiver) {
        Player player = new Player(x, y);
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