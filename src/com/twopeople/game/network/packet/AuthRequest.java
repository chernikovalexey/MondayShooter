package com.twopeople.game.network.packet;

/**
 * Created by podko_000
 * At 18:21 on 13.01.14
 */

public class AuthRequest extends Packet {
    public String nickname;

    public AuthRequest() {}

    public AuthRequest(String nickname) {this.nickname = nickname;}
}