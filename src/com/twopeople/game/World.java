package com.twopeople.game;

import com.twopeople.game.particle.ParticleManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.tiled.TiledMap;

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
    private int tilesX, tilesY;

    private GameState game;
    private Random random = new Random();

    private ArrayList<Spawner> spawners = new ArrayList<Spawner>();
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private ParticleManager particles = new ParticleManager(this);
    private ArrayList<Entity> bullets = new ArrayList<Entity>();

    private boolean loaded = false;
    public static int lastUsedLayer;

    private Comparator<Entity> entitySorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity entity1, Entity entity2) {
            if (entity1.getBBCentre().getY() + entity1.getHeight() / 6 >= entity2.getBBCentre().getY()) {
                return 1;
            } else if (entity1.getBBCentre().getY() + entity1.getHeight() / 6 < entity2.getBBCentre().getY()) {
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

//        spawners.add(new Spawner(0, 0));
//        spawners.add(new Spawner(140, 120));
//        spawners.add(new Spawner(240, 340));
//        spawners.add(new Spawner(120, 200));
    }

    public void init() {
        loadMap("map");

        Player player = addPlayer(-1, random.nextInt(400), random.nextInt(340), false);
        player.respawn();
        game.getCamera().alignCenterOn(player);
    }

    public void update(GameContainer gameContainer, int delta) {
        if (!loaded) {

            loaded = true;
        }

        synchronized (entities) {
            updateFromIterator(gameContainer, delta, entities.values().iterator());
        }

        for (ParticleSystem system : particles.list.values()) {
            system.update(delta);
        }

        synchronized (bullets) {
            updateFromIterator(gameContainer, delta, bullets.iterator());
        }

        System.out.println("spawners="+spawners.size());
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

        for (Tile tile : tiles) {
            if (tile.isVisible(camera)) {
                tile.render(camera, g);
            }
        }

        for (ParticleSystem system : particles.list.values()) {
            system.render();
        }

        ArrayList<Entity> sorted = new ArrayList<Entity>();

        synchronized (entities) {
            Iterator<Entity> it = entities.values().iterator();
            while (it.hasNext()) {
                sorted.add(it.next());
            }
        }

        synchronized (bullets) {
            Iterator<Entity> it = bullets.iterator();
            renderFromIterator(gameContainer, g, it);
            while (it.hasNext()) {
                sorted.add(it.next());
            }
        }

        Collections.sort(sorted, entitySorter);
        renderFromIterator(gameContainer, g, sorted.iterator());

        g.setColor(Color.white);
        g.drawString("blood particles=" + particles.get(ParticleManager.BLOOD_DEBRIS).getParticleCount(), 10, 30);
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
        return tilesX * Tile.width;
    }

    public float getHeight() {
        return tilesY * Tile.height;
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

    public void loadMap(String level) {
        try {
            long time = System.currentTimeMillis();
            TiledMap map = new TiledMap("res/maps/" + level + ".tmx");

            Tile.setSize(map.getTileWidth(), map.getTileHeight());
            tilesX = map.getWidth();
            tilesY = map.getHeight();

            int tw = map.getTileWidth();
            int th = map.getTileHeight();

            tiles.clear();
            removeEntitiesByIdExcept(new int[]{getGame().getUserId()});

            for (int layer = 0; layer < map.getLayerCount(); ++layer) {
                for (int x = 0, oddOffset = 1, evenOffset = 0; x < map.getWidth(); ++x, oddOffset = 1, evenOffset = 0) {
                    for (int y = 0; y < map.getHeight(); ++y) {
                        int id = map.getTileId(x, y, layer);
                        boolean odd = y % 2 != 0;

                        if (id != 0) {
                            float xt, yt;
                            String type = map.getTileProperty(id, "type", "");
                            String[] rawSkin = map.getTileProperty(id, "skin", "0,0").split(",");
                            int skin[] = new int[]{Integer.parseInt(rawSkin[0]), Integer.parseInt(rawSkin[1])};

                            if (odd) {
                                xt = x * tw + tw / 2;
                                yt = (y - oddOffset++) * th + th / 2;
                            } else {
                                xt = x * tw;
                                yt = (y - evenOffset++) * th;
                            }

                            if (type.equals("spawner")) {
                                spawners.add(new Spawner(xt,yt));
                            } else if (EntityLoader.has(type)) {
                                Entity entity = EntityLoader.getEntityInstanceByName(type, new Object[]{xt, yt - 61}, skin);
                                addEntity(entity, true);
                            } else {
                                TileList.addTile(id, map.getTileImage(x, y, layer));
                                tiles.add(new Tile(xt, yt, id));
                            }
                        } else {
                            if (odd) {
                                oddOffset++;
                            } else {
                                evenOffset++;
                            }
                        }
                    }
                }
            }

            System.out.println("Level parsed in " + (System.currentTimeMillis() - time) + " ms");
        } catch (SlickException e) {
            e.printStackTrace();
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

    public void removeEntitiesByIdExcept(int[] ids) {
        synchronized (entities) {
            Iterator<Entity> it = entities.values().iterator();
            while (it.hasNext()) {
                Entity entity = it.next();
                boolean found = false;
                for (int id : ids) {
                    if (entity.getId() == id) {
                        found = true;
                    }
                }
                if (!found) {
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

    public ParticleSystem getParticleSystem(int id) {
        return particles.get(id);
    }

    public Spawner getRandomSpawner() {
        return spawners.get(random.nextInt(spawners.size()));
    }
}