package com.twopeople.game.particle.debris;

import com.twopeople.game.world.World;

/**
 * Created by Alexey
 * At 12:15 AM on 2/8/14
 */

public class GunshotDebrisEmitter extends DebrisEmitter {
    public GunshotDebrisEmitter(World world, float x, float y, float vx, float vy) {
        super(world, x, y, 250, 0, world.getRandom().nextInt(4), 40 + world.getRandom().nextInt(4), 0.05f, 0.99f, 0.99f, 0.0f, 12, 16, 1.2f, vx, vy);
    }
}