package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 22:22 on 13.01.14
 */

public class DisconnectionRequest {
    public int userId;

    public DisconnectionRequest() {}

    public DisconnectionRequest(int userId) {this.userId = userId;}
}
