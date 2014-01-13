package com.twopeople.game.network.packet;

import com.twopeople.game.Entity;

/**
 * Created by podko_000
 * At 18:26 on 13.01.14
 */

public class AuthResponse extends Packet {
    public int yourId;
    public Entity[] bullets;
    public Entity[] entities;

    public AuthResponse() {}

    public AuthResponse(int yourId, Entity[] entities, Entity[] bullets) {
        this.yourId = yourId;
        this.entities = entities;
        this.bullets = bullets;
    }
}