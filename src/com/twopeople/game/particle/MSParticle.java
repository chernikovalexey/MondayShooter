package com.twopeople.game.particle;

import com.twopeople.game.Camera;
import com.twopeople.game.IRenderable;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Player;
import com.twopeople.game.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.Collection;

/**
 * Created by Alexey
 * At 9:14 PM on 1/26/14
 */

public class MSParticle extends Particle implements IRenderable {
    private World world;
    private ParticleSystem engine;

    protected float z;
    protected float velz;

    private Entity associatedEntity;

    public MSParticle(World world, ParticleSystem engine) {
        super(engine);
        this.world = world;
        this.engine = engine;
        this.associatedEntity = new Entity(x, y, 0, size, size, size, false);
    }

    @Override
    public void update(int delta) {
        float vx = velx;
        float vy = vely;

        this.velx = 0f;
        this.vely = 0f;

        super.update(delta);

        this.velx = vx;
        this.vely = vy;

        float d = delta * 0.25f;
        int steps = (int) Math.sqrt(velx * velx + vely * vely + velz * velz) + 1;

        for (int i = 0; i < steps; ++i) {
            partialMove(d * velx / steps, 0, 0);
            partialMove(0, d * vely / steps, 0);
        }

        if (life > 0) {
            z += d * velz;
        }
    }

    private void partialMove(float dx, float dy, float dz) {
        x += dx;
        y += dy;

        associatedEntity.setX(x);
        associatedEntity.setY(y);
        associatedEntity.setZ(z/2);
        Collection<Entity> nearby = world.getEntities().getNearbyEntities(associatedEntity);

        //        System.out.println(nearby.size());

        for (Entity e : nearby) {
            if (associatedEntity.collidesWith(e) && !(e instanceof Player)) {
                collideWithEntity(dx, dy, dz);
                x -= dx;
                y -= dy;
                break;
            }
        }
    }

    private void collideWithEntity(float dx, float dy, float dz) {
        if (dx != 0) { velx = -velx; }
        if (dy != 0) { vely = -vely; }
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

            image.draw((int) (-(size / 2)), (int) (-(size / 2)), (int) size,
                       (int) size, color);
            GL.glPopMatrix();
        }
    }

    @Override
    public Vector2f getBBCentre() {
        return associatedEntity.getBBCentre();
    }

    @Override
    public float getHeight() {
        return size;
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