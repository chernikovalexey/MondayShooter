package com.twopeople.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 12:35 PM on 1/25/14
 */

public class Wall extends Entity {
    public static final float WIDTH = 128;
    public static final float HEIGHT = 162;

    public Wall() {
    }

    public Wall(float x, float y) {
        super(x, y, WIDTH, HEIGHT, true);
        setLayer(2);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.setColor(Color.gray);
        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.fill(shape);
            g.setColor(Color.green);
        }

        g.drawImage(Images.walls.getSprite(0, 0), camera.getX(getX()), camera.getY(getY()));

        g.setColor(new Color(204, 204, 204, 120));
        g.fillRect(camera.getX(getX()), camera.getY(getY()), WIDTH, HEIGHT);
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

    // It is standing on a tile

    @Override
    public Shape[] getSkeleton() {
        return world.getTileAt(x, y + 98, 3, 15).getSkeleton();
    }
}