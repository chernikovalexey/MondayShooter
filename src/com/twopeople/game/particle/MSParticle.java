package com.twopeople.game.particle;

import com.twopeople.game.Camera;
import com.twopeople.game.IEntity;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Created by Alexey
 * At 9:14 PM on 1/26/14
 */

public class MSParticle extends Particle implements IEntity {
    protected World world;

    protected float z;
    protected float velz;

    protected Entity associatedEntity;

    public MSParticle(World world, ParticleSystem engine) {
        super(engine);
        this.world = world;
        this.associatedEntity = new Entity(x, y, z, size, size, 0, false);
    }

    @Override
    public void render() {}

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        if (inUse()) {
            GL.glPushMatrix();

            GL.glTranslatef(camera.getX(x), camera.getY(y - z), 0);

            if (oriented) {
                float angle = (float) (Math.atan2(y, x) * 180 / Math.PI);
                GL.glRotatef(angle, 0f, 0f, 1.0f);
            }

            // scale
            GL.glScalef(1.0f, scaleY, 1.0f);

            image.draw((int) (-(size / 2)), (int) (-(size / 2)), (int) size, (int) size, color);
            GL.glPopMatrix();
        }
    }

    // =======
    // Getters and setters

    @Override
    public Vector2f getBBCentre() {
        return associatedEntity.getBBCentre();
    }

    @Override
    public float getWidth() {
        return size;
    }

    @Override
    public float getHeight() {
        return size;
    }

    public void setVelocity(float dx, float dy, float dz, float speed) {
        super.setVelocity(dx, dy, speed);
        velz = dz * speed;
    }

    public void adjustVelocity(float dx, float dy, float dz) {
        adjustVelocity(dx, dy);
        velz += dz;
    }

    @Override
    public void setSpeed(float speed) {
        // To avoid division by zero
        float currentSpeed = (float) Math.sqrt((velx * velx) + (vely * vely) + (velz * velz)) + 1;
        velx *= speed;
        vely *= speed;
        velz *= speed;
        velx /= currentSpeed;
        vely /= currentSpeed;
        velz /= currentSpeed;
    }

    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y);
        this.z = z;
    }

    public float getVelocityX() {
        return velx;
    }

    public float getVelocityY() {
        return vely;
    }

    public float getVelocityZ() {
        return velz;
    }

    public void setVelocityZ(float velz) {
        this.velz = velz;
    }

    public void increaseVelocityX(float dx) {
        velx *= dx;
    }

    public void increaseVelocityY(float dy) {
        vely *= dy;
    }

    public void increaseVelocityZ(float dz) {
        velz *= dz;
    }

    @Override
    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}