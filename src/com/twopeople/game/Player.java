package com.twopeople.game;

import com.twopeople.game.particle.BloodDebrisEmitter;
import com.twopeople.game.particle.ParticleManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;

import java.io.File;
import java.io.IOException;

/**
 * Created by Alexey
 * At 7:40 PM on 1/13/14
 */

public class Player extends Entity {
    public static final float WIDTH = 16;
    public static final float HEIGHT = 16;

    private long lastShootTime = System.currentTimeMillis();

    public Player() {
        loadAnimations(Images.player);
    }

    public Player(float x, float y) {
        super(x, y, WIDTH, HEIGHT, true);

        setLayer(1);
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

            if (input.isKeyPressed(Input.KEY_P)) {
                File file = new File("res/particle.xml");
                ConfigurableEmitter emitter = null;
                try {
                    emitter = ParticleIO.loadEmitter(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                emitter.setPosition(world.getGame().getCamera().getX(getX()), world.getGame().getCamera().getY(getY()));

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
            g.fill(shape);
            g.setColor(Color.green);
        }

        g.drawImage(animations[currentAnimationState].getCurrentFrame(), camera.getX(getX()), camera.getY(getY()));
    }

    public Shape getBB() {
        return new Circle(getX() + WIDTH / 2, getY() + HEIGHT / 2 + 4, (WIDTH + HEIGHT) / 4 - 4 / 2);
    }

    public boolean isControllable() {
        return getId() == world.getGame().getUserId();
    }
}