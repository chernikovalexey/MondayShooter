package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 12:35 PM on 1/25/14
 */

public class Wall extends Entity {
    public static final float WIDTH = 128;
    public static final float HEIGHT = 62;
    public static final float DEPTH = 64;

    public Wall() {
    }

    public Wall(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(image, camera.getX(this), camera.getY(this));

        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.setColor(new Color(255, 255, 255, 255));
            //                g.fill(shape);
        }

        g.setColor(new Color(204, 204, 204, 120));
        //                g.fillRect(camera.getX(getX()), camera.getY(getY()), WIDTH, getOrthogonalHeight());
    }

    @Override
    public Vector2f getBBCentre() {
        Shape[] skeleton = getSkeleton();
        return new Vector2f(skeleton[0].getCenterX(), skeleton[0].getCenterY() + (skeleton[1].getCenterY() - skeleton[0].getCenterY()) / 2);
    }

    @Override
    public Vector2f getHitSideVector(Entity entity) {
        Shape[] skeleton = getSkeleton();
        Vector2f vector = new Vector2f();
        float ex = entity.getBBCentre().getX();
        float ey = entity.getBBCentre().getY();
        float cx = getBBCentre().getX();
        float cy = getBBCentre().getY();

        if (cx > ex && cy > ey) {
            // 1
            vector.x = skeleton[0].getPoint(1)[0] - skeleton[0].getPoint(0)[0];
            vector.y = skeleton[0].getPoint(1)[1] - skeleton[0].getPoint(0)[1];
        } else if (cx > ex && cy < ey) {
            // 4
            vector.x = skeleton[1].getPoint(0)[0] - skeleton[1].getPoint(2)[0];
            vector.y = skeleton[1].getPoint(0)[1] - skeleton[1].getPoint(2)[1];
        } else if (cx < ex && cy > ey) {
            // 2
            vector.x = skeleton[0].getPoint(2)[0] - skeleton[0].getPoint(1)[0];
            vector.y = skeleton[0].getPoint(2)[1] - skeleton[0].getPoint(1)[1];
        } else if (cx < ex && cy < ey) {
            // 3
            vector.x = skeleton[1].getPoint(2)[0] - skeleton[1].getPoint(1)[0];
            vector.y = skeleton[1].getPoint(2)[1] - skeleton[1].getPoint(1)[1];
        }

        return vector;
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