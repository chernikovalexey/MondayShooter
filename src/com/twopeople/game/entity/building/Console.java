package com.twopeople.game.entity.building;

import com.twopeople.game.entity.Entity;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Alexey
 * At 7:32 PM on 3/5/14
 */

public class Console extends Entity {
    public Console(float x, float y) {
        super(x, y, 0, 128, Wall.HEIGHT, 64, true);
    }

    @Override
    public Shape getBB() {
        return new Rectangle(x + width / 2, y + height + depth / 2, 1, 1);
    }
}