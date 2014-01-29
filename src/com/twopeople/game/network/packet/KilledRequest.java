package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 13:48 on 29.01.14
 */

public class KilledRequest extends Packet {
    public int killerId;
    public int killedId;
    public KilledRequest() {

    }

    public KilledRequest(int killer) {
        killerId = killer;
    }
}
