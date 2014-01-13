package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 23:19 on 13.01.14
 */

public class UserResponse extends Packet {
    public String nickname;
    public int userId;

    public UserResponse(String name, int userId) {
        this.nickname = name;
        this.userId = userId;
    }

    public UserResponse() {}
}
