package com.twopeople.game.particle.debris;

import com.twopeople.game.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.ArrayList;

public class DebrisEmitter implements ParticleEmitter {
    private World world;
    private ArrayList<Debris> particles = new ArrayList<Debris>();

    private float x, y;
    private boolean used = false;

    private int minParticles, maxParticles;
    private int particleLife;
    private float xOffset, yOffset, zOffset;
    private float gravity;
    private float dx, dy, dz;
    private float strength;
    private float velx, vely;
    private float size;
    private boolean hasShadow = true;
    private boolean collides = true;

    public DebrisEmitter(World world, float x, float y, int particleLife,
                         float xOffset, float yOffset, float zOffset,
                         float gravity, float dx, float dy, float dz,
                         int minParticles, int maxParticles, float strength,
                         float velx, float vely, float size, boolean hasShadow, boolean collides) {
        this.world = world;
        this.x = x;
        this.y = y;

        this.particleLife = particleLife;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.gravity = gravity;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;

        this.minParticles = minParticles;
        this.maxParticles = maxParticles;
        this.strength = strength;
        this.velx = velx;
        this.vely = vely;
        this.size = size;
        this.hasShadow = hasShadow;
        this.collides = collides;
    }

    @Override
    public void update(ParticleSystem particleSystem, int delta) {
        if (!used) {
            used = true;

            for (int i = 0; i < minParticles + world.getRandom().nextInt(maxParticles - minParticles + 1); ++i) {
                Debris p = (Debris) particleSystem.getNewParticle(this, particleLife);

                p.setPosition(x + xOffset, y + yOffset, zOffset);
                p.setSize(size);

                p.hasShadow = hasShadow;
                p.collides = collides;

                float vx, vy, vz, dd;

                do {
                    vx = world.getRandom().nextFloat() * 2 - 1;
                    vy = world.getRandom().nextFloat() * 2 - 1;
                    vz = world.getRandom().nextFloat() * 2 - 1;
                } while ((dd = vx * vx + vy * vy + vz * vz) > 1);
                dd = (float) Math.sqrt(dd);
                vx = vx / dd * strength;
                vy = (vy / dd * strength);
                vz = (vz / dd + 1f) * strength;

                p.setVelocity(vx, vy, vz, strength);
                if (velx != 0f && vely != 0f) {
                    p.setVelocity(velx, vely);
                }

                particles.add(p);
            }
        } else {
            /*Iterator<MSParticle> it = particles.iterator();
            while (it.hasNext()) {
                MSParticle particle = it.next();
                if (particle.getLife() < 10f) {
                    it.remove();
                }
            }*/
        }
    }

    @Override
    public void updateParticle(Particle particle, int delta) {
        Debris p = (Debris) particle;

        if (particle.getLife() < 50f) {
            particles.remove(particle);
        }

        if (!p.finished) {
            if (p.getZ() < 1f) {
                p.finished = true;
                p.setSpeed(0f);
            } else {
                p.increaseVelocityX(dx);
                p.increaseVelocityY(dy);
                p.increaseVelocityZ(dz);
                p.adjustVelocity(0, 0, -gravity);
            }

            //float c = 0.0065f * delta;
            //particle.adjustColor(0, -c / 2, -c * 2, -c / 4);
        }
    }

    public ArrayList<Debris> getParticles() {
        return particles;
    }

    @Override
    public boolean completed() {
        return used && particles.size() == 0;
    }

    @Override
    public void wrapUp() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean b) {
    }

    @Override
    public boolean useAdditive() {
        return false;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public boolean isOriented() {
        return false;
    }

    @Override
    public boolean usePoints(ParticleSystem particleSystem) {
        return false;
    }

    @Override
    public void resetState() {
    }
}