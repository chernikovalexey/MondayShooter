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

import java.util.Collection;

public class Entity {
    public static int serialId;

    protected transient World world;

    protected int id;
    private int owner;
    private int layer;

    protected float x, y;
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
    protected transient Animation[] animations = new Animation[8];

    public Entity() {
    }

    public Entity(float x, float y, float width, float height, boolean hasId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        init(hasId);
    }

    public void init(boolean hasId) {
        if (hasId) {
            id = serialId++;
        }
    }

    public void update(GameContainer container, int delta) {
        long time = System.currentTimeMillis();

        float speed = getSpeed();
        float friction = 0.00001f;
        float accelerationX = -velocity.x * friction + movingDirection.x * speed;
        float accelerationZ = -velocity.y * friction + movingDirection.y * speed;

        velocity.x = accelerationX * delta;
        velocity.y = accelerationZ * delta;

        Collection<Entity> entities = world.getEntities().values();

        float dx = velocity.x * delta * 0.0001f * 20;
        float dy = velocity.y * delta * 0.0001f * 20;

        x += dx;

        for (Entity e : entities) {
            if (this.collidesWith(e) && !e.equals(this)) {
                bumpedInto(e);
                //System.out.println("Colliding position x: " + x);
                while (e.collidesWith(this)) {
                    x -= dx / 10;
                    //System.out.println("Reducing x: " + x + " (" + ((velocity.x * delta * 0.0001f * 20) / 10) + ")");
                }
            }
        }

        y += dy;

        for (Entity e : entities) {
            if (this.collidesWith(e) && !e.equals(this)) {
                bumpedInto(e);

                if (movingDirection.x == 0) {
                    Vector2f hitSide = e.getHitSideVector(this);
                    float angle = Vector3f.angle(new Vector3f(hitSide.x, 0, hitSide.y), new Vector3f(x, 0, y));
                    x += (float) Math.cos(0 + angle);
                }

                while (e.collidesWith(this)) {
                    y -= dy / 10;
                }
            }
        }

//        System.out.println("Time elapsed on updating and moving: " + (System.currentTimeMillis() - time));
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
        Camera camera = world.getGame().getCamera();
        Vector2f newDirection = (new Vector2f(dx - camera.getX(x), dy - camera.getY(y))).normalise();
        updateDirection(newDirection.x, newDirection.y);
    }

    public void loadAnimations(SpriteSheet sprite) {
        int states = 1;
        Image[] up = new Image[states];
        Image[] down = new Image[states];
        Image[] left = new Image[states];
        Image[] right = new Image[states];
        Image[] upLeft = new Image[states];
        Image[] upRight = new Image[states];
        Image[] downLeft = new Image[states];
        Image[] downRight = new Image[states];

        for (int i = 0; i < states; ++i) {
            up[i] = sprite.getSprite(i, 0);
            down[i] = sprite.getSprite(i, 4);
            left[i] = sprite.getSprite(i, 2);
            right[i] = sprite.getSprite(i, 6);
            upLeft[i] = sprite.getSprite(i, 1);
            upRight[i] = sprite.getSprite(i, 7);
            downLeft[i] = sprite.getSprite(i, 3);
            downRight[i] = sprite.getSprite(i, 5);
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

    public Vector2f getHitSideVector(Entity entity) {
        return new Vector2f(0, 0);
    }

    public Vector2f getBBCentre() {
        return new Vector2f(getBB().getCenterX(), getBB().getCenterY());
    }

    public Shape getBB() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public Shape[] getSkeleton() {
        return new Shape[]{getBB()};
    }

    public boolean collidesWith(Entity entity) {
        for (Shape shape1 : getSkeleton()) {
            for (Shape shape2 : entity.getSkeleton()) {
                if (shape1.intersects(shape2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void bumpedInto(Entity entity) {}

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

    public boolean hasWorld() {
        return world != null;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}