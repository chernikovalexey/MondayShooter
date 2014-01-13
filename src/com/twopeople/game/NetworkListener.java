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
        Entity.serialId = response.yourId;
        game.setUserId(response.yourId);

        if (!game.isServer()) {
            System.out.println("Receiving entities: " + response.entities.length);
            for (int i = 0, len = response.entities.length; i < len; ++i) {
                Entity entity = response.entities[i];
                System.out.println(entity.getId());
                world.addEntity(entity, true);
            }
        }

        world.init();
    }

    @Override
    public void playerConnected(int id, String nickname) {
        Entity.serialId = id;
        System.out.println("Player joined! " + id);
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
        return world.getBullets().values().toArray(new Bullet[]{});
    }

    @Override
    public void runningStart(float x, float y, int id) {
    }

    @Override
    public void runningEnd(float x, float y, int id) {
    }

    @Override
    public void movingDirectionChanged(float x, float y, float vx, float vy, int id) {
    }

    @Override
    public void headingDirectionChanged(float x, float y, float vx, float vy, int id) {
    }

    @Override
    public void shoot(float x, float y, float vx, float vy, int shooterId) {
        world.addBullet(x, y, shooterId, new Vector2f(vx, vy));
    }

    @Override
    public void disconnected(int id) {
    }
}