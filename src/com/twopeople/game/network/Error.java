package com.twopeople.game.network;

import com.twopeople.game.network.packet.Packet;

/**
 * Created by podko_000
 * At 20:28 on 13.01.14
 */

public class Error extends Packet {
    private static final int NICKNAME_ERROR = 1;
    public int code;
    public String message;

    private Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Error() {}

    public static Error nicknameError()
    {
        return new Error(NICKNAME_ERROR, "Nickname is not free. Choose another/");
    }

    @Override public String toString() {
        return "Error@#"+code+"|{"+message+"}";
    }
}
