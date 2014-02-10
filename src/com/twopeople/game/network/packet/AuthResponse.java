package com.twopeople.game.network.packet;

import com.twopeople.game.entity.Entity;

/**
 * Created by podko_000
 * At 18:26 on 13.01.14
 */

public class AuthResponse extends Packet {
    public int yourId;
    public String levelName;
    public Entity[] bullets;
    public Entity[] users;

    public AuthResponse() {}

    public AuthResponse(int yourId,Entity[] bullets, Entity[] players, String levelName) {
        this.yourId = yourId;
        this.bullets = bullets;
        this.users = players;
        this.levelName = levelName;
    }
}