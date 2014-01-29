package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 10:08 PM on 1/13/14
 */

public class Bullet extends Entity {
    public static final float WIDTH = 12;
    public static final float HEIGHT = 12;

    public Bullet() {
    }

    public Bullet(World world, float x, float y, Vector2f movingDirection) {
        super(x, y, WIDTH, HEIGHT, false);
        this.movingDirection = movingDirection;
        setSpeed(15f);
        setWorld(world);
        setLayer(-1);
    }

    public void update(GameContainer container, int delta) {
        super.update(container, delta);

        if (world.ranOut(this)) {
            remove = true;
        }
    }

    public void render(GameContainer container, Camera camera, Graphics g) {
        g.setColor(Color.pink);
        g.fillOval(camera.getX(getX()), camera.getY(getY()), getWidth(), getHeight());
    }

    public boolean collidesWith(Entity entity) {
        if (entity instanceof Player && entity.getId() == getOwner()) {
            return false;
        }
        return super.collidesWith(entity);
    }

    public void bumpedInto(Entity entity) {
        remove = true;
        entity.hurt(this, 25);
    }
}