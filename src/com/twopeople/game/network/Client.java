package com.twopeople.game.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.twopeople.game.Entity;
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
                    case RunningRequest.END:
                        listener.runningEnd(r.x, r.y, r.id);
                        break;
                    case RunningRequest.START:
                        listener.runningStart(r.x, r.y, r.id);
                        break;
                    case RunningRequest.SHUT:
                        listener.shut(r.x, r.y, r.vx, r.vy, r.id);
                        break;
                }
            } else if(o instanceof DisconnectionRequest) {
                listener.disconnected(((DisconnectionRequest)o).userId);
            }
        }
    }

    Entity[] getState() {
        return listener.getState();
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

    public void shut(Entity shooter) {
        client.sendUDP(new RunningRequest(shooter, RunningRequest.SHUT));
    }
}