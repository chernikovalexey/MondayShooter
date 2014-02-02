package com.twopeople.game.particle;

import com.twopeople.game.Camera;
import com.twopeople.game.world.World;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Created by Alexey
 * At 9:14 PM on 1/26/14
 */

public class MSParticle extends Particle {
    private World world;

    protected float z;
    protected float velz;

    public MSParticle(World world, ParticleSystem engine) {
        super(engine);
        this.world = world;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if (life > 0) {
            z += delta * velz;
        }
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
        float currentSpeed = (float) Math.sqrt((velx * velx) + (vely * vely) + (velz * velz));
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}