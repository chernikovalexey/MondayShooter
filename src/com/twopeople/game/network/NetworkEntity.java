package com.twopeople.game.network;

import com.esotericsoftware.kryo.Kryo;
import com.twopeople.game.Bullet;
import com.twopeople.game.Entity;
import com.twopeople.game.Player;
import com.twopeople.game.Wall;
import com.twopeople.game.network.packet.AuthRequest;
import com.twopeople.game.network.packet.AuthResponse;
import com.twopeople.game.network.packet.DisconnectionRequest;
import com.twopeople.game.network.packet.EntityPacket;
import com.twopeople.game.network.packet.Packet;
import com.twopeople.game.network.packet.RunningRequest;
import com.twopeople.game.network.packet.UserResponse;
import org.newdawn.slick.geom.Vector2f;

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
        kryo.register(Wall.class);
        kryo.register(Bullet[].class);
        kryo.register(Entity[].class);
        kryo.register(Vector2f.class);
        kryo.register(Vector2f[].class);
        kryo.register(RunningRequest.class);
        kryo.register(DisconnectionRequest.class);
        kryo.register(EntityPacket.class);
    }
}