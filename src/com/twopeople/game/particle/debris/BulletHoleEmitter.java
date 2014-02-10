package com.twopeople.game.particle.debris;

import com.twopeople.game.world.World;

/**
 * Created by Alexey
 * At 8:42 PM on 2/10/14
 */

public class BulletHoleEmitter extends DebrisEmitter {
    public BulletHoleEmitter(World world, float x, float y, float z) {
        super(world, x, y, 1250, 0, 0, z, 0.0f, 0.0f, 0.0f, 0.0f, 1, 1, 0.0f, 0.0f, 0.0f, 40, false, false);
    }
}