package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 13:54 on 29.01.14
 */

public class SpawnResponse extends Packet {
    public int spawnerId;
    public int userId;

    public SpawnResponse() {

    }

    public SpawnResponse(int spawnerId, int userId) {
        this.spawnerId = spawnerId;
        this.userId = userId;
    }
}
