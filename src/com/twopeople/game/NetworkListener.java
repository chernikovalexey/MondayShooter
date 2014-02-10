package com.twopeople.game;

import com.twopeople.game.entity.Bullet;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Player;
import com.twopeople.game.network.Listener;
import com.twopeople.game.network.packet.AuthResponse;
import com.twopeople.game.world.World;
import org.lwjgl.util.vector.Vector3f;
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
        game.setUserId(response.yourId);
        Entity.connectionSerialId = response.yourId;
        System.out.println("connection id="+Entity.connectionSerialId);
        Entity.serialId += response.yourId;

        if (!game.isServer()) {
            System.out.println("Ids on the client start from " + response.yourId);

            for (int i = 0, len = response.users.length; i < len; ++i) {
                Player player = (Player) response.users[i];
                world.addPlayer(player.getConnectionId(), player.getX(), player.getY(), true);
            }

            for (int i = 0, len = response.bullets.length; i < len; ++i) {
                Bullet bullet = (Bullet) response.bullets[i];
                bullet.setWorld(world);
                world.addBullet(bullet);
            }
        }

        world.init();
    }

    @Override
    public void addEntity(Entity entity) {
        world.addEntity(entity, true);
    }

    @Override
    public void onUserKilled(int killed, int killer, float spawnerX, float spawnerY) {
        System.out.println("User " + killed + " has been killed by " + killer);

        /*Player killedPlayer = (Player) world.getEntityByConnectionId(killed);
        Player killerPlayer = (Player) world.getEntityByConnectionId(killer);

        killedPlayer.respawnAt(new Vector2f(spawnerX, spawnerY));

        // todo
        // score leading*/
    }

    @Override
    public void playerConnected(int id, String nickname) {
        Entity.serialId = id;
        System.out.println(id + " " + nickname);
    }

    @Override
    public void connectionFailed(com.twopeople.game.network.Error e) {
    }

    @Override
    public String getLevelName() {
        return "untitled.tmx";
    }

    @Override
    public Bullet[] getBullets() {
        return world.getBullets().getAll().toArray(new Bullet[]{});
    }

    @Override
    public Entity[] getUsers() {
        return world.getUsers().toArray(new Entity[]{});
    }

    private Entity updateEntityState(int id, float x, float y) {
        Entity entity = world.getEntityByConnectionId(id);
        entity.setX(x);
        entity.setY(y);
        return entity;
    }

    @Override
    public void movingDirectionChanged(float x, float y, float vx, float vy, int id) {
        updateEntityState(id, x, y).setMovingVector(new Vector3f(vx, vy, 0f));
    }

    @Override
    public void headingDirectionChanged(float x, float y, float vx, float vy, int id) {
        updateEntityState(id, x, y).setHeadingVector(new Vector2f(vx, vy));
    }

    @Override
    public void shoot(float x, float y, float vx, float vy, int shooterId) {
        world.addBullet(x, y, 42, world.getEntityByConnectionId(shooterId), new Vector2f(vx, vy), true);
    }

    @Override
    public void disconnected(int id) {
        world.removeEntityById(id);
    }
}