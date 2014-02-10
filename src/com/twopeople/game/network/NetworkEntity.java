package com.twopeople.game.network;

import com.esotericsoftware.kryo.Kryo;
import com.twopeople.game.entity.Bullet;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Player;
import com.twopeople.game.entity.building.Wall;
import com.twopeople.game.network.packet.*;
import org.lwjgl.util.vector.Vector3f;
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
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(float[][].class);
        kryo.register(RunningRequest.class);
        kryo.register(DisconnectionRequest.class);
        kryo.register(EntityPacket.class);
        kryo.register(KilledRequest.class);
        kryo.register(Vector3f.class);
    }
}