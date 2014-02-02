package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 4:28 PM on 2/2/14
 */

public class Fence extends Entity {
    public Fence() {
    }

    public Fence(float x, float y) {
        super(x, y, 0, Wall.WIDTH, Wall.HEIGHT, Wall.DEPTH, true);
        setLayer(2);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(image, camera.getX(this), camera.getY(this));

        for (float zo = 0; zo <= 0; zo++) {
            for (Shape shape : getSkeleton()) {
                shape.setX(camera.getX(shape.getX()));
                shape.setY(camera.getY(shape.getY() - 42));
                g.setColor(new Color(255, 255 - (int) zo, 255 - (int) zo, 255));
                //                g.fill(shape);
            }
        }

        g.setColor(new Color(204, 204, 204, 120));
        //        g.fillRect(camera.getX(getX()), camera.getY(getY()), width, getOrthogonalHeight());
    }

    @Override
    public Vector2f getHitSideVector(Entity entity) {
        Vector2f centre = getBBCentre();
        if (skin[0] == 0) {
            centre.x *= -1;
            centre.y *= -1;
        } else if (skin[0] == 2) {
            centre.y *= -1;
        } else if (skin[0] == 3) {
            centre.x *= -1;
        }
        return centre;
    }

    @Override
    public Shape getBB() {
        if (skin[0] == 0) {
            return new Line(x + width / 2, y + height, x + width, y + height + depth / 2);
        } else if (skin[0] == 1) {
            return new Line(x, y + height + depth / 2, x + width / 2, y + height + depth);
        } else if (skin[0] == 2) {
            return new Line(x + width / 2, y + height + depth, x + width, y + height + depth / 2);
        } else if (skin[0] == 3) {
            return new Line(x, y + height + depth / 2, x + width / 2, y + height);
        }
        return null;
    }
}