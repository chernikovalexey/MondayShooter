package com.twopeople.game.network.packet;

import com.twopeople.game.Bullet;
import com.twopeople.game.Entity;

/**
 * Created by podko_000
 * At 18:26 on 13.01.14
 */

public class AuthResponse extends Packet {
    public int yourId;
    public String levelName;
    public Entity[] bullets;

    public AuthResponse() {}

    public AuthResponse(int yourId,Entity[] bullets, String levelName) {
        this.yourId = yourId;
        this.bullets = bullets;
        this.levelName = levelName;
    }
}