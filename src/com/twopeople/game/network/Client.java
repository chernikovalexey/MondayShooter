package com.twopeople.game.network;

import com.esotericsoftware.kryonet.Connection;
import com.twopeople.game.Entity;
import com.twopeople.game.network.packet.AuthRequest;
import com.twopeople.game.network.packet.AuthResponse;
import com.twopeople.game.network.packet.DisconnectionRequest;
import com.twopeople.game.network.packet.EntityPacket;
import com.twopeople.game.network.packet.Packet;
import com.twopeople.game.network.packet.RunningRequest;
import com.twopeople.game.network.packet.UserResponse;

import java.io.IOException;

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
            e.printStackTrace();
        }

        client.sendUDP(new AuthRequest(nickname));
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof Packet) {
            if (o instanceof AuthResponse) {
                listener.connectionSuccess((AuthResponse) o);
            } else if (o instanceof Error) {
                Error e = (Error) o;
                switch (e.code) {
                    case Error.NICKNAME_ERROR:
                        listener.connectionFailed(e);
                        break;
                }
            } else if (o instanceof RunningRequest) {
                RunningRequest r = (RunningRequest) o;
                switch (r.type) {
                    case RunningRequest.DIRECTION:
                        listener.movingDirectionChanged(r.x, r.y, r.vx, r.vy, r.id);
                        break;
                    case RunningRequest.HEAD_DIRECTION:
                        listener.headingDirectionChanged(r.x, r.y, r.vx, r.vy, r.id);
                        break;
                    case RunningRequest.SHOOT:
                        listener.shoot(r.x, r.y, r.vx, r.vy, r.id);
                        break;
                }
            } else if (o instanceof DisconnectionRequest) {
                listener.disconnected(((DisconnectionRequest) o).userId);
            } else if (o instanceof UserResponse) {
                listener.playerConnected(((UserResponse) o).userId, ((UserResponse) o).nickname);
            } else if (o instanceof EntityPacket) {
                listener.addEntity(((EntityPacket) o).entity);
            }
        }
    }

    public void sendEntity(Entity e) {
        client.sendUDP(new EntityPacket(e));
    }

    Entity[] getEntities() {
        return listener.getEntities();
    }

    Entity[] getBullets() {
        return listener.getBullets();
    }

    public void runningStart(Entity e) {
        client.sendUDP(new RunningRequest(e, RunningRequest.START));
    }

    public void runningEnd(Entity e) {
        client.sendUDP(new RunningRequest(e, RunningRequest.END));
    }

    public void directionChange(Entity e) {
        client.sendUDP(new RunningRequest(e, RunningRequest.DIRECTION));
    }

    public void headDirectionChange(Entity e) {
        client.sendUDP(new RunningRequest(e, RunningRequest.HEAD_DIRECTION));
    }

    public void shoot(Entity shooter) {
        client.sendUDP(new RunningRequest(shooter, RunningRequest.SHOOT));
    }
}