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
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;

import java.io.File;
import java.io.IOException;

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
            new float[]{40, 2}, new float[]{56, 16},
            new float[]{24, 25}, new float[]{10, 12},
            new float[]{16, 6}, new float[]{60, 8},
            new float[]{6, 20}, new float[]{46, 28}
    };

    public int kills, deaths;

    public Player() {
        loadAnimations(Images.player);
    }

    public Player(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);

        setSpeed(4.5f);
        loadAnimations(Images.player);
        setHealth(100, 100);
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        Input input = container.getInput();

        super.update(container, delta, entities);
        movingDirection.set(0f, 0f, 0f);

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
            if (input.isKeyPressed(Input.KEY_SPACE) && velocity.z == 0) {
                movingDirection.z = -1;
            }

            if (input.isKeyPressed(Input.KEY_P)) {
                world.getParticleSystem(ParticleManager.BLOOD_DEBRIS).addEmitter(new BloodDebrisEmitter(world, getX(), getY() - getZ()));
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
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && currentShootTime - lastShootTime > 105) {
                lastShootTime = currentShootTime;
                double angle = Math.atan2(getHeadingVector().y, getHeadingVector().x);
                shoot(new Vector2f((float) Math.cos(angle), (float) Math.sin(angle)), false);
            }
        }

        if (hasMoved) {
            entities.move(this);
        }
    }

    public void shoot(Vector2f vec, boolean fromReceiver) {
        world.addBullet(getX() + gunshotOffsets[currentAnimationState][0], getY() + height + gunshotOffsets[currentAnimationState][1], getZ() + 42, this, vec, fromReceiver);

        Camera camera = world.getGame().getCamera();

        try {
            File xmlFile = new File("res/gunshot.xml");
            ConfigurableEmitter emitter = ParticleIO.loadEmitter(xmlFile);

            // todo
            // play more with it
            emitter.setPosition((camera.getX(this)+gunshotOffsets[currentAnimationState][0])/2, (camera.getY(this)  + gunshotOffsets[currentAnimationState][1])/2);

            emitter.angularOffset.setValue((float) (getHeadingVector().getTheta() + 90));
            world.getParticleSystem(ParticleManager.GUNSHOT_DEBRIS).addEmitter(emitter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.setColor(new Color(243, 243, 243, 175));
        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            //            g.fill(shape);
        }

        renderOvalShadow(camera, g, 8f, 0.1f);

        g.drawImage(animations[currentAnimationState].getCurrentFrame(), camera.getX(this), camera.getY(this));
        g.setColor(Color.white);
        g.drawString("" + health, camera.getX(this) + 10, camera.getY(this));

        g.setColor(new Color(204, 204, 204, 120));
        //        g.fillRect(camera.getX(getX()), camera.getY(getY()), WIDTH, getOrthogonalHeight());
    }

    @Override
    public Shape getBB() {
        return new Circle(getX() + WIDTH / 2, getY() + HEIGHT + 12, 12);
    }

    public boolean isControllable() {
        return getConnectionId() == world.getGame().getUserId();
    }

    public Vector2f respawn() {
        Vector2f spawner = world.getRandomSpawner();
        respawnAt(spawner);
        return spawner;
    }

    public void respawnAt(Vector2f spawner) {
        if (spawner != null) {
            setX(spawner.x + 64 / 2);
            setY(spawner.y - 16);
            world.getGame().getCamera().alignCenterOn(this);
            world.getEntities().move(this);
            health = maxHealth;
        }
    }

    @Override
    public void killed(Entity by) {
        Player killedBy = (Player) world.getEntityByConnectionId(by.getOwner());

        if (isControllable()) {
            Vector2f spawner = respawn();
            world.getGame().getClient().killed(killedBy.getConnectionId(), spawner.x, spawner.y);
        }

        ++killedBy.kills;
        ++this.deaths;
    }
}