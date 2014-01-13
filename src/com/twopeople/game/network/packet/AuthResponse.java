package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 18:26 on 13.01.14
 */

public class AuthResponse extends Packet {
    public int yourId;

    public AuthResponse() {}

    public AuthResponse(int yourId) {this.yourId = yourId;}
}