package com.twopeople.game.world.pathfinder;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.world.World;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Alexey
 * At 9:38 PM on 2/22/14
 */

public class RailroadRouter {
    private ArrayList<Entity> railroad;

    public RailroadRouter(ArrayList<Entity> railroad) {
        this.railroad = railroad;
    }

    private World world;
    private float cellWidth, cellHeight;

    private static final Vector2f[] dirs = new Vector2f[]{
            new Vector2f(1, 1), new Vector2f(-1, -1), new Vector2f(-1, 1), new Vector2f(1, -1)
    };

    private Node goal;
    private HashMap<String, Node> nodes = new HashMap<String, Node>();

    public RailroadRouter(World world) {
        this.world = world;
    }

    private Node createNode(float x, float y) {
        Node node = new Node(x, y, cellWidth, cellHeight);
        nodes.put(Node.getHash(x, y), node);
        return node;
    }

    private Node getNode(float x, float y) {
        return nodes.get(Node.getHash(x, y));
    }

    private void addNeighbours(Node node) {
        for (Vector2f d : dirs) {
            for (float yo = -1f; yo <= 1f; yo += 0.5f) {
                float x = node.x + d.x * node.width / 2;
                float y = node.y + d.y * node.height / 2 + yo;
                Node n = getNode(x, y);
                if (n != null) {
                    node.addNeighbour(n);
                    break;
                }
            }
        }
    }

    private Path constructPath(Node goalNode) {
        Path path = new Path(false);
        Node node = goalNode;
        while (node != null) {
            path.addNodeFront(node);
            node = node.getParent();
        }
        return path;
    }

    public Path construct(Entity from, Entity to) {
        nodes.clear();

        this.cellWidth = from.getWidth();
        this.cellHeight = from.getDepth();

        for (Entity e : railroad) {
            createNode(e.getX(), e.getY());
        }

        System.out.println(cellWidth + ", " + cellHeight);
        System.out.println("Starting tracing with " + nodes.size() + " nodes (railroad = " + railroad.size() + ").");

        Node start = createNode(from.getX(), from.getY());
        Node goal = createNode(to.getX(), to.getY());

        if (start.isIntersecting(goal)) {
            return new Path(true);
        }

        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(start);
//        queue.add(goal);

        Path bestPath = null;

        while (queue.size() > 0) {
            Node current = queue.poll();

            if (current.isVisited()) {
                continue;
            }

            if (current.isIntersecting(goal)) {
                bestPath = constructPath(current);
                break;
            }

            addNeighbours(current);

            current.visit();

            for (Node neighbour : current.getNeighbours()) {
                if (neighbour.isVisited()) {
                    continue;
                }

                float distance = current.getPathDistance() + current.getPosition().distance(neighbour.getPosition());

                if (neighbour.getParent() != null && distance >= neighbour.getPathDistance()) {
                    continue;
                }

                neighbour.setPathDistance(distance);
                neighbour.setHeuristicDistance(neighbour.getPosition().distance(goal.getPosition()) + distance);
                neighbour.setPriority(neighbour.getHeuristicDistance());
                if (neighbour.getParent() == null) {
                    neighbour.setParent(current);
                    queue.add(neighbour);
                }
            }
        }

        return bestPath == null ? new Path(true) : bestPath;
    }

    public void render(Camera camera, Graphics g) {
        g.setColor(new Color(200, 200, 15, 220));
        Iterator it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Node node = (Node) pairs.getValue();
            g.drawRect(camera.getX(node.x), camera.getY(node.y), node.width, node.height);
            g.setColor(Color.white);
            g.drawString(node.x + " / " + node.y, camera.getX(node.x + 5), camera.getY(node.y + 5));
        }
    }
}