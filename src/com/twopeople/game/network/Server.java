package com.twopeople.game.network;

import com.esotericsoftware.kryonet.Connection;
import com.twopeople.game.network.packet.*;

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
                    answer = new AuthResponse(c.getID(), client.getBullets(), client.getUsers(), client.getLevelName());
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
                ((EntityPacket) o).entity.setConnectionId(c.getID());
                answer = (Packet) o;
            } else if(o instanceof KilledRequest) {
                KilledRequest kr = (KilledRequest)o;
                kr.killedId = c.getID();
                server.sendToAllExceptUDP(c.getID(), kr);
            }



            if (answer != null) { server.sendToAllExceptUDP(c.getID(), answer); }
        }
    }

    @Override
    public void disconnected(Connection c) {
        users.remove(getByClientById(c.getID()));
        server.sendToAllExceptUDP(c.getID(), new DisconnectionRequest(c.getID()));
        c.close();
    }

    private ClientInfo getByClientById(int id) {
        for (ClientInfo c : users) {
            if (c.getConnection().getID()==id) { return c; }
        }
        return  null;
    }

    private ClientInfo getByClientByName(String name) {
        for (ClientInfo c : users) {
            if (c.getNickname().equals(name)) { return c; }
        }
        return null;
    }
}