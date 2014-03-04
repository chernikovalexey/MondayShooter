package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by Alexey
 * At 11:44 PM on 3/3/14
 */

public class Pumphouse extends Entity {
    public static final float WIDTH = 240;
    public static final float HEIGHT = 170;
    public static final float DEPTH = 40;

    public Pumphouse(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        /*Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_UP)) {
            y -= 2f;
        }*/
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.pumphouse.getSprite(0, 0), camera.getX(this), camera.getY(this));
        g.setColor(Color.white);
        g.drawString("" + y, camera.getX(this), camera.getY(this));
    }
}