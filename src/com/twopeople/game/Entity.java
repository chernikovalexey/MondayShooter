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
    private float speed;

    protected boolean remove = false;

    protected Vector2f movingDirection = new Vector2f(0f, 0f);
    private Vector2f headingDirection = new Vector2f(0f, 0f);
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
        //        System.out.println("Initialized an entity with id=" + id);
    }

    public void update(GameContainer container, int delta) {
        float speed = getSpeed();
        float friction = 0.00001f;
        float accelerationX = -velocity.x * friction + movingDirection.x * speed;
        float accelerationZ = -velocity.y * friction + movingDirection.y * speed;

        velocity.x = accelerationX * delta;
        velocity.y = accelerationZ * delta;

        x += velocity.x * delta * 0.001f * 20;
        y += velocity.y * delta * 0.001f * 20;
    }

    public void render(GameContainer container, Graphics g) {
    }

    private void updateDirection(float dx, float dy) {
        float oldX = headingDirection.x;
        float oldY = headingDirection.y;
        headingDirection.x = dx;
        headingDirection.y = dy;

        if (oldX != dx || oldY != dy) {
            world.getGame().getClient().headDirectionChange(this);
        }

        float theta;
        float minTheta = 360f;
        for (int i = 0, len = directions.length; i < len; ++i) {
            theta = Vector3f.angle(new Vector3f(headingDirection.x, 0, headingDirection.y), new Vector3f(directions[i].x, 0, directions[i].y));
            if (theta < minTheta) {
                minTheta = theta;
                //currentAnimationState = i;
            }
        }
    }

    public void updateDirectionToPoint(float dx, float dy) {
        Vector2f newDirection = (new Vector2f(dx - x, dy - y)).normalise();
        updateDirection(newDirection.x, newDirection.y);
    }

    public Shape getBB() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public boolean intersects(Shape shape) {
        return shape.intersects(getBB());
    }

    public boolean seeksForRemoval() {
        return remove;
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Vector2f getHeadingVector() {
        return headingDirection;
    }

    public void setHeadingVector(Vector2f heading) {
        this.headingDirection = heading;
    }

    public Vector2f getMovingVector() {
        return movingDirection;
    }

    public void setMovingVector(Vector2f moving) {
        this.movingDirection = moving;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}