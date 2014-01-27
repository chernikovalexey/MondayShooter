package com.twopeople.game.particle;

import com.twopeople.game.Camera;
import com.twopeople.game.World;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.Random;

/**
 * Created by Alexey
 * At 9:14 PM on 1/26/14
 */

public class MSParticle extends Particle {
    public static final Random random = new Random();

    private World world;

    // Allow it soaring
    public float z;
    public float velz;
    private float startX, startY;
    public boolean bounced = false;
    public boolean finished = false;

    public MSParticle(World world, ParticleSystem engine) {
        super(engine);
        this.world = world;
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        z += delta * velz;
    }

    @Override
    public void render() {
        float px = getX();
        float py = getY();

        Camera camera = world.getGame().getCamera();
        this.x = camera.getX(px);
        this.y = camera.getY(py - this.z);

        super.render();

        this.x = px;
        this.y = py;
    }

    // =======
    // Getters and setters

    // Do these all need a getter for the world?

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
        super.setSpeed(speed);
        velz *= speed;
    }

    public void setStartPoint(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y);
        this.z = z;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getVelocityX() {
        return velx;
    }

    public float getVelocityY() {
        return vely;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}