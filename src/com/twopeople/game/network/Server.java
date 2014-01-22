package com.twopeople.game.network;

import com.esotericsoftware.kryonet.Connection;
import com.twopeople.game.network.packet.AuthRequest;
import com.twopeople.game.network.packet.AuthResponse;
import com.twopeople.game.network.packet.DisconnectionRequest;
import com.twopeople.game.network.packet.EntityPacket;
import com.twopeople.game.network.packet.Packet;
import com.twopeople.game.network.packet.RunningRequest;
import com.twopeople.game.network.packet.UserResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by podko_000
 * At 17:41 on 13.01.14
 */

public class Server extends NetworkEntity {
    private com.esotericsoftware.kryonet.Server server;
    private Client client;
    private ArrayList<ClientInfo> users = new ArrayList<ClientInfo>();

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

    @Override
    public void connected(Connection c) {

    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof Packet) {
            Packet answer = null;
            if (o instanceof AuthRequest) {
                AuthRequest r = (AuthRequest) o;
                if (getByClientByName(r.nickname) == null) {
                    answer = new AuthResponse(c.getID(), client.getEntities(), client.getBullets());
                    users.add(new ClientInfo(c, r.nickname));
                    System.out.println("User " + r.nickname + " connected. So there are " + users.size()
                                               + " users on server now!");

                    c.sendUDP(answer);
                    server.sendToAllExceptUDP(c.getID(), new UserResponse(r.nickname, c.getID()));
                    return;
                } else {
                    answer = Error.nicknameError();
                    System.out.print(answer);
                }
            } else if (o instanceof RunningRequest) {
                answer = (RunningRequest) o;
            } else if (o instanceof EntityPacket) {//I don't what for.
                answer = (Packet) o;
            }
            if (answer != null) { server.sendToAllExceptUDP(c.getID(), answer); }
        }
    }

    @Override
    public void disconnected(Connection c) {
        server.sendToAllExceptUDP(c.getID(), new DisconnectionRequest(c.getID()));
    }

    private ClientInfo getByClientByName(String name) {
        for (ClientInfo c : users) {
            if (c.getNickname().equals(name)) { return c; }
        }
        return null;
    }
}