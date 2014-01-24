package com.twopeople.game;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
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

    protected int currentAnimationState;
    protected Animation[] animations = new Animation[8];

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
        id = serialId++;
    }

    public void update(GameContainer container, int delta) {
        float speed = getSpeed();
        float friction = 0.00001f;
        float accelerationX = -velocity.x * friction + movingDirection.x * speed;
        float accelerationZ = -velocity.y * friction + movingDirection.y * speed;

        velocity.x = accelerationX * delta;
        velocity.y = accelerationZ * delta;

        x += velocity.x * delta * 0.0001f * 20;
        y += velocity.y * delta * 0.0001f * 20;
    }

    public void render(GameContainer container, Camera camera, Graphics g) {
    }

    private void updateDirection(float dx, float dy) {
        float oldX = headingDirection.x;
        float oldY = headingDirection.y;
        headingDirection.x = dx;
        headingDirection.y = dy;

        if (oldX != dx || oldY != dy) {
            world.getGame().getClient().headDirectionChange(this);
        }

        float minTheta = 360f;
        for (int i = 0, len = directions.length; i < len; ++i) {
            float theta = Vector3f.angle(new Vector3f(headingDirection.x, 0, headingDirection.y), new Vector3f(directions[i].x, 0, directions[i].y));
            if (theta < minTheta) {
                minTheta = theta;
                currentAnimationState = i;
            }
        }
    }

    public void updateDirectionToPoint(float dx, float dy) {
        Vector2f newDirection = (new Vector2f(dx - x, dy - y)).normalise();
        updateDirection(newDirection.x, newDirection.y);
    }

    public void loadAnimations(SpriteSheet sprite) {
        Image[] up = new Image[3];
        Image[] down = new Image[3];
        Image[] left = new Image[3];
        Image[] right = new Image[3];
        Image[] upLeft = new Image[3];
        Image[] upRight = new Image[3];
        Image[] downLeft = new Image[3];
        Image[] downRight = new Image[3];

        for (int i = 0; i < 3; ++i) {
            up[i] = sprite.getSprite(i, 0);
            down[i] = sprite.getSprite(i, 4);
            left[i] = sprite.getSprite(i, 2);
            right[i] = left[i].getFlippedCopy(true, false);
            upLeft[i] = sprite.getSprite(i, 1);
            upRight[i] = upLeft[i].getFlippedCopy(true, false);
            downLeft[i] = sprite.getSprite(i, 3);
            downRight[i] = downLeft[i].getFlippedCopy(true, false);
        }

        animations[0] = new Animation(up, 200, true);
        animations[1] = new Animation(right, 200, true);
        animations[2] = new Animation(down, 200, true);
        animations[3] = new Animation(left, 200, true);
        animations[4] = new Animation(upLeft, 200, true);
        animations[5] = new Animation(upRight, 200, true);
        animations[6] = new Animation(downLeft, 200, true);
        animations[7] = new Animation(downRight, 200, true);
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
        updateDirection(heading.x, heading.y);
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