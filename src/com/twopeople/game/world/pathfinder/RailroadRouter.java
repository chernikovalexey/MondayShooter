package com.twopeople.game.world.pathfinder;

import com.twopeople.game.entity.Entity;

import java.util.ArrayList;

/**
 * Created by Alexey
 * At 9:38 PM on 2/22/14
 */

public class RailroadRouter {
    private ArrayList<Entity> railroad;

    public RailroadRouter(ArrayList<Entity> railroad) {
        this.railroad = railroad;
    }

    public RailPath construct(Entity from, Entity to, Entity prev) {
        RailPath path = new RailPath();

        if (from.equals(to)) {
            return path;
        }

        float minDist = Float.MAX_VALUE;
        Entity closest = null;

        for (Entity e : railroad) {
            if (!e.equals(from) && !e.equals(prev)) {
                float dist = e.getBBCentre().distance(from.getBBCentre());
                if (dist < minDist && from.getX() - e.getX() != 0) {
                    minDist = dist;
                    closest = e;
                }
            }
        }

        path.addNode(closest);

        return path.merge(construct(closest, to, from));
    }
}