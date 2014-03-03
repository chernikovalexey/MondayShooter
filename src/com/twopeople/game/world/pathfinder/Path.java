package com.twopeople.game.world.pathfinder;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;


public class Path {
    private int index = 0;
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private boolean finished = false;

    public Path(boolean finished) {
        this.finished = finished;
    }

    public int getLength() {
        return nodes.size();
    }

    public void addNodeFront(Node node) {
        nodes.add(0, node);
    }

    public Vector2f getCurrentTarget(Entity entity) {
        Node node = nodes.get(index);
        float cx = node.getBounds().getCenterX();
        float cy = node.getBounds().getCenterY();

        if (entity.getBBCentre().distance(new Vector2f(cx, cy)) < 2.5f) {
            next();
        }

        return new Vector2f(cx, cy);
    }

    public void prev() {
        if (index > 0) {
            --index;
        }
    }

    public void next() {
        ++index;
        if (index >= nodes.size()) {
            index = nodes.size() - 1;
            finished = true;
        }
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void render(Camera camera, Graphics g) {
        g.setColor(Color.magenta);
        for (Node node : nodes) {
            if (node != null) {
                g.fillRect(camera.getX(node.x), camera.getY(node.y), node.width, node.height);
            }
        }
    }
}