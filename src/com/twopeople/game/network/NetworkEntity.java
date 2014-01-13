package com.twopeople.game.network;

import com.esotericsoftware.kryo.Kryo;
import com.twopeople.game.Entity;
import com.twopeople.game.network.packet.AuthRequest;
import com.twopeople.game.network.packet.*;
import com.twopeople.game.*;

/**
 * Created by podko_000
 * At 17:51 on 13.01.14
 */

public class NetworkEntity extends com.esotericsoftware.kryonet.Listener {
    protected static final int UDP_PORT = 54555;
    protected static final int TCP_PORT = 54556;

    public void register(Kryo kryo) {
        kryo.register(Packet.class);
        kryo.register(AuthRequest.class);
        kryo.register(AuthResponse.class);
        kryo.register(UserResponse.class);
        kryo.register(Error.class);
        kryo.register(Entity.class);
        kryo.register(Player.class);
        kryo.register(Bullet.class);
        kryo.register(Entity[].class);
        kryo.register(RunningRequest.class);
        kryo.register(DisconnectionRequest.class);

    }
}