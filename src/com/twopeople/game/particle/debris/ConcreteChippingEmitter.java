package com.twopeople.game.particle.debris;

import com.twopeople.game.world.World;

/**
 * Created by Alexey
 * At 5:44 PM on 2/8/14
 */

public class ConcreteChippingEmitter extends DebrisEmitter {
    public ConcreteChippingEmitter(World world, float x, float y, float z) {
        super(world, x, y, 200, 0, 0, z, 0.35f, 0.95f, 0.99f, 1f, 6, 10, 1.2f, 0f, 0f, 3, false, true);
    }
}