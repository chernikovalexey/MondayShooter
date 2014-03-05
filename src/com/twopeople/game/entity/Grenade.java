package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.entity.building.Wall;
import com.twopeople.game.particle.ParticleManager;
import com.twopeople.game.particle.debris.GrenadeSplinterEmitter;
import com.twopeople.game.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;

import java.io.File;
import java.io.IOException;

/**
 * Created by Alexey
 * At 7:51 PM on 3/5/14
 */

public class Grenade extends Bullet {
    public static final float SIZE = 5;

    public Grenade() {
    }

    public Grenade(World world, float x, float y, float z, Vector2f movingDirection) {
        super(world, x, y, z, movingDirection);

        setSpeed(9.8f);
        this.airFriction = 0.09f / 2f;
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        world.getBullets().move(this);
        super.update(container, delta, entities);
        world.getBullets().move(this);

        if (world.ranOut(this)) {
            remove = true;
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        renderOvalShadow(camera, g, z - height * 10, 0.2f);
        image.getScaledCopy(1.5f).draw(camera.getX(this), camera.getY(this));
    }

    public Shape getBB() {
        return new Circle(x, y, SIZE / 2);
    }

    @Override
    public boolean collidesWith(Entity entity) {
        if (entity instanceof Player && entity.getConnectionId() == getOwner()) {
            return false;
        }
        return super.collidesWith(entity);
    }

    @Override
    public void bumpedInto(Entity entity) {
        remove = true;
        entity.hurt(this, 15);

        if (entity instanceof Wall) {
            float dist = entity.getBBCentre().distance(getBBCentre());
            float az = dist < 35 ? z / 2 : z;


        }
    }

    @Override
    public void onLanding() {
        remove = true;

        GrenadeSplinterEmitter gsEmitter = new GrenadeSplinterEmitter(world, x, y);
        world.getParticleSystem(ParticleManager.CHIPPING_DEBRIS).addEmitter(gsEmitter);

        try {
            Camera camera = world.getGame().getCamera();
            File xmlFile = new File("res/explosion.xml");
            ConfigurableEmitter emitter = ParticleIO.loadEmitter(xmlFile);

            emitter.setPosition(camera.getX(this) / 2, camera.getY(this) / 2);

            world.getParticleSystem(ParticleManager.GRENADE_EXPLOSIONS).addEmitter(emitter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}