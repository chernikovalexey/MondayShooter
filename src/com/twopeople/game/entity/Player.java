package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.particle.ParticleManager;
import com.twopeople.game.particle.debris.BloodDebrisEmitter;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 7:40 PM on 1/13/14
 */

public class Player extends Entity {
    public static final float WIDTH = 70;
    public static final float HEIGHT = 50;
    public static final float DEPTH = 20;

    private long lastShootTime = System.currentTimeMillis();

    private float[][] gunshotOffsets = new float[][]{
            new float[]{41, -14}, new float[]{58, 3},
            new float[]{24, 0}, new float[]{8, -1},
            new float[]{8, -14}, new float[]{56, -1},
            new float[]{6, 12}, new float[]{48, 18}
    };

    public Player() {
        loadAnimations(Images.player);
    }

    public Player(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);

        setLayer(1);
        setSpeed(4.5f);
        loadAnimations(Images.player);
        setHealth(100, 100);
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        Input input = container.getInput();

        super.update(container, delta, entities);
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

            if (input.isKeyPressed(Input.KEY_P)) {
                world.getParticleSystem(ParticleManager.BLOOD_DEBRIS).addEmitter(new BloodDebrisEmitter(world, getX(), getY() - getZ()));
            }

            if (isMoving) {
                entities.move(this);
                world.getGame().getClient().directionChange(this);
                world.getGame().getCamera().alignCenterOn(this);

                int frame = animations[currentAnimationState].getFrame();
                if (++frame >= animations[currentAnimationState].getFrameCount()) {
                    frame = 0;
                }
                animations[currentAnimationState].setCurrentFrame(frame);
            }

            long currentShootTime = System.currentTimeMillis();
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && currentShootTime - lastShootTime > 125) {
                lastShootTime = currentShootTime;
                double angle = Math.atan2(getHeadingVector().y, getHeadingVector().x);
                world.addBullet(getX() + gunshotOffsets[currentAnimationState][0], getY() + height + gunshotOffsets[currentAnimationState][1], getZ() + 42, this, new Vector2f((float) Math.cos(angle), (float) Math.sin(angle)), false);
            }
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.setColor(new Color(243, 243, 243, 175));
        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.fill(shape);
        }

        g.drawImage(animations[currentAnimationState].getCurrentFrame(), camera.getX(this), camera.getY(this));
        //        g.drawString(getCellX() + ", " + getCellY(), camera.getX(this), camera.getY(this));

        g.setColor(new Color(204, 204, 204, 120));
        //        g.fillRect(camera.getX(getX()), camera.getY(getY()), WIDTH, getOrthogonalHeight());
    }

    @Override
    public Shape getBB() {
        return new Circle(getX() + WIDTH / 2, getY() + HEIGHT + 12, 12);
    }

    public boolean isControllable() {
        return getId() == world.getGame().getUserId();
    }

    public Vector2f respawn() {
        Vector2f spawner = world.getRandomSpawner();
        respawnAt(spawner);
        return spawner;
    }

    public void respawnAt(Vector2f spawner) {
        setX(spawner.x + 64 / 2);
        setY(spawner.y - 16);
        world.getGame().getCamera().alignCenterOn(this);
        world.getEntities().move(this);
    }

    @Override
    public void killed(Entity by) {
        Vector2f spawner = respawn();
        world.getGame().getClient().killed(by.getId(), spawner.x, spawner.y);
    }
}