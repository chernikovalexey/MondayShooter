package com.twopeople.game.particle;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Created by Alexey
 * At 8:27 PM on 1/26/14
 */

public class BloodDebrisEmitter implements ParticleEmitter {
    private float x, y;

    private int timer;
    private int particlesAmount = 0;

    public BloodDebrisEmitter(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(ParticleSystem particleSystem, int delta) {
        if (timer <= 0) {
            timer = 1;

            for (int i = 0; i < 24 + MSParticle.random.nextInt(12); ++i) {
                MSParticle p = (MSParticle) particleSystem.getNewParticle(this, 2500);

                float z = 0;
                p.setPosition(x, y, z);

                float vx = (float) (Math.random() * 0.255f);
                float vy = (float) (Math.random() * 0.155f);
                float vz = (float) (-(Math.random() * 0.35f)) * 0.752f;

                System.out.println(vx + ", " + vy + ", " + vz);

                p.setVelocity(vx, vy, vz, 1.5f);

                p.setStartPoint(x, y);
            }
        }
    }

    public static float getDist(float x1, float y1, float x2, float y2) {
        return (float) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }

    @Override
    public void updateParticle(Particle particle, int delta) {
        MSParticle p = (MSParticle) particle;

        if (p.finished) {
            return;
        }

        float dragging = 0.012f;
        float gravity = 0.035f;

        if (p.getZ() < 1f) {
            p.velz = 0f;
            if (p.bounced) {
                if (p.getVelocityX() <= 0) {
                    p.finished = true;
                    p.setSpeed(0f);
                }
                p.increaseVelocityX(0.764f);
                p.increaseVelocityY((float) Math.random() * 2f * 0.764f);
            } else {
                p.bounced = true;
                p.increaseVelocityY(0.965f);
            }
        } else {
            gravity -= 0.05f;
            p.adjustVelocity(0, 0, gravity);
            // p.increaseVelocityZ(0.12f);
        }
    }

    @Override
    public boolean completed() {
        return timer == 0 || particlesAmount > 0;
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