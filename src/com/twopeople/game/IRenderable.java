package com.twopeople.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 8:26 PM on 2/3/14
 */

public interface IRenderable {
    public void render(GameContainer container, Camera camera, Graphics g);

    public Vector2f getBBCentre();

    public float getHeight();

    public float getZ();
}