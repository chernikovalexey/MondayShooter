package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 7:40 PM on 1/13/14
 */

public class Player extends Entity {
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;

    private long lastShootTime = System.currentTimeMillis();

    public Player() {
    }

    public Player(World world, float x, float y) {
        super(world, x, y, WIDTH, HEIGHT);

        setSpeed(5f);
    }

    @Override
    public void update(GameContainer container, int delta) {
        Input input = container.getInput();

        super.update(container, delta);
        movingDirection.set(0f, 0f);
        updateDirectionToPoint(input.getMouseX(), input.getMouseY());

        boolean isMoving = false;

        if (input.isKeyDown(Input.KEY_W)) {
            movingDirection.y -= 1;
            isMoving = true;
        }
        if (input.isKeyDown(Input.KEY_S)) {
            movingDirection.y += 1;
            isMoving = true;
        }
        if (input.isKeyDown(Input.KEY_A)) {
            movingDirection.x -= 1;
            isMoving = true;
        }
        if (input.isKeyDown(Input.KEY_D)) {
            movingDirection.x += 1;
            isMoving = true;
        }

        long currentShootTime = System.currentTimeMillis();
        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && currentShootTime - lastShootTime > 45) {
            lastShootTime = currentShootTime;
            Vector2f heading = getHeadingVector();
            double angle = Math.atan2(heading.y, heading.x);
            world.addBullet(getX(), getY(), getId(), new Vector2f((float) Math.cos(angle), (float) Math.sin(angle)));
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(Color.green);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}