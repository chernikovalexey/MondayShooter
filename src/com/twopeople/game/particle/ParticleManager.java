package com.twopeople.game.particle;

import com.twopeople.game.Images;
import com.twopeople.game.particle.debris.DebrisParticle;
import com.twopeople.game.particle.debris.GunshotDebrisEmitter;
import com.twopeople.game.world.World;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.HashMap;

/**
 * Created by Alexey
 * At 5:32 PM on 1/25/14
 */

public class ParticleManager {
    public final HashMap<Integer, MSParticleSystem> list = new HashMap<Integer, MSParticleSystem>();

    public static final int BLOOD_DEBRIS = 1;
    public static final int GUNSHOT_DEBRIS = 2;

    public ParticleManager(World world) {
        list.put(BLOOD_DEBRIS, new MSParticleSystem(world, Images.particles, 200, DebrisParticle.class));
        list.put(GUNSHOT_DEBRIS, new MSParticleSystem(world, Images.gunshots, 200, DebrisParticle.class));
    }

    public ParticleSystem get(int id) {
        return list.get(id);
    }
}