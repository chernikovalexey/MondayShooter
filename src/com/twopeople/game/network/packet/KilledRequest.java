package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 13:48 on 29.01.14
 */

public class KilledRequest extends Packet {
    public int killerId;
    public int killedId;
    public int spawnerId;
    public KilledRequest() {

    }

    public KilledRequest(int killer, int spawner) {
        killerId = killer;
        spawnerId = spawner;
    }
}
