package com.twopeople.game.world;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.GameState;
import com.twopeople.game.IEntity;
import com.twopeople.game.entity.Bullet;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.EntityLoader;
import com.twopeople.game.entity.Player;
import com.twopeople.game.particle.MSParticleSystem;
import com.twopeople.game.particle.ParticleManager;
import com.twopeople.game.world.tile.Tile;
import com.twopeople.game.world.tile.TileList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private ArrayList<Vector2f> spawners = new ArrayList<Vector2f>();
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private EntityVault entities = new EntityVault(12800, 12800);
    private EntityVault bullets = new EntityVault(12800, 12800);
    private ParticleManager particles = new ParticleManager(this);

    private boolean canLoad = false;

    private Comparator<IEntity> entitySorter = new Comparator<IEntity>() {
        @Override
        public int compare(IEntity entity1, IEntity entity2) {
            float e1y = entity1.getBBCentre().getY();
            float e2y = entity2.getBBCentre().getY();

            if ((entity1.getWidth() * entity1.getHeight()) / (entity2.getWidth() * entity2.getHeight()) < 1f) {
                e1y -= entity1.getZ();
                e2y -= entity2.getZ();
            }

            if (e1y > e2y) {
                return 1;
            } else if (e1y < e2y) {
                return -1;
            }

            return 0;
        }
    };

    public World(GameState game) {
        this.game = game;
    }

    public void init() {
        canLoad = true;
    }

    public void update(GameContainer gameContainer, int delta) {
        final long time = System.currentTimeMillis();

        if (canLoad) {
            loadMap("dm_map01");
            createPlayer();
            canLoad = false;
        }

        updateFromIterator(gameContainer, delta, entities.getAll().iterator(), entities);
        updateFromIterator(gameContainer, delta, bullets.getAll().iterator(), bullets);

        for (ParticleSystem system : particles.list.values()) {
            system.update(delta);
        }

        //        System.out.println("World updated in " + (System.currentTimeMillis() - time) + " ms");
    }

    public void updateFromIterator(GameContainer container, int delta, Iterator<Entity> it, EntityVault vault) {
        while (it.hasNext()) {
            Entity entity = it.next();

            // Note that regardless value of the `vault` variable, entities are sent to the update method
            entity.update(container, delta, entities);
            if (entity.isRemovalFlagOn()) {
                it.remove();
                vault.remove(entity);
            }
        }
    }

    public void render(GameContainer gameContainer, Graphics g) {
        final long time = System.currentTimeMillis();
        final Camera camera = game.getCamera();

        for (Tile tile : tiles) {
            if (tile.isVisible(camera)) {
                tile.render(camera, g);
            }
        }

        ArrayList<IEntity> sorted = new ArrayList<IEntity>() {{
            addAll(entities.getVisible(camera));
            addAll(bullets.getVisible(camera));
        }};

        int pa = 0;
        for (ParticleSystem system : particles.list.values()) {
            if (system instanceof MSParticleSystem) {
                pa += ((MSParticleSystem) system).getAllParticles().size();
                sorted.addAll(((MSParticleSystem) system).getAllParticles());
            } else {
                pa += system.getParticleCount();
                system.render(0, 0);
            }
        }

        Collections.sort(sorted, entitySorter);
        renderFromIterator(gameContainer, g, sorted.iterator());

        //renderGrid(camera, g, entities);

        g.setColor(Color.white);
        g.drawString("particles=" + pa, 10, 30);
        g.drawString("entities=" + entities.size(), 10, 50);
        g.drawString("bullets=" + bullets.size(), 10, 70);

        //        System.out.println("World rendered in " + (System.currentTimeMillis() - time) + " ms");
    }

    public void renderFromIterator(GameContainer container, Graphics g, Iterator<IEntity> it) {
        while (it.hasNext()) {
            IEntity entity = it.next();
            entity.render(container, game.getCamera(), g); // others
        }
    }

    private void renderGrid(Camera camera, Graphics g, EntityVault vault) {
        g.setColor(Color.white);

        for (float x = 0; x < vault.xCells * EntityVault.EntityVaultCell.WIDTH; x += EntityVault.EntityVaultCell.WIDTH) {
            for (float y = 0; y < vault.yCells * EntityVault.EntityVaultCell.HEIGHT; y += EntityVault.EntityVaultCell.HEIGHT) {
                if (camera.isVisible(x, y, EntityVault.EntityVaultCell.WIDTH, EntityVault.EntityVaultCell.HEIGHT)) {
                    g.drawLine(camera.getX(x), camera.getY(y), camera.getX(x + EntityVault.EntityVaultCell.WIDTH), camera.getY(y));
                    g.drawLine(camera.getX(x), camera.getY(y), camera.getX(x), camera.getY(y + EntityVault.EntityVaultCell.HEIGHT));
                    int cell = ((int) (x / EntityVault.EntityVaultCell.WIDTH + y / EntityVault.EntityVaultCell.HEIGHT * vault.xCells));
                    g.drawString("(" + x / EntityVault.EntityVaultCell.WIDTH + ", " + y / EntityVault.EntityVaultCell.HEIGHT + ") items=" + vault.getCell(cell).getEntities().size(), camera.getX(x + 15), camera.getY(y + 15));
                }
            }
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

        entities.add(entity);

        if (!fromReceiver) {
            getGame().getClient().sendEntity(entity);
        }

        return entity;
    }

    public Player createPlayer() {
        Player player = new Player(0, 0);
        player.setWorld(this);
        player.respawn();
        return addPlayer(Entity.connectionSerialId, player, false);
    }

    public Player addPlayer(int connectionId, Player player, boolean fromReceiver) {
        if (connectionId != -1) {
            player.setConnectionId(connectionId);
        }
        addEntity(player, fromReceiver);
        return player;
    }

    public Player addPlayer(int connectionId, float x, float y, boolean fromReceiver) {
        return addPlayer(connectionId, new Player(x, y), fromReceiver);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void addBullet(float x, float y, float z, Entity shooter, Vector2f direction, boolean fromReceiver) {
        Bullet bullet = new Bullet(this, x, y, z, direction);
        bullet.setOwner(shooter.getConnectionId());

        addBullet(bullet);

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
                            Image image = map.getTileImage(x, y, layer);
                            String type = map.getTileProperty(id, "type", "");
                            String[] rawSkin = map.getTileProperty(id, "skin", "0,0").split(",");
                            int skin[] = new int[]{Integer.parseInt(rawSkin[0]), Integer.parseInt(rawSkin[1])};

                            if (odd) {
                                xt = x * tw + tw / 2;
                                yt = (y - oddOffset) * th + th / 2;
                            } else {
                                xt = x * tw;
                                yt = (y - evenOffset) * th;
                            }

                            if (type.equals("spawner")) {
                                spawners.add(new Vector2f(xt, yt));
                            } else if (EntityLoader.has(type)) {
                                Entity entity = EntityLoader.getEntityInstanceByName(type, new Object[]{xt, yt}, image, skin);
                                entity.setY(entity.getY() - entity.getHeight());
                                addEntity(entity, true);
                            } else {
                                TileList.addTile(id, image);
                                tiles.add(new Tile(xt, yt, id));
                            }
                        }

                        if (odd) {
                            oddOffset++;
                        } else {
                            evenOffset++;
                        }
                    }
                }
            }

            System.out.println("Level parsed in " + (System.currentTimeMillis() - time) + " ms");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void removeEntityByConnectionId(int cid) {
        Entity entity = getEntityByConnectionId(cid);
        entities.remove(entity);

        Iterator<Entity> it = bullets.getAll().iterator();
        while (it.hasNext()) {
            Entity bullet = it.next();
            if (bullet.getOwner() == entity.getId()) {
                it.remove();
                bullets.remove(bullet);
            }
        }
    }

    public void removeEntitiesByIdExcept(int[] ids) {
        Iterator<Entity> it = entities.getAll().iterator();
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
                entities.remove(entity);
            }
        }
    }

    // =======
    // Getters

    public ArrayList<Entity> getUsers() {
        ArrayList<Entity> players = new ArrayList<Entity>();
        for (Entity entity : entities.getAll()) {
            if (entity instanceof Player) {
                players.add(entity);
            }
        }
        return players;
    }

    public Entity getEntityByConnectionId(int cid) {
        for (Entity entity : entities.getAll()) {
            if (entity.getConnectionId() == cid) {
                return entity;
            }
        }
        return null;
    }

    public EntityVault getEntities() {
        return entities;
    }

    public EntityVault getBullets() {
        return bullets;
    }

    public GameState getGame() {
        return game;
    }

    public Random getRandom() {
        return random;
    }

    public ParticleSystem getParticleSystem(int id) {
        return particles.get(id);
    }

    public Vector2f getRandomSpawner() {
        if (spawners.size() == 0) {
            return null;
        }
        return spawners.get(random.nextInt(spawners.size()));
    }
}