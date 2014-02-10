package com.twopeople.game.network.packet;

import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Player;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by podko_000
 * At 21:29 on 13.01.14
 */

public class RunningRequest extends Packet {
    public static final byte START = 1;
    public static final byte END = 2;
    public static final byte DIRECTION = 3;
    public static final byte SHOOT = 4;
    public static final byte HEAD_DIRECTION = 5;

    public int id;
    public float x, y;
    public float vx, vy;
    public byte type;

    public RunningRequest() {}

    public RunningRequest(Entity e, byte type) {
        this.id = e.getConnectionId();
        this.x = e.getX();
        this.y = e.getY();
        this.type = type;

        if (type == HEAD_DIRECTION || type == SHOOT) {
            Vector2f heading = e.getHeadingVector();
            this.vx = heading.x;
            this.vy = heading.y;
        } else {
            this.vx = e.getMovingVector().getX();
            this.vy = e.getMovingVector().getY();
        }

        this.type = type;
    }
}