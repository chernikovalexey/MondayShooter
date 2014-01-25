package com.twopeople.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Alexey
 * At 12:35 PM on 1/25/14
 */

public class Wall extends Entity {
    public static final float WIDTH = 63;
    public static final float HEIGHT = 49;

    public Wall() {
    }

    public Wall(float x, float y) {
        super(x, y, WIDTH, HEIGHT, true);
        setLayer(2);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.tiles.getSprite(0, 1).getSubImage(0, 15, (int) WIDTH, (int) HEIGHT), camera.getX(getX()), camera.getY(getY()));

        /*g.setColor(new Color(204, 204, 204, 120));
        g.fillRect(camera.getX(getX()), camera.getY(getY()), WIDTH, HEIGHT);

        g.setColor(Color.gray);
        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.fill(shape);
            g.setColor(Color.green);
        }*/
    }

    @Override
    public Shape[] getSkeleton() {
        return new Shape[]{
                new Polygon(new float[]{getX(), getY() + HEIGHT / 2, getX() + WIDTH / 2, getY() + 3, getX() + WIDTH, getY() + HEIGHT / 2}),
                new Polygon(new float[]{getX(), getY() + HEIGHT / 2, getX() + WIDTH, getY() + HEIGHT / 2, getX() + WIDTH / 2, getY() + HEIGHT}),
        };
    }
}