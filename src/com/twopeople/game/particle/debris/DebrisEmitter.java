package com.twopeople.game.particle.debris;

import com.twopeople.game.entity.Player;
import com.twopeople.game.particle.MSParticle;
import com.twopeople.game.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.ArrayList;

public class DebrisEmitter implements ParticleEmitter {
    private World world;
    private ArrayList<MSParticle> particles = new ArrayList<MSParticle>();

    private float x, y;
    private boolean used = false;
    private int amount;

    public DebrisEmitter(World world, float x, float y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(ParticleSystem particleSystem, int delta) {
        if (!used) {
            used = true;

            for (int i = 0; i < 36 + world.getRandom().nextInt(12); ++i) {
                MSParticle p = (MSParticle) particleSystem.getNewParticle(this, 3000);

                p.setPosition(x + Player.WIDTH / 2, y + Player.HEIGHT / 2, 16);

                float vx, vy, vz, dd;

                do {
                    vx = world.getRandom().nextFloat() * 2 - 1;
                    vy = world.getRandom().nextFloat() * 2 - 1;
                    vz = world.getRandom().nextFloat() * 2 - 1;
                } while ((dd = vx * vx + vy * vy + vz * vz) > 1);
                dd = (float) Math.sqrt(dd);
                float speed = 1f;
                vx = vx / dd * speed;
                vy = (vy / dd * speed);
                vz = (vz / dd + 1f) * speed;

                p.setVelocity(vx, vy, vz, speed);

                ++amount;
                particles.add(p);
            }
        }
    }

    @Override
    public void updateParticle(Particle particle, int delta) {
        DebrisParticle p = (DebrisParticle) particle;

        if (particle.getLife() < 20f) {
            --amount;
            particles.remove(particle);
        }

        if (!p.finished) {
            float gravity = 0.109f;

            if (p.getZ() < 1f) {
                p.setVelocityZ(0f);
                if (p.getVelocityX() > 1f) { p.setVelocity(0f, p.getVelocityY()); }
                if (p.getVelocityY() > 1f) { p.setVelocity(p.getVelocityX(), 0f); }
                if (p.bounced) {
                    if (p.getVelocityX() <= 0) {
                        p.finished = true;
                        p.setSpeed(0f);
                    }
                    p.increaseVelocityX(0.445f);
                    p.increaseVelocityY((float) Math.random() * 0.495f);
                } else {
                    p.bounced = true;
                    p.increaseVelocityY(0.965f);
                }
            } else {
                p.increaseVelocityX(0.895f);
                p.increaseVelocityY(0.735f);
                p.adjustVelocity(0, 0, -gravity);
            }

            float c = 0.0065f * delta;
            particle.adjustColor(0, -c / 2, -c * 2, -c / 4);
        }
    }

    public ArrayList<MSParticle> getParticles() {
        return particles;
    }

    @Override
    public boolean completed() {
        return used && amount == 0;
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