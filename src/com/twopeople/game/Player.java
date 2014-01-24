package com.twopeople.game;

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

        setSpeed(2.1f);
        loadAnimations(Images.player);
    }

    @Override
    public void update(GameContainer container, int delta) {
        Input input = container.getInput();

        super.update(container, delta);
        movingDirection.set(0f, 0f);

        if (isControllable()) {
            boolean isMoving = false;
            updateDirectionToPoint(input.getMouseX(), input.getMouseY());

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

            if (isMoving) {
                world.getGame().getClient().directionChange(this);
                world.getGame().getCamera().alignCenterOn(this);
            }

            long currentShootTime = System.currentTimeMillis();
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && currentShootTime - lastShootTime > 125) {
                lastShootTime = currentShootTime;
                Vector2f heading = getHeadingVector();
                double angle = Math.atan2(heading.y, heading.x);
                world.addBullet(getX(), getY(), this, new Vector2f((float) Math.cos(angle), (float) Math.sin(angle)), false);
            }
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(animations[currentAnimationState].getCurrentFrame(),camera.getX(getX()), camera.getY(getY()));
        //        g.setColor(Color.green);
        //        g.fillRect(camera.getX(getX()), camera.getY(getY()), getWidth(), getHeight());
        //        g.setColor(Color.white);
        //        g.drawString(isControllable() ? "c" : "", camera.getX(getX() + 5), camera.getY(getY() + 15));
    }

    public boolean isControllable() {
        return getId() == world.getGame().getUserId();
    }
}