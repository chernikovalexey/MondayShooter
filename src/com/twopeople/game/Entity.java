package com.twopeople.game;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Entity {
    public static int serialId;

    protected transient World world;

    private int id;
    private int owner;

    private float x, y;
    private float width, height;

    private Vector2f direction = new Vector2f(0f, 0f);
    private Vector2f velocity = new Vector2f(0f, 0f);

    private Vector2f[] directions = new Vector2f[]{
            new Vector2f(-90), new Vector2f(0),
            new Vector2f(90), new Vector2f(180),
            new Vector2f(-135), new Vector2f(-45),
            new Vector2f(135), new Vector2f(45)
    };

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

    public void update(GameContainer container, int delta) {
        updateDirection(direction.x, direction.y);
        direction.set(0, 0);
    }

    public void render(GameContainer container, Graphics g) {
    }

    private void updateDirection(float dx, float dy) {
        setDirectionX(dx);
        setDirectionY(dy);

        float theta;
        float minTheta = 360f;
        for (int i = 0, len = directions.length; i < len; ++i) {
            theta = Vector3f.angle(new Vector3f(direction.x, 0, direction.y), new Vector3f(directions[i].x, 0, directions[i].y));
            if (theta < minTheta) {
                minTheta = theta;
                //currentAnimationState = i;
            }
        }
    }

    public void updateDirectionToPoint(float dx, float dy) {
        Vector2f newDirection = (new Vector2f(dx - getX(), dy - getY())).normalise();
        updateDirection(newDirection.x, newDirection.y);
    }

    public void moveInertly(int delta) {
        float speed = 10f;
        float friction = 0.00001f;
        float _speed = speed / 10f;
        float accelerationX = -velocity.x * friction + direction.x * _speed;
        float accelerationZ = -velocity.y * friction + direction.y * _speed;

        velocity.x = accelerationX * delta;
        velocity.y = accelerationZ * delta;

        x += velocity.x;
        y += velocity.y;
    }

    public void move(float dx, float dy) {
        direction = (new Vector2f(dx - x, dy - y)).normalise();
    }

    public Shape getBB() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public boolean intersects(Shape shape) {
        return shape.intersects(getBB());
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

    public float getDirectionX() {
        return direction.x;
    }

    public void setDirectionX(float dx) {
        direction.x = dx;
    }

    public float getDirectionY() {
        return direction.y;
    }

    public void setDirectionY(float dy) {
        direction.y = dy;
    }
}