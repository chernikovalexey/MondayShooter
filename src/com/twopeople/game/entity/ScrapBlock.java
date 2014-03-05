package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.Images;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 7:39 PM on 3/5/14
 */

public class ScrapBlock extends Entity {
    public static final float WIDTH = 62;
    public static final float HEIGHT = 32;
    public static final float DEPTH = 32;

    public ScrapBlock(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.scrap.getSprite(0, 0), camera.getX(this), camera.getY(this));

        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.setColor(new Color(255, 255, 255, 155));
            g.fill(shape);
        }
    }

    @Override
    public Vector2f getBBCentre() {
        Shape[] skeleton = getSkeleton();
        return new Vector2f(skeleton[0].getCenterX(), skeleton[0].getCenterY() + (skeleton[1].getCenterY() - skeleton[0].getCenterY()) / 2);
    }

    @Override
    public Shape[] getSkeleton() {
        float yo = HEIGHT;
        return new Shape[]{
                new Polygon(new float[]{x, yo + y + DEPTH / 2, x + WIDTH / 2, yo + y, x + WIDTH, yo + y + DEPTH / 2}),
                new Polygon(new float[]{x, yo + y + DEPTH / 2, x + WIDTH, yo + y + DEPTH / 2, x + WIDTH / 2, yo + y + DEPTH}),
        };
    }
}