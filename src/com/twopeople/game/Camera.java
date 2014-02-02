package com.twopeople.game;

import com.twopeople.game.entity.Entity;
import org.newdawn.slick.GameContainer;

/**
 * Created by Alexey
 * At 7:51 PM on 1/24/14
 */

public class Camera {
    private GameContainer container;

    private float targetX, targetY;
    public float x, y;

    public Camera(GameContainer container) {
        this.container = container;
    }

    public void update(int delta) {
        x += (targetX - x);
        y += (targetY - y);
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public boolean isVisible(float ox, float oy, float width, float height) {
        return ox + width * 2 >= x && oy + height * 2 >= y && ox - width * 2 <= x + container.getWidth() && oy - height * 2 <= y + container.getHeight();
    }

    public boolean isVisible(Entity entity) {
        return isVisible(entity.getX(), entity.getY() - entity.getZ(), entity.getWidth(), entity.getDepth());
    }

    public void alignCenterOn(Entity entity) {
        setTargetX(entity.getX() - container.getWidth() / 2 + entity.getWidth());
        setTargetY(entity.getY() - container.getHeight() / 2 + entity.getHeight());
    }

    public float getX(float ex) {
        return ex - this.x;
    }

    public float getY(float ey) {
        return ey - this.y;
    }

    public float getX(Entity entity) {
        return getX(entity.getX());
    }

    public float getY(Entity entity) {
        return getY(entity.getY() - entity.getZ());
    }
}