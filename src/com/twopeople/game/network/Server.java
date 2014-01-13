package com.twopeople.game.network;

import java.io.IOException;

/**
 * Created by podko_000
 * At 17:41 on 13.01.14
 */

public class Server extends NetworkEntity {
    private com.esotericsoftware.kryonet.Server server;
    private Client client;

    public Server() {
        server = new com.esotericsoftware.kryonet.Server();
        register(server.getKryo());
        server.start();
        try {
            server.bind(TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(this);

        System.out.println("Server successfully started");
    }

    public void setClient(Client c) {
        client = c;
    }
}