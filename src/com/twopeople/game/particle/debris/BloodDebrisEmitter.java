package com.twopeople.game.particle.debris;

import com.twopeople.game.entity.Player;
import com.twopeople.game.world.World;

/**
 * Created by Alexey
 * At 12:28 PM on 1/28/14
 */

public class BloodDebrisEmitter extends DebrisEmitter {
    public BloodDebrisEmitter(World world, float x, float y) {
        super(world, x, y, 1500, Player.WIDTH / 2, Player.HEIGHT, 16, 0.1f, 0.95f, 0.959f, 1f, 24, 36, 1.15f, 0, 0);
    }
}