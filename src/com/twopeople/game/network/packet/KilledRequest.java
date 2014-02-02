package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 13:48 on 29.01.14
 */

public class KilledRequest extends Packet {
    public int killerId;
    public int killedId;
    public float spawnerX;
    public float spawnerY;

    public KilledRequest() {
    }

    public KilledRequest(int killer, float spawnerX, float spawnerY) {
        killerId = killer;
        this.spawnerX = spawnerX;
        this.spawnerY = spawnerY;
    }
}