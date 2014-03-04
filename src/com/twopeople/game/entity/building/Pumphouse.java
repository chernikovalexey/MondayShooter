package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Railcar;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Alexey
 * At 11:44 PM on 3/3/14
 */

public class Pumphouse extends Entity {
    public static final float WIDTH = 240;
    public static final float HEIGHT = 170;
    public static final float DEPTH = 40;

    private boolean activated = false;
    private int timer = 240000;

    public Pumphouse(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);

        this.range = 100f;
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        /*Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_UP)) {
            y -= 2f;
        }*/

        if (activated) {
            timer -= delta;
            if (timerRanOut()) {
                timer = 0;
                activated = false;

                // Put a railcar on rails
                for (Entity e : world.getFilteredEntities(Railroad.class)) {
                    Railroad road = (Railroad) e;
                    if (road.isStartingSection()) {
                        world.addEntity(new Railcar(road.getX(), road.getY() - 15), true);
                    }
                }
            }
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.pumphouse.getSprite(0, 0), camera.getX(this), camera.getY(this));
        g.setColor(Color.white);

        g.setColor(new Color(213, 43, 22, 105));
        Shape bb = getBB();
        bb.setX(camera.getX(bb.getX()));
        bb.setY(camera.getY(bb.getY()));
        //g.fill(bb);

        g.drawString("" + y, camera.getX(this), camera.getY(this));
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