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
        super(world, x, y, WIDTH, HEIGHT);
        this.movingDirection = movingDirection;
        setSpeed(6f);
    }

    public void update(GameContainer container, int delta) {
        System.out.println(movingDirection.x + ", " + movingDirection.y);
        super.update(container, delta);
    }

    public void render(GameContainer container, Graphics g) {
        g.setColor(Color.pink);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}