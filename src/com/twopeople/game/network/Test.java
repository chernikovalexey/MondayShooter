package com.twopeople.game.network;

import com.twopeople.game.network.packet.AuthResponse;

/**
 * Created by podko_000
 * At 21:01 on 13.01.14
 */

public class Test implements Listener {
    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client(new Test());
        client.connect("localhost", "WORLDking");

        while (true) {}
    }

    @Override
    public void connectionSuccess(AuthResponse response) {
        System.out.println("Connection success. Id="+response.yourId);
    }
}
