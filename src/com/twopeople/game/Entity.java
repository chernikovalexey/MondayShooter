package com.twopeople.game;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Entity {
    public static int serialId;

    protected transient World world;

    private int id;
    private int owner;

    private float x, y;
    private float width, height;

    public Entity() {
        init();
    }

    public Entity(World world, float x, float y, float width, float height) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        init();
    }

    public void init() {
        id = ++serialId;
    }

    public Shape getBB() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public boolean intersects(Shape shape) {
        return shape.intersects(getBB());
    }

    public void move(int dx, int dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    // ===================
    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}