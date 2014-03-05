package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Railcar;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Alexey
 * At 4:30 PM on 3/5/14
 */

public class Launcher extends Entity {
    public static final float WIDTH = 128;
    public static final float HEIGHT = 60;
    public static final float DEPTH = 64;

    private boolean activated = false;
    private int timer = 240000;

    public Launcher(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);

        this.range = 100f;
    }

    @Override
    public void initOnWorldReady() {
        System.out.println("railroad.size=" + world.getFilteredEntities(Railroad.class).size());
        Railroad road = (Railroad) Railroad.getRailroadPart(world.getFilteredEntities(Railroad.class), "starting", 1);
        if (road != null) {
            world.addEntity(new Railcar(road.getX(), road.getY() - 15), true);
        }
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        if (activated) {
            timer -= delta;
            if (timerRanOut()) {
                timer = 0;
                activated = false;
            }
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(image, camera.getX(this), camera.getY(this));
    }

    @Override
    public Shape getBB() {
        return new Rectangle(x + width / 2, y + height + depth / 2, 1, 1);
    }

    public void activate() {
        if (!activated) {
            activated = true;
        }
    }

    public int getTimer() {
        return timer;
    }

    public boolean timerRanOut() {
        return timer <= 0;
    }
}