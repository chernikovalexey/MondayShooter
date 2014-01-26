package com.twopeople.game.particle;

import com.twopeople.game.World;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Created by Alexey
 * At 9:15 PM on 1/26/14
 */

public class MSParticleSystem extends ParticleSystem {
    private World world;

    public MSParticleSystem(World world, String defaultSpriteRef, int maxParticles) {
        super(defaultSpriteRef, maxParticles);
        this.world = world;
    }

    @Override
    protected Particle createParticle(ParticleSystem system) {
        return new MSParticle(world, system);
    }
}