package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.entity.building.Fence;
import com.twopeople.game.entity.building.Wall;
import com.twopeople.game.particle.ParticleManager;
import com.twopeople.game.particle.debris.ConcreteChippingEmitter;
import com.twopeople.game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 10:08 PM on 1/13/14
 */

public class Bullet extends Entity {
    public static final float SIZE = 5;

    public Bullet() {
    }

    public Bullet(World world, float x, float y, float z, Vector2f movingDirection) {
        super(x, y, z, SIZE, SIZE, 0, false);
        this.movingDirection.set(movingDirection.x, movingDirection.y);
        setSpeed(20.0f);
        setWorld(world);
        this.airFriction = 0f;
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        world.getBullets().move(this);
        super.update(container, delta, entities);

        if (world.ranOut(this)) {
            remove = true;
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        renderOvalShadow(camera, g, z - height * 6, 0.2f);

        Image image = Images.bullets.getSprite(0, 0);
        image.setCenterOfRotation(SIZE / 2, SIZE / 2);
        image.setRotation((float) new Vector2f(movingDirection.x, movingDirection.y).getTheta() + 90);
        image.draw(camera.getX(this), camera.getY(this));

        g.setColor(new Color(204, 204, 204, 120));
//        g.fillRect(camera.getX(this), camera.getY(this), getWidth(), getOrthogonalHeight());
    }

    public Shape getBB() {
        return new Circle(x, y - z, SIZE / 2);
    }

    @Override
    public boolean collidesWith(Entity entity) {
        if (entity instanceof Player && entity.getId() == getOwner() || entity instanceof Fence) {
            return false;
        }
        return super.collidesWith(entity);
    }

    @Override
    public void bumpedInto(Entity entity) {
        remove = true;
        entity.hurt(this, 25);

        if (entity instanceof Wall) {
            ConcreteChippingEmitter emitter = new ConcreteChippingEmitter(world, x, y, z);
            world.getParticleSystem(ParticleManager.CHIPPING_DEBRIS).addEmitter(emitter);
        }
    }

    @Override
    public boolean alwaysFlying() {
        return true;
    }
}