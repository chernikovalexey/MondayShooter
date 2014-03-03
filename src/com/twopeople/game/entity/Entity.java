package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.IEntity;
import com.twopeople.game.MathUtil;
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
    public static int connectionSerialId;

    protected transient World world;

    private int cellX, cellY;

    protected int id, connectionId;
    private int owner;

    protected float x, y, z;
    protected float width, height, depth;
    private float speed;

    protected int health, maxHealth;

    protected boolean remove = false;
    protected boolean hasMoved = false; // Whether it moved in the current tick
    protected boolean simpleMovement = false;

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

        if (hasId) {
            id = serialId++;
        }
    }

    public void init() {}

    public void update(GameContainer container, int delta, EntityVault entities) {
        if (movingDirection.z == -1) {
            velocity.z = 25f;
        }

        long time = System.currentTimeMillis();

        float speed = getSpeed();
        float gravity = 9.8f;
        float accelerationX = -velocity.x * groundFriction + movingDirection.x * speed;
        float accelerationY = -velocity.y * groundFriction + movingDirection.y * speed;
        float accelerationZ = -gravity * airFriction;

        velocity.x = accelerationX * delta;
        velocity.y = accelerationY * delta;
        velocity.z += accelerationZ * delta * .0099f;

        z += velocity.z * delta * 0.001f * 20;
        if (z <= 0) {
            velocity.z = 0;
            z = 0;
        }

        float dx = velocity.x * delta * 0.0001f * 20;
        float dy = velocity.y * delta * 0.0001f * 20;

        float px = x;
        float py = y;

        if (dx != 0 || dy != 0) {
            Collection<Entity> nearby = entities.getNearbyEntities(this);

            int steps = (int) speed * 2;
            if (simpleMovement) { steps /= 2 * 6; }
            float d2x = 0f;
            float d2y = 0f;

            //System.out.println(dx + ", " + steps + ", " + dx / steps);

            o:
            while (d2x / dx < 1f || d2y / dy < 1f) {
                d2x += dx / steps;
                if (d2x / dx > 1f) { d2x = dx; }
                d2y += dy / steps;
                if (d2y / dy > 1f) { d2y = dy; }

                float pdx = d2x;
                float pdy = d2y;

                for (Entity e : nearby) {
                    x += pdx;
                    y += pdy;

                    if (this.collidesWith(e)) {
                        d2x -= dx / steps;
                        d2y -= dy / steps;

                        if (simpleMovement) {
                            x -= pdx;
                            y -= pdy;
                            bumpedInto(e);
                            break o;
                        }

                        y -= pdy;
                        boolean canMoveX = !this.collidesWith(e);
                        y += pdy;

                        x -= pdx;
                        boolean canMoveY = !this.collidesWith(e);
                        y -= pdy;

                        float smx = 0f;
                        float smy = 0f;

                        if (canMoveX) { smx = pdy; }
                        if (canMoveY) { smy = pdx * 0.35f; }

                        if (getBBCentre().x < e.getBBCentre().x && getBBCentre().y < e.getBBCentre().y || getBBCentre().x > e.getBBCentre().x && getBBCentre().y > e.getBBCentre().y) {
                            smx *= -1;
                            smy *= -1;
                        }

                        if (!canMoveY && canMoveX) {
                            x += smx;
                        }
                        if (!canMoveX && canMoveY) {
                            y += smy;
                        }

                        velocity.set(0, 0);
                        bumpedInto(e);
                        break o;
                    }

                    x -= pdx;
                    y -= pdy;
                }
            }

            x += d2x;
            y += d2y;

            for (Entity e : nearby) {
                if (this.collidesWith(e)) {
                    if (d2x != 0 || d2y != 0) {
                        x -= d2x;
                        y -= d2y;
                    } else {
                        float xi = 0.1f;
                        float yi = 0.1f;
                        if (e.getBBCentre().getX() < getBBCentre().getX()) {
                            xi *= -1;
                        }
                        if (e.getBBCentre().getY() < getBBCentre().getY()) {
                            yi *= -1;
                        }
                        x -= xi;
                        y -= yi;
                    }
                }
            }
        }

        hasMoved = x != px || y != py;

        if (hasMoved) {
            //System.out.println((System.currentTimeMillis() - time) + " ms elapsed on moving.");
        }
    }

    @Override
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

    public void setMovingDirectionToPoint(float dx, float dy) {
        float dist = MathUtil.getDist(x, y, dx, dy);
        //System.out.println((dx - x) + ", " + (dy - y) + " => " + dist);
        if (dist > 2.5f) {
            Vector2f newDirection = new Vector2f(dx - getBBCentre().x, dy - getBBCentre().y).normalise();
            movingDirection.x = newDirection.x;
            movingDirection.y = newDirection.y;
        }
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

    @Override
    public Vector2f getBBCentre() {
        return new Vector2f(getBB().getCenterX(), getBB().getCenterY());
    }

    public Shape getBB() {
        return new Rectangle(x, y, width, depth);
    }

    public Shape[] getSkeleton() {
        return new Shape[]{getBB()};
    }

    public boolean collidesWith(Entity entity) {
        if (entity.getHeight() == 0) {
            return false;
        }

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

    // ===================
    // Getters and setters

    public int getId() {
        return id;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
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