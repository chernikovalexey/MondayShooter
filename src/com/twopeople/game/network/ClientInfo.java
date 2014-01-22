package com.twopeople.game.network;

import com.esotericsoftware.kryonet.Connection;

/**
 * Created by podko_000
 * At 20:44 on 13.01.14
 */

public class ClientInfo {
    private Connection connection;
    private String nickname;

    public ClientInfo(Connection connection, String nickname) {
        this.connection = connection;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Connection getConnection() {
        return connection;
    }
}