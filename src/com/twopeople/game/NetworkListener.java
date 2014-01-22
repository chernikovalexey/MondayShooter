package com.twopeople.game;

import com.twopeople.game.network.Listener;
import com.twopeople.game.network.packet.AuthResponse;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 11:01 PM on 1/13/14
 */

public class NetworkListener implements Listener {
    private GameState game;
    private World world;

    public NetworkListener(GameState game, World world) {
        this.game = game;
        this.world = world;
    }

    @Override
    public void connectionSuccess(AuthResponse response) {
        game.setUserId(response.yourId + 1);
        Entity.serialId = response.yourId;

        if (!game.isServer()) {
            for (int i = 0, len = response.entities.length; i < len; ++i) {
                Entity entity = response.entities[i];
                entity.setWorld(world);
                world.addEntity(entity, true);
            }

            for (int i = 0, len = response.bullets.length; i < len; ++i) {
                Bullet bullet = (Bullet) response.bullets[i];
                bullet.setWorld(world);
                world.addBullet(bullet.getX(), bullet.getY(), world.getEntityById(bullet.getOwner()), bullet.getMovingVector(), true);
            }
        }

        world.init();
    }

    @Override
    public void addEntity(Entity entity) {
        entity.setWorld(world);
        world.addEntity(entity, true);
    }

    @Override
    public void playerConnected(int id, String nickname) {
        Entity.serialId = id;
//        System.out.println("Player " + id + " joined!");
    }

    @Override
    public void connectionFailed(com.twopeople.game.network.Error e) {
    }

    @Override
    public Entity[] getEntities() {
        return world.getEntities().values().toArray(new Entity[]{});
    }

    @Override
    public Entity[] getBullets() {
        return world.getBullets().toArray(new Bullet[]{});
    }

    private Entity updateEntityState(int id, float x, float y) {
        Entity entity = world.getEntityById(id);
        entity.setX(x);
        entity.setY(y);
        return entity;
    }

    @Override
    public void movingDirectionChanged(float x, float y, float vx, float vy, int id) {
        updateEntityState(id, x, y).setMovingVector(new Vector2f(vx, vy));
    }

    @Override
    public void headingDirectionChanged(float x, float y, float vx, float vy, int id) {
        updateEntityState(id, x, y).setHeadingVector(new Vector2f(vx, vy));
    }

    @Override
    public void shoot(float x, float y, float vx, float vy, int shooterId) {
        world.addBullet(x, y, world.getEntityById(shooterId), new Vector2f(vx, vy), true);
    }

    @Override
    public void disconnected(int id) {
        world.removeEntityById(id);
    }
}