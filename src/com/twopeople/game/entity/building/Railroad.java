package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.ArrayList;

/**
 * Created by Alexey
 * At 4:45 PM on 2/22/14
 */

public class Railroad extends Entity {
    public static float WIDTH = 128;
    public static float HEIGHT = 0;
    public static float DEPTH = 65;

    public Railroad(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(image, camera.getX(this), camera.getY(this));
        g.setColor(new Color(255, 255, 255, 200));

        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.setColor(new Color(255, 255, 255, 155));
            g.fill(shape);
        }

        //g.drawString(id + ", " + team.id, camera.getX(this), camera.getY(this));
    }

    @Override
    public Shape getBB() {
        return new Rectangle(Float.MIN_VALUE, Float.MIN_VALUE, 1, 1);
    }

    public static Entity getRailroadPart(ArrayList<Entity> railroad, String key, int val) {
        for (Entity e : railroad) {
            if (e.getProperties().getFirstValue(key) == val) {
                return e;
            }
        }
        return null;
    }
}