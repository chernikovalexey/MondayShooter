package com.twopeople.game.world.pathfinder;

import com.twopeople.game.entity.Entity;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;


public class Node implements Comparable<Node> {
    public float x, y;
    public float width, height;
    private boolean visited = false;
    private float pathDistance = 0f;
    private float heuristicDistance = 0f;
    private float priority = 0f;

    private Node parent = null;
    private ArrayList<Node> neighbours = new ArrayList<Node>();

    public Node(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void visit() {
        this.visited = true;
    }

    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }

    public float getPathDistance() {
        return this.pathDistance;
    }

    public void setPathDistance(float dist) {
        this.pathDistance = dist;
    }

    public float getHeuristicDistance() {
        return heuristicDistance;
    }

    public float getPriority() {
        return priority;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setHeuristicDistance(float heuristicDistance) {
        this.heuristicDistance = heuristicDistance;
    }

    public void addNeighbour(Node node) {
        neighbours.add(node);
    }

    public ArrayList<Node> getNeighbours() {
        return this.neighbours;
    }

    public int compare(Node n1, Node n2) {
        if (n1.getPriority() == n2.getPriority()) { return 0; }
        return n1.getPriority() < n2.getPriority() ? -1 : 1;
    }

    @Override
    public int compareTo(Node o) {
        return compare(this, o);
    }

    public static String getHash(float x, float y) {
        return x + "_" + y;
    }

    public Shape getBounds() {
        return new Rectangle(x + 1, y + 1, width - 2, height - 2);
    }

    public boolean isIntersecting(Node node) {
        return node.getBounds().intersects(getBounds());
    }
}