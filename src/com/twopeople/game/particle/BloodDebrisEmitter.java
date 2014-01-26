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

    //The particle emission rate
    private int interval = 50;

    // Time til the next particle
    private int timer;

    // The size of the initial particles
    private float size = 20;

    public BloodDebrisEmitter(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(ParticleSystem particleSystem, int delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = interval;
            Particle p = particleSystem.getNewParticle(this, 1000);
            p.setColor(1, 1, 1, 0.5f);
            p.setPosition(x, y);
            p.setSize(size);
            float vx = (float) (-0.02f + (Math.random() * 0.04f));
            float vy = (float) (-(Math.random() * 0.15f));
            p.setVelocity(vx, vy, 1.1f);
        }
    }

    @Override
    public void updateParticle(Particle particle, int delta) {
        System.out.println(particle);
        if (particle.getLife() > 600) {
            particle.adjustSize(0.07f * delta);
        } else {
            particle.adjustSize(-0.04f * delta * (size / 40.0f));
        }
        float c = 0.002f * delta;
        particle.adjustColor(0, -c / 2, -c * 2, -c / 4);
    }

    @Override
    public boolean completed() {
        return false;
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