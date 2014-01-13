package com.twopeople.game.network;

/**
 * Created by podko_000
 * At 17:41 on 13.01.14
 */

public class Client extends NetworkEntity {
    private com.esotericsoftware.kryonet.Client client;
    private Listener listener;

    public Client(Listener listener) {
        this.listener = listener;
        client = new com.esotericsoftware.kryonet.Client();
        register(client.getKryo());
        client.addListener(this);
    }
}