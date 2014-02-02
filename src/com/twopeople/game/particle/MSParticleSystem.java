package com.twopeople.game.particle;

import com.twopeople.game.world.World;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

import java.lang.reflect.Constructor;

/**
 * Created by Alexey
 * At 9:15 PM on 1/26/14
 */

public class MSParticleSystem extends ParticleSystem {
    protected Class<?> clazz;
    protected World world;

    public MSParticleSystem(World world, String defaultSpriteRef, int maxParticles, Class<?> clazz) {
        super(defaultSpriteRef, maxParticles);
        this.clazz = clazz;
        this.world = world;
    }

    @Override
    protected Particle createParticle(ParticleSystem system) {
        if (clazz != null) {
            try {
                Constructor<?> constructor = clazz.getConstructor(World.class, ParticleSystem.class);
                return (Particle) constructor.newInstance(new Object[]{world, system});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new MSParticle(world, system);
    }
}