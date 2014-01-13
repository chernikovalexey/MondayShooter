package com.twopeople.game;

import com.twopeople.game.network.Listener;
import com.twopeople.game.network.packet.AuthResponse;

/**
 * Created by Alexey
 * At 11:01 PM on 1/13/14
 */

public class NetworkListener implements Listener {
    private World world;

    public NetworkListener(World world) {
        this.world = world;
    }

    @Override
    public void connectionSuccess(AuthResponse response) {
    }

    @Override
    public void connectionFailed(com.twopeople.game.network.Error e) {
    }

    @Override
    public Entity[] getState() {
        return new Entity[0];
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
    }

    @Override
    public void disconnected(int id) {
    }
}