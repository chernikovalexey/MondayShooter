package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by Alexey
 * At 7:24 PM on 3/5/14
 */

public class Pit extends Entity {
    private static final int CAPACITY = 400;

    private int loaded = 0;

    public Pit(float x, float y) {
        super(x, y, 0, 128, 0, 64, true);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(image, camera.getX(this), camera.getY(this));
    }
}