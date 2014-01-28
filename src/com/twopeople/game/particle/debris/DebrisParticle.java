package com.twopeople.game.particle.debris;

import com.twopeople.game.World;
import com.twopeople.game.particle.MSParticle;
import org.newdawn.slick.particles.ParticleSystem;

public class DebrisParticle extends MSParticle {
    public boolean bounced = false;
    public boolean finished = false;

    public DebrisParticle(World world, ParticleSystem engine) {
        super(world, engine);
    }
}