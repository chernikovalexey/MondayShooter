package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.entity.building.Railroad;
import com.twopeople.game.world.pathfinder.Path;
import com.twopeople.game.world.pathfinder.RailroadRouter;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 5:21 PM on 2/22/14
 */

public class Railcar extends Entity {
    public static float WIDTH = 128;
    public static float HEIGHT = 38;
    public static float DEPTH = 95;

    private RailroadRouter router;
    private Path path;

    public Railcar(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
        setSpeed(3.5f);

        this.range = 145f;
    }

    @Override
    public void init() {
        this.router = new RailroadRouter(world.getFilteredEntities(Railroad.class));
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        if (delta > 30) {
            return;
        }

        if (carrying != null && path != null) {
            if (!path.isFinished()) {
                Vector2f goalPosition = path.getCurrentTarget(this);
                setMovingDirectionToPoint(goalPosition.x, goalPosition.y);
            }

            float px = x;
            float py = y;

            super.update(container, delta, entities);
            movingDirection.set(0, 0);

            float dx = x - px;
            float dy = y - py;
            carrying.setX(carrying.getX() + dx);
            carrying.setY(carrying.getY() + dy);
            carrying.onBoundMove();
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.railcar.getSprite(0, 0), camera.getX(this), camera.getY(this));

        for (Shape shape : getSkeleton()) {
            shape.setX(camera.getX(shape.getX()));
            shape.setY(camera.getY(shape.getY()));
            g.setColor(new Color(255, 255, 255, 155));
            g.fill(shape);
        }

        if (router != null && path != null) {
            //router.render(camera, g);
            //path.render(camera,g);
        }
    }

    @Override
    public Shape[] getSkeleton() {
        float yo = height;
        float shrink = 19;
        float xo = 6;
        return new Shape[]{
                new Polygon(new float[]{x + xo, yo + y + DEPTH / 2 - shrink, x + WIDTH / 2, yo + y, x + WIDTH - xo, yo + y + DEPTH / 2 - shrink}),
                new Polygon(new float[]{x + xo, yo + y + DEPTH / 2 - shrink, x + WIDTH - xo, yo + y + DEPTH / 2 - shrink, x + WIDTH / 2, y + DEPTH}),
        };
    }

    @Override
    public void startCarrying() {
        constructPath();
    }

    public void constructPath() {
        Entity from = null;
        float minDist = Float.MAX_VALUE;

        for (Entity e : world.getEntities().getNearbyEntities(this)) {
            float dist = getBBCentre().distance(new Vector2f(e.getX(), e.getY()));
            if (e instanceof Railroad && dist < minDist) {
                from = e;
                minDist = dist;
            }
        }

        if (isCarrying()) {
            Entity e = Railroad.getRailroadPart(world.getFilteredEntities(Railroad.class), "end", ((Player) carrying).getTeam());
            path = router.construct(from, e);
        }
    }
}