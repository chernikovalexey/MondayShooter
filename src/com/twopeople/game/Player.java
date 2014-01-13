package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Created by Alexey
 * At 7:40 PM on 1/13/14
 */

public class Player extends Entity {
    public static final float WIDTH = 32;
    public static final float HEIGHT = 32;

    public Player() {
    }

    public Player(World world, float x, float y) {
        super(world, x, y, WIDTH, HEIGHT);
    }


    @Override
    public void update(GameContainer container, int delta) {
        Input input = container.getInput();

        moveInertly(delta);

        System.out.println("Move?");

        if (input.isKeyDown(Input.KEY_W)) {
            move(0f, -10f);
        }

        if (input.isKeyDown(Input.KEY_S)) {
            move(0f, 10f);
        }

        if (input.isKeyDown(Input.KEY_A)) {
            move(-10f, 0f);
        }

        if (input.isKeyDown(Input.KEY_D)) {
            move(10f, 0f);
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.setColor(Color.green);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}