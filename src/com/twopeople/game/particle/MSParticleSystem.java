package com.twopeople.game.particle;

import com.twopeople.game.particle.debris.DebrisEmitter;
import com.twopeople.game.world.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Alexey
 * At 9:15 PM on 1/26/14
 */

public class MSParticleSystem extends ParticleSystem {
    protected Class<?> clazz;
    protected World world;

    public MSParticleSystem(World world, Image defaultSprite, int maxParticles, Class<?> clazz) {
        super(defaultSprite, maxParticles);
        this.clazz = clazz;
        this.world = world;
    }

    public MSParticleSystem(World world, SpriteSheet sprite, int maxParticles, Class<?> clazz) {
        this(world, sprite.getSprite(world.getRandom().nextInt(sprite.getHorizontalCount()), world.getRandom().nextInt(sprite.getVerticalCount())), maxParticles, clazz);
    }

    public ArrayList<MSParticle> getAllParticles() {
        ArrayList<MSParticle> particles = new ArrayList<MSParticle>();
        for (Object emitter : emitters) {
            particles.addAll(((DebrisEmitter) emitter).getParticles());
        }
        return particles;
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