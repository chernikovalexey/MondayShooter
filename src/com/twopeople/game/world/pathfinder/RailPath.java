package com.twopeople.game.world.pathfinder;

import com.twopeople.game.entity.Entity;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

/**
 * Created by Alexey
 * At 9:41 PM on 2/22/14
 */

public class RailPath {
    private ArrayList<Entity> nodes = new ArrayList<Entity>();
    private int index;
    private boolean finished = false;

    public RailPath merge(RailPath path) {
        nodes.addAll(path.getNodes());
        return this;
    }

    public void addNode(Entity node) {
        nodes.add(node);
    }

    public ArrayList<Entity> getNodes() {
        return nodes;
    }

    public Vector2f getCurrentTarget(Entity entity) {
        Entity target = nodes.get(index);

        if (target.collidesWith(entity)) {
            next();
        }

        return target.getBBCentre();
    }

    private void next() {
        ++index;
        if (index >= nodes.size()) {
            index = nodes.size() - 1;
            finished = true;
        }
    }

    public void prev() {
        if (index > 0) {
            --index;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}