package com.twopeople.game.particle;

import com.twopeople.game.Images;
import com.twopeople.game.particle.debris.Debris;
import com.twopeople.game.world.World;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.HashMap;

/**
 * Created by Alexey
 * At 5:32 PM on 1/25/14
 */

public class ParticleManager {
    public final HashMap<Integer, ParticleSystem> list = new HashMap<Integer, ParticleSystem>();

    public static final int BLOOD_DEBRIS = 1;
    public static final int GUNSHOT_DEBRIS = 2;
    public static final int CHIPPING_DEBRIS = 3;
    public static final int BULLET_HOLE_DEBRIS = 4;
    public static final int GRENADE_EXPLOSIONS = 5;

    public ParticleManager(World world) {
        list.put(BLOOD_DEBRIS, new MSParticleSystem(world, Images.particles, 200, Debris.class));
        list.put(GUNSHOT_DEBRIS, new ParticleSystem("res/gunshots.png", 200));
        list.put(CHIPPING_DEBRIS, new MSParticleSystem(world, Images.concrete_chipping, 200, Debris.class));
        list.put(BULLET_HOLE_DEBRIS, new MSParticleSystem(world, Images.concrete_chipping, 200, Debris.class));
        list.put(GRENADE_EXPLOSIONS, new ParticleSystem("res/gunshots.png", 200));
    }

    public ParticleSystem get(int id) {
        return list.get(id);
    }
}