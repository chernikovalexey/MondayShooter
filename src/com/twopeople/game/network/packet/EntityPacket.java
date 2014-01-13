package com.twopeople.game.network.packet;

import com.twopeople.game.Entity;

/**
 * Created by podko_000
 * At 1:01 on 14.01.14
 */

public class EntityPacket extends Packet {
    public Entity entity;
    public EntityPacket() {}
    public EntityPacket(Entity e) {}

}
