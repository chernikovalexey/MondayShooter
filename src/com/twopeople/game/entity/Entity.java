package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.IEntity;
import com.twopeople.game.world.World;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.Collection;

public class Entity implements IEntity {
    public static int serialId;

    protected transient World world;

    private int cellX, cellY;

    protected int id;
    private int owner;

    protected float x, y, z;
    protected float width, height, depth;
    private float speed;

    protected int health, maxHealth;

    protected boolean remove = false;

    protected Vector3f velocity = new Vector3f(0f, 0f, 0f);
    protected Vector3f movingDirection = new Vector3f(0f, 0f, 0f);
    private Vector2f headingDirection = new Vector2f(0f, 0f);

    private Vector2f[] directions = new Vector2f[]{
            new Vector2f(-90), new Vector2f(0),
            new Vector2f(90), new Vector2f(180),
            new Vector2f(-135), new Vector2f(-45),
            new Vector2f(135), new Vector2f(45)
    };

    protected int currentAnimationState;
    protected transient Animation[] animations = new Animation[8];
    protected int[] skin;

    // Used for static on-the-fly-created entities (parsed)
    // Remains unused in other cases
    protected transient Image image;

    protected float groundFriction = 1.0f / 1000f;
    protected float airFriction = 1.0f;

    public Entity() {
    }

    public Entity(float x, float y, float z, float width, float height, float depth, boolean hasId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        init(hasId);
    }

    public void init(boolean hasId) {
        if (hasId) {
            id = serialId++;
        }
    }


    public void update(GameContainer container, int delta, EntityVault entities) {
        if (movingDirection.z == -1) {
            velocity.z = 25f;
        }

        long time = System.currentTimeMillis();

        float speed = getSpeed();
        //        float friction = 0.00001f;
        float gravity = 9.8f;
        float accelerationX = -velocity.x * groundFriction + movingDirection.x * speed;
        float accelerationY = -velocity.y * groundFriction + movingDirection.y * speed;
        float accelerationZ = -gravity * airFriction;

        velocity.x = accelerationX * delta;
        velocity.y = accelerationY * delta;
        velocity.z += accelerationZ * delta * .0099f;

        z += velocity.z * delta * 0.001f * 20;
        if (z < 0) {
            velocity.z = 0;
            z = 0;
        }

        float dx = velocity.x * delta * 0.0001f * 20;
        float dy = velocity.y * delta * 0.0001f * 20;

        if (dx != 0 || dy != 0) {
            Collection<Entity> nearby = entities.getNearbyEntities(this);

            if (dx != 0) {
                x += dx;

                for (Entity e : nearby) {
                    if (this.collidesWith(e)) {
                        bumpedInto(e);

                        if (movingDirection.y == 0) {
                            Vector2f hitSide = e.getHitSideVector(this);
                            float angle = Vector3f.angle(new Vector3f(hitSide.x, 0, hitSide.y), new Vector3f(getBBCentre().x, 0, 0));
                            y += (float) Math.sin(angle - Math.PI / 2);
                        }

                        while (e.collidesWith(this)) {
                            x -= dx / 10;
                        }
                    }
                }
            }

            if (dy != 0) {
                y += dy;

                for (Entity e : nearby) {
                    if (this.collidesWith(e)) {
                        bumpedInto(e);

                        if (movingDirection.x == 0) {
                            Vector2f hitSide = e.getHitSideVector(this);
                            float angle = Vector3f.angle(new Vector3f(hitSide.x, 0, hitSide.y), new Vector3f(0, 0, getBBCentre().y));
                            x += (float) Math.cos(angle);
                        }

                        while (e.collidesWith(this)) {
                            y -= dy / 10;
                        }
                    }
                }
            }
        }

        //                System.out.println((System.currentTimeMillis()-time) + " elapsed on moving.");
    }

    public void render(GameContainer container, Camera camera, Graphics g) {}

    public void renderOvalShadow(Camera camera, Graphics g, float shadowDistance, float minOpacity) {
        float shadowOpacity = 0.15f;

        float k = z / height;
        float minWidth = getBB().getWidth();
        float minHeight = getBB().getHeight() / 2;
        float sw = getBB().getWidth() * (1.5f - k);
        float sh = getBB().getHeight() / 1.5f * (1 - k);
        if (sw < minWidth) { sw = minWidth; }
        if (sh < minHeight) { sh = minHeight; }

        float opacity = shadowOpacity * (1 - k);
        if (opacity < minOpacity) { opacity = minOpacity; }
        g.setColor(new Color(0, 0, 0, opacity));
        g.fillOval(camera.getX(x + (width - sw) / 2), camera.getY(y + height + shadowDistance), sw, sh);
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
                if (getZ() > entity.getHeight()) {
                    continue;
                }

                if (shape1.intersects(shape2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void bumpedInto(Entity entity) {}

    public void hurt(Entity by, int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            killed(by);
        }
    }

    public void killed(Entity by) {}

    public boolean isRemovalFlagOn() {
        return remove;
    }

    public boolean alwaysFlying() {
        return false;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
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

    public float getOrthogonalHeight() {
        return height + depth;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
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

    public Vector3f getMovingVector() {
        return movingDirection;
    }

    public void setMovingVector(Vector3f moving) {
        this.movingDirection = moving;
    }

    public boolean hasWorld() {
        return world != null;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHealth(int health, int maxHealth) {
        setHealth(health);
        this.maxHealth = maxHealth;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setSkin(int[] skin) {
        this.skin = skin;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }
}