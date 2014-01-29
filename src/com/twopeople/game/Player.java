package com.twopeople.game;

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
    public static final float HEIGHT = 70;

    private long lastShootTime = System.currentTimeMillis();

  /*  private float[][] gunshotOffsets = new float[]{
            new float[] {}, new float[] {},
            new float[] {}, new float[] {},
            new float[] {}, new float[] {},
            new float[] {}, new float[] {}
    };*/

    public Player() {
        loadAnimations(Images.player);
    }

    public Player(float x, float y) {
        super(x, y, WIDTH, HEIGHT, true);

        setLayer(1);
        setSpeed(3.1f);
        loadAnimations(Images.player);
        setHealth(100,100);
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

            if (input.isKeyPressed(Input.KEY_P)) {
                world.getParticleSystem(ParticleManager.BLOOD_DEBRIS).addEmitter(new BloodDebrisEmitter(getX(), getY()));
            }

            if (isMoving) {
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
                world.addBullet(getX(), getY(), this, new Vector2f((float) Math.cos(angle), (float) Math.sin(angle)), false);
            }
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.setColor(new Color(243, 243, 243, 175));
        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
//            g.fill(shape);
            g.setColor(Color.green);
        }

        g.drawImage(animations[currentAnimationState].getCurrentFrame(), camera.getX(getX()), camera.getY(getY()));
    }

    public Shape getBB() {
        return new Circle(getX() + WIDTH / 2, getY() + HEIGHT / 2 + 25, 12);
    }

    public boolean isControllable() {
        return getId() == world.getGame().getUserId();
    }

    public Spawner respawn() {
        Spawner spawner = world.getRandomSpawner();
        setX(spawner.x);
        setY(spawner.y);
        return spawner;
    }

    @Override
    public void killed(Entity by, boolean fromListener) {
        System.out.println("Killed by: " + by.getId());
        Spawner spawner = respawn();
        if (!fromListener) {
            world.getGame().getClient().killed(by.getId(), spawner.id);
        }
    }
}