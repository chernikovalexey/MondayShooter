package com.twopeople.game.network.packet;

import com.twopeople.game.Entity;

/**
 * Created by podko_000
 * At 21:29 on 13.01.14
 */

public class RunningRequest extends Packet {
    public static final byte START = 1;
    public static final byte END = 2;
    public static final byte DIRECTION = 3;
    public static final byte SHUT = 4;
    public static final byte HEAD_DIRECTION = 5;

    public int id;
    public float x,y;
    public float vx,vy;
    public byte type;

    public RunningRequest() {}

    public RunningRequest(Entity e, byte type) {
        this.id = e.getId();
        this.x = e.getX();
        this.y = e.getY();
        this.type = type;

        if(type==HEAD_DIRECTION || type==SHUT) {
        this.vx = e.getDirectionX();
        this.vy = e.getDirectionY();
        } else {
            this.vx = e.getMovingVector().getX();
            this.vy = e.getMovingVector().getY();
        }

        this.type = type;
    }
}
