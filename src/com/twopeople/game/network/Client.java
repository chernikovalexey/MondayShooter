package com.twopeople.game.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.twopeople.game.network.packet.*;
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

    public void connect(String ip, String nickname) {
        client.start();
        try {
            client.connect(250, ip, TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        client.sendUDP(new AuthRequest(nickname));
    }

    @Override public void received(Connection c, Object o) {
        if(o instanceof Packet) {
            if(o instanceof AuthResponse) {
                listener.connectionSuccess((AuthResponse)o);
            }
        }
    }
}