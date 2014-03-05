package com.twopeople.game.particle.debris;

import com.twopeople.game.entity.Player;
import com.twopeople.game.world.World;

/**
 * Created by Alexey
 * At 8:27 PM on 3/5/14
 */

public class GrenadeSplinterEmitter extends DebrisEmitter {
    public GrenadeSplinterEmitter(World world, float x, float y) {
        super(world, x, y, 1500, Player.WIDTH / 2, Player.HEIGHT, 16, 0.1f, 0.95f, 0.959f, 1f, 6, 10, 1.35f, 0, 0, 4f, true, true);
    }
}