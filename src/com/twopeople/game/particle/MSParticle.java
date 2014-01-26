package com.twopeople.game.particle;

import com.twopeople.game.Camera;
import com.twopeople.game.World;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Created by Alexey
 * At 9:14 PM on 1/26/14
 */

public class MSParticle extends Particle {
    private World world;

    public MSParticle(World world, ParticleSystem engine) {
        super(engine);
        this.world = world;
    }

    @Override
    public void render() {
        float px = getX();
        float py = getY();

        Camera camera = world.getGame().getCamera();
        this.x = camera.getX(px);
        this.y = camera.getY(py);

        super.render();

        this.x = px;
        this.y = py;
    }

    // =======
    // Getters

    // Do these all need a getter for the world?
}