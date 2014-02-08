package com.twopeople.game.particle;

import com.twopeople.game.Camera;
import com.twopeople.game.IRenderable;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Player;
import com.twopeople.game.world.World;
import org.newdawn.slick.Color;
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

    protected float z;
    protected float velz;

    private Entity associatedEntity;

    public boolean hasShadow = true;
    public boolean collides = true;

    public MSParticle(World world, ParticleSystem engine) {
        super(engine);
        this.world = world;
        this.associatedEntity = new Entity(x, y, z, size, size, size, false);
    }

    @Override
    public void update(int delta) {
        float px = x;
        float py = y;

        super.update(delta);

        this.x = px;
        this.y = py;

        float d = delta * 0.150f;
        int steps = (int) Math.sqrt(velx * velx + vely * vely + velz * velz) + 1;

        for (int i = 0; i < steps; ++i) {
            partialMove(d * velx / steps, d * vely / steps, delta);
        }

        if (life > 0) {
            z += d * velz;
        }
    }

    private void partialMove(float dx, float dy, int delta) {
        x += dx;
        y += dy;

        if (collides) {
            associatedEntity.setX(x);
            associatedEntity.setY(y);
            associatedEntity.setZ(z);
            Collection<Entity> nearby = world.getEntities().getNearbyEntities(associatedEntity);

            for (Entity e : nearby) {
                if (associatedEntity.collidesWith(e) && !(e instanceof Player)) {
                    Vector2f hitSide = e.getHitSideVector(associatedEntity);

                    Vector2f u = hitSide.getPerpendicular();
                    Vector2f w = hitSide.sub(u);
                    w.x *= 0.01f;
                    w.y *= 0.01f;
                    u.x *= 0.001f;
                    u.y *= 0.001f;
                    Vector2f v2 = w.sub(u);

                    v2.x *= delta * 0.05f;
                    v2.y *= delta * 0.05f;

                    //System.out.println(v2);

                    velx = v2.x;
                    vely = v2.y;

                    x -= dx;
                    y -= dy;
                    break;
                }
            }
        }
    }

    @Override
    public void render() {}

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        if (inUse()) {
            // Because of not extending `Entity` I am repeating myself
            if (hasShadow) {
                float shadowDistance = z - size;
                float shadowOpacity = 0.1f;

                g.setColor(new Color(0, 0, 0, shadowOpacity));
                float sw = size * 1.5f;
                g.fillOval(camera.getX(x + (size - sw) / 2), camera.getY(y + size + shadowDistance - z), sw, size / 1.75f);

            }

            // ================
            // After the shadow

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
        velx = velx * dx;
    }

    public void increaseVelocityY(float dy) {
        vely = vely * dy;
    }

    public void increaseVelocityZ(float dz) {
        velz = velz * dz;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}